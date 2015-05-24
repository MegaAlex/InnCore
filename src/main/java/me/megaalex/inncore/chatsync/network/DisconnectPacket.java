package me.megaalex.inncore.chatsync.network;

import com.sk89q.worldedit.internal.gson.Gson;
import com.sk89q.worldedit.internal.gson.JsonObject;

public class DisconnectPacket extends ChatSyncPacket {

    private final String reason;

    public DisconnectPacket(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public int getPriority() {
        return 1000;
    }

    @Override
    public byte getId() {
        return 0x01;
    }

    @Override
    public String getJson() {
        final JsonObject jsonObject = new JsonObject();
        final Gson gson = new Gson();
        jsonObject.addProperty("reason", reason);
        return gson.toJson(jsonObject);
    }

    public String getReason() {
        return reason;
    }
}
