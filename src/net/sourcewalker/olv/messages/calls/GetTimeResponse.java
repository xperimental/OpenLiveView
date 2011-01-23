package net.sourcewalker.olv.messages.calls;

import java.nio.ByteBuffer;

import net.sourcewalker.olv.messages.LiveViewCall;
import net.sourcewalker.olv.messages.MessageConstants;

public class GetTimeResponse extends LiveViewCall {

    public GetTimeResponse() {
        super(MessageConstants.MSG_GETTIME_RESP);
    }

    /*
     * (non-Javadoc)
     * @see net.sourcewalker.olv.messages.LiveViewRequest#getPayload()
     */
    @Override
    protected byte[] getPayload() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt((int) (System.currentTimeMillis() / 1000));
        buffer.put((byte) 0);
        return buffer.array();
    }

}