package net.sourcewalker.olv.messages.calls;

import net.sourcewalker.olv.messages.LiveViewCall;
import net.sourcewalker.olv.messages.MessageConstants;

public class SetMenuSize extends LiveViewCall {

    private final byte menuSize;

    public SetMenuSize(byte menuSize) {
        super(MessageConstants.MSG_SETMENUSIZE);
        this.menuSize = menuSize;
    }

    /*
     * (non-Javadoc)
     * @see net.sourcewalker.olv.messages.LiveViewRequest#getPayload()
     */
    @Override
    protected byte[] getPayload() {
        return new byte[] { menuSize };
    }

}
