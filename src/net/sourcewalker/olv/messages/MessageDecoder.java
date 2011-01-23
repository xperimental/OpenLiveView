package net.sourcewalker.olv.messages;

import java.nio.ByteBuffer;

import net.sourcewalker.olv.messages.response.CapsResponse;
import net.sourcewalker.olv.messages.response.DeviceStatus;
import net.sourcewalker.olv.messages.response.GetTimeResponse;
import net.sourcewalker.olv.messages.response.MenuItemsResponse;
import net.sourcewalker.olv.messages.response.Navigation;
import net.sourcewalker.olv.messages.response.ResultResponse;

public final class MessageDecoder {

    private static LiveViewResponse newInstanceForId(byte id)
            throws DecodeException {
        switch (id) {
        case MessageConstants.MSG_GETCAPS_RESP:
            return new CapsResponse();
        case MessageConstants.MSG_SETVIBRATE_ACK:
        case MessageConstants.MSG_SETLED_ACK:
            return new ResultResponse(id);
        case MessageConstants.MSG_GETTIME:
            return new GetTimeResponse();
        case MessageConstants.MSG_GETMENUITEMS:
            return new MenuItemsResponse();
        case MessageConstants.MSG_DEVICESTATUS:
            return new DeviceStatus();
        case MessageConstants.MSG_NAVIGATION:
            return new Navigation();
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
            LiveViewResponse result = newInstanceForId(msgId);
            result.readData(buffer);
            return result;
        } else {
            throw new DecodeException("Invalid message length: "
                    + message.length + " (should be " + (payloadLen + 6) + ")");
        }
    }

}
