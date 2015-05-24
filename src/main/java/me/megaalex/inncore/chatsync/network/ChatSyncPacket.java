package me.megaalex.inncore.chatsync.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import me.megaalex.inncore.chatsync.ChatSyncManager;
import me.megaalex.inncore.chatsync.UnsecureConnectionException;

public abstract class ChatSyncPacket implements Comparable<ChatSyncPacket> {

    public abstract boolean isSecure();
    public abstract int getPriority();

    public abstract byte getId();

    public int getDataSize() {
        return getData().length;
    }

    public byte[] getData() {
        return getJson().getBytes(Charset.forName("UTF-8"));
    }

    public abstract String getJson();

    @Override
    public int compareTo(final ChatSyncPacket o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }

    public void writePacket(final OutputStream outputStream, final ChatSyncManager manager) throws IOException, UnsecureConnectionException {
        if(this.isSecure() && !manager.isAuthenticated()) {
            throw new UnsecureConnectionException();
        }
        final DataOutputStream dataOutput = new DataOutputStream(outputStream);
        final byte[] data = this.getData();

        dataOutput.writeByte(this.getId());
        dataOutput.writeInt(data.length);
        dataOutput.write(data);

        outputStream.flush();
    }
}
