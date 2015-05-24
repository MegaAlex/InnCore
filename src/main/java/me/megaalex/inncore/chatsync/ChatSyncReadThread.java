package me.megaalex.inncore.chatsync;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;

public class ChatSyncReadThread extends BukkitRunnable {

    private final InputStream inputStream;
    private final ChatSyncManager manager;

    public ChatSyncReadThread(final InputStream inputStream, ChatSyncManager manager) {
        this.inputStream = inputStream;
        this.manager = manager;
    }

    @Override
    public void run() {
        final DataInputStream dataInputStream = new DataInputStream(inputStream);
        while(manager.isConnected()) {
            System.out.println("Reading...");
            try {
                byte packet = dataInputStream.readByte();
                int len = dataInputStream.readInt();
                System.out.printf("Packet %d, Size %d\n", packet, len);
                byte[] data = new byte[len];
                int bytesRead = inputStream.read(data);
                if(bytesRead != data.length) {
                    InnCore.getInstance().getLogger().info("Malformed packet - Packet with wrong length!");
                    continue;
                }
                processPacket(packet, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processPacket(final byte packet, final byte[] data) {
        switch (packet) {
            case 0x01 : manager.disconnectPacket(); break;
            case 0x02: processAuthRequestToken(data); break;
            case 0x31: manager.authSuccess(); break;
            case 0x32: manager.authFailure(); break;
            default: processUnknownPacket(packet, data);
        }
    }

    private void processUnknownPacket(final byte packet, final byte[] data) {
        InnCore.getInstance().getLogger().info("Malformed packet - Packet with wrong id!");
        manager.disconnect();
    }

    private void processAuthRequestToken(final byte[] data) {
        final String proxyPass = new String(data);
        manager.authTokenRequest(proxyPass);
    }
}
