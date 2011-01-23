package net.sourcewalker.olv.messages.response;

import java.nio.ByteBuffer;

import net.sourcewalker.olv.messages.LiveViewResponse;
import net.sourcewalker.olv.messages.MessageConstants;

public class MenuItemsResponse extends LiveViewResponse {

    public MenuItemsResponse() {
        super(MessageConstants.MSG_GETMENUITEMS);
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sourcewalker.olv.messages.LiveViewResponse#readData(java.nio.ByteBuffer
     * )
     */
    @Override
    public void readData(ByteBuffer buffer) {
    }

}
