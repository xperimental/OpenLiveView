package net.sourcewalker.olv.messages;

import java.nio.ByteBuffer;

import net.sourcewalker.olv.messages.impl.CapsResponse;

public final class MessageDecoder {

    private static Class<? extends LiveViewResponse> getClassForId(byte id)
            throws DecodeException {
        switch (id) {
        case MessageConstants.MSG_GETCAPS_RESP:
            return CapsResponse.class;
        default:
            throw new DecodeException("No message found matching ID: " + id);
        }
    }

    public static final LiveViewResponse decode(byte[] message, int length)
            throws DecodeException {
        ByteBuffer buffer = ByteBuffer.wrap(message, 0, length);
        byte msgId = buffer.get();
        buffer.get();
        int payloadLen = buffer.getInt();
        if (payloadLen + 6 == length) {
            try {
                LiveViewResponse result = getClassForId(msgId).newInstance();
                result.readData(buffer);
                return result;
            } catch (IllegalAccessException e) {
                throw new DecodeException(e);
            } catch (InstantiationException e) {
                throw new DecodeException(e);
            }
        } else {
            throw new DecodeException("Invalid message length: "
                    + message.length + " (should be " + (payloadLen + 6) + ")");
        }
    }

}
