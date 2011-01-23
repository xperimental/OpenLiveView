package net.sourcewalker.olv.messages.request;

import net.sourcewalker.olv.messages.LiveViewRequest;
import net.sourcewalker.olv.messages.MessageConstants;

/**
 * @author Xperimental
 */
public class Ack extends LiveViewRequest {

    private final byte ackMsgId;

    public Ack(byte ackMsgId) {
        super(MessageConstants.MSG_ACK);
        this.ackMsgId = ackMsgId;
    }

    /*
     * (non-Javadoc)
     * @see net.sourcewalker.olv.messages.LiveViewRequest#getPayload()
     */
    @Override
    protected byte[] getPayload() {
        return new byte[] { ackMsgId };
    }

}
