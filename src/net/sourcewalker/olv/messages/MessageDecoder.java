package net.sourcewalker.olv.messages;

import java.nio.ByteBuffer;

import net.sourcewalker.olv.messages.events.CapsResponse;
import net.sourcewalker.olv.messages.events.DeviceStatus;
import net.sourcewalker.olv.messages.events.GetMenuItems;
import net.sourcewalker.olv.messages.events.GetTime;
import net.sourcewalker.olv.messages.events.Navigation;
import net.sourcewalker.olv.messages.events.ResultEvent;
import android.util.Log;

public final class MessageDecoder {

    private static final String TAG = "MessageDecoder";

    private static LiveViewEvent newInstanceForId(byte id)
            throws DecodeException {
        switch (id) {
        case MessageConstants.MSG_GETCAPS_RESP:
            return new CapsResponse();
        case MessageConstants.MSG_SETVIBRATE_ACK:
        case MessageConstants.MSG_SETLED_ACK:
            return new ResultEvent(id);
        case MessageConstants.MSG_GETTIME:
            return new GetTime();
        case MessageConstants.MSG_GETMENUITEMS:
            return new GetMenuItems();
        case MessageConstants.MSG_DEVICESTATUS:
            return new DeviceStatus();
        case MessageConstants.MSG_NAVIGATION:
            return new Navigation();
        default:
            throw new DecodeException("No message found matching ID: " + id);
        }
    }

    public static final LiveViewEvent decode(byte[] message, int length)
            throws DecodeException {
        if (length < 4) {
            Log.w(TAG, "Got empty message!");
            throw new DecodeException("Can't decode empty message!");
        } else {
            ByteBuffer buffer = ByteBuffer.wrap(message, 0, length);
            byte msgId = buffer.get();
            buffer.get();
            int payloadLen = buffer.getInt();
            if (payloadLen + 6 == length) {
                LiveViewEvent result = newInstanceForId(msgId);
                result.readData(buffer);
                return result;
            } else {
                throw new DecodeException("Invalid message length: "
                        + message.length + " (should be " + (payloadLen + 6)
                        + ")");
            }
        }
    }

}
