package net.sourcewalker.olv.messages.response;

import java.nio.ByteBuffer;

import net.sourcewalker.olv.messages.LiveViewResponse;

public class ResultResponse extends LiveViewResponse {

    private byte result;

    public byte getResult() {
        return result;
    }

    public ResultResponse(byte id) {
        super(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sourcewalker.olv.messages.LiveViewResponse#readData(java.nio.ByteBuffer
     * )
     */
    @Override
    public void readData(ByteBuffer buffer) {
        result = buffer.get();
    }

}
