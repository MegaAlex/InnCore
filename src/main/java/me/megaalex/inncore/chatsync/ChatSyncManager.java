package me.megaalex.inncore.chatsync;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.Manager;
import me.megaalex.inncore.chatsync.network.AuthTokenPacket;
import me.megaalex.inncore.chatsync.network.ChatSyncPacket;
import me.megaalex.inncore.chatsync.network.DisconnectPacket;
import me.megaalex.inncore.config.ChatSyncConfig;

public class ChatSyncManager extends Manager {

    private boolean connected = false;
    private boolean authenticated = false;

    private PriorityBlockingQueue<ChatSyncPacket> packetQueue;

    private ChatSyncReadThread readThread;
    private ChatSyncWriteThread writeThread;

    private Socket socket;
    private ChatSyncConfig config;

    @Override
    public String getEnableConfigName() {
        return "chat.sync.enabled";
    }

    @Override
    public void onEnable() {
        final InnCore plugin = InnCore.getInstance();
        super.onEnable();

        config = InnCore.getInstance().getConfigManager().getChatSyncConfig();
        try {
            this.socket = new Socket(config.getProxyHost(), config.getProxyPort());
            this.connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            InnCore.getInstance().getLogger().warning("Couldn't connect to proxy! Disabling chat sync!");
            try {
                if(socket != null)
                    socket.close();
            } catch (IOException ignored) {
            }
            return;
        }
        this.packetQueue = new PriorityBlockingQueue<>();

        try {
            readThread = new ChatSyncReadThread(socket.getInputStream(), this);
            readThread.runTaskAsynchronously(plugin);
            writeThread = new ChatSyncWriteThread(packetQueue, socket.getOutputStream(), this);
            writeThread.runTaskAsynchronously(plugin);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        disconnectPacket();
    }

    public void authSuccess() {
        this.authenticated = true;
    }

    public void authFailure() {
        InnCore.getInstance().getLogger().warning("[ChatSync] Proxy authentication failure! Disconnecting!");
        disconnect();
    }
    public void disconnect() {
        disconnect("Disconnecting.");
    }

    public void disconnect(final String reason) {
        final DisconnectPacket packet = new DisconnectPacket(reason);
        queuePacket(packet);
    }

    public void disconnectPacket() {
        if(!this.connected) {
            return;
        }
        this.connected = false;
        this.authenticated = false;

        readThread.cancel();
        writeThread.cancel();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authTokenRequest(final String proxyPass) {
        if(!config.getProxyPass().equals(proxyPass)) {
            InnCore.getInstance().getLogger().warning("Wrong password received! Disconnecting!");
            this.disconnect("Wrong proxy password");
            return;
        }

        final AuthTokenPacket authPacket = new AuthTokenPacket(config.getServerName(), config.getServerPass());
        queuePacket(authPacket);
    }

    public void queuePacket(final ChatSyncPacket packet) {
        this.packetQueue.offer(packet);
    }
}
