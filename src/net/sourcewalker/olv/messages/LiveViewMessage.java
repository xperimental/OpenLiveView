package net.sourcewalker.olv.messages;


public abstract class LiveViewMessage {

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

}
