package net.sourcewalker.olv.messages.request;

import net.sourcewalker.olv.messages.LiveViewRequest;
import net.sourcewalker.olv.messages.MessageConstants;

public class DeviceStatusAck extends LiveViewRequest {

    public DeviceStatusAck() {
        super(MessageConstants.MSG_DEVICESTATUS_ACK);
    }

    /*
     * (non-Javadoc)
     * @see net.sourcewalker.olv.messages.LiveViewRequest#getPayload()
     */
    @Override
    protected byte[] getPayload() {
        return new byte[] { MessageConstants.RESULT_OK };
    }

}
