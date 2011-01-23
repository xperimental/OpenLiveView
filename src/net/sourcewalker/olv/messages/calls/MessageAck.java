package net.sourcewalker.olv.messages.calls;

import net.sourcewalker.olv.messages.LiveViewCall;
import net.sourcewalker.olv.messages.MessageConstants;

/**
 * @author Xperimental
 */
public class MessageAck extends LiveViewCall {

    private final byte ackMsgId;

    public MessageAck(byte ackMsgId) {
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
