package net.sourcewalker.olv.messages.calls;

import java.nio.ByteBuffer;
import java.util.Date;

import net.sourcewalker.olv.messages.LiveViewCall;
import net.sourcewalker.olv.messages.MessageConstants;

public class GetTimeResponse extends LiveViewCall {

    private int time;

    public GetTimeResponse(Date time) {
        super(MessageConstants.MSG_GETTIME_RESP);

        this.time = (int) (time.getTime() / 1000);
        this.time -= time.getTimezoneOffset() * 60;
    }

    public GetTimeResponse() {
        this(new Date());
    }

    /*
     * (non-Javadoc)
     * @see net.sourcewalker.olv.messages.LiveViewRequest#getPayload()
     */
    @Override
    protected byte[] getPayload() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putInt(time);
        buffer.put((byte) 0);
        return buffer.array();
    }

}
