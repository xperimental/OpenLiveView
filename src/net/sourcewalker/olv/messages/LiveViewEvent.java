package net.sourcewalker.olv.messages;

import java.nio.ByteBuffer;

public abstract class LiveViewEvent extends LiveViewMessage {

    public LiveViewEvent(byte id) {
        super(id);
    }

    public abstract void readData(ByteBuffer buffer);

}
