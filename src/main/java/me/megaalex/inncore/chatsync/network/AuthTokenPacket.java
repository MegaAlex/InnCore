package me.megaalex.inncore.chatsync.network;

import org.json.simple.JSONObject;

public class AuthTokenPacket extends ChatSyncPacket {

    private final String serverName;
    private final String serverPass;

    public AuthTokenPacket(String serverName, String serverPass) {
        this.serverName = serverName;
        this.serverPass = serverPass;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public int getPriority() {
        return 999;
    }

    @Override
    public byte getId() {
        return 0x02;
    }

    @Override
    public int getDataSize() {
        return getData().length;
    }


    @Override
    public String getJson() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("server", serverName);
        jsonObj.put("password", serverPass);

        return jsonObj.toJSONString();
    }


}
