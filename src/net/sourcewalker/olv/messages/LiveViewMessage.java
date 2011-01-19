package net.sourcewalker.olv.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class LiveViewMessage {

    /**
     * Header consists of two bytes and a int value (4 bytes).
     */
    private static final int HEADER_LENGTH = 6;

    private byte id;

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public LiveViewMessage(byte id) {
        this.id = id;
    }

    protected abstract byte[] getPayload();

    public byte[] getEncoded() {
        byte[] payload = getPayload();
        int msgLength = payload.length + HEADER_LENGTH;
        ByteBuffer msgBuffer = ByteBuffer.allocate(msgLength);
        msgBuffer.order(ByteOrder.BIG_ENDIAN);
        msgBuffer.put(id);
        msgBuffer.put((byte) 4);
        msgBuffer.putInt(msgLength);
        msgBuffer.put(payload);
        return msgBuffer.array();
    }
}
