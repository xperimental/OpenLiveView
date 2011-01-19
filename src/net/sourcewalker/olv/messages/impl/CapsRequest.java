package net.sourcewalker.olv.messages.impl;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.sourcewalker.olv.messages.EncodingException;
import net.sourcewalker.olv.messages.LiveViewMessage;
import net.sourcewalker.olv.messages.MessageConstants;

public class CapsRequest extends LiveViewMessage {

    private static final byte HEADER_LENGTH = 1;

    public CapsRequest() {
        super(MessageConstants.MSG_GETCAPS);
    }

    /*
     * (non-Javadoc)
     * @see net.sourcewalker.olv.LiveViewMessage#getPayload()
     */
    @Override
    protected byte[] getPayload() {
        try {
            byte[] version = MessageConstants.CLIENT_SOFTWARE_VERSION
                    .getBytes("iso-8859-1");
            byte msgLength = (byte) (HEADER_LENGTH + version.length);
            ByteBuffer buffer = ByteBuffer.allocate(msgLength);
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.put(msgLength);
            buffer.put(version);
            return buffer.array();
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
    }
}
