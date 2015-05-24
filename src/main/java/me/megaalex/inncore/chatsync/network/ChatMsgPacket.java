package me.megaalex.inncore.chatsync.network;

import com.sk89q.worldedit.internal.gson.Gson;
import me.megaalex.inncore.chatsync.model.ChatMessage;

public class ChatMsgPacket extends ChatSyncPacket {

    private ChatMessage msg;

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public byte getId() {
        return 0x03;
    }

    @Override
    public String getJson() {
        final Gson gson = new Gson();
        return gson.toJson(msg);
    }
}
