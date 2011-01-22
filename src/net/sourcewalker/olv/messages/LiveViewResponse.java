package net.sourcewalker.olv.messages;

import java.nio.ByteBuffer;

public abstract class LiveViewResponse extends LiveViewMessage {

    public LiveViewResponse(byte id) {
        super(id);
    }

    public abstract void readData(ByteBuffer buffer);

}
