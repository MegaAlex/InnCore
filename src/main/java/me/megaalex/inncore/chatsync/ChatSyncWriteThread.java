package me.megaalex.inncore.chatsync;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.PriorityBlockingQueue;

import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.chatsync.network.ChatSyncPacket;
import me.megaalex.inncore.chatsync.network.DisconnectPacket;

public class ChatSyncWriteThread extends BukkitRunnable {

    private final PriorityBlockingQueue<ChatSyncPacket> packetQueue;
    private final ChatSyncManager manager;
    private final OutputStream outputStream;

    public ChatSyncWriteThread(PriorityBlockingQueue<ChatSyncPacket> packetQueue, final OutputStream outputStream,
                               final ChatSyncManager manager) {
        this.packetQueue = packetQueue;
        this.outputStream = outputStream;
        this.manager = manager;
    }


    @Override
    public void run() {

        while(manager.isConnected()) {
            try {
                final ChatSyncPacket packet = packetQueue.take();
                if(!manager.isConnected()) {
                    break;
                }
                packet.writePacket(outputStream, manager);
                callCallback(packet);
            } catch (InterruptedException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
                InnCore.getInstance().getLogger().warning("Couldn't send packet! Disconnecting!");
                manager.disconnect();
            } catch (UnsecureConnectionException e) {
                InnCore.getInstance().getLogger().fine("Dropping secure packet in unsecure connection.");
            }
        }

    }

    private void callCallback(final ChatSyncPacket packet) {
        if(packet instanceof DisconnectPacket) {
            manager.disconnectPacket();
        }
    }
}
