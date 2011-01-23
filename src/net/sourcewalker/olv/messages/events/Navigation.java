package net.sourcewalker.olv.messages.events;

import java.nio.ByteBuffer;

import net.sourcewalker.olv.messages.LiveViewEvent;
import net.sourcewalker.olv.messages.MessageConstants;
import android.util.Log;

public class Navigation extends LiveViewEvent {

    private static final String TAG = "Navigation";

    private byte menuItemId;
    private byte navAction;
    private byte navType;
    private boolean inAlert;

    public byte getMenuItemId() {
        return menuItemId;
    }

    public byte getNavAction() {
        return navAction;
    }

    public byte getNavType() {
        return navType;
    }

    public boolean isInAlert() {
        return inAlert;
    }

    public Navigation() {
        super(MessageConstants.MSG_NAVIGATION);
    }

    /*
     * (non-Javadoc)
     * @see
     * net.sourcewalker.olv.messages.LiveViewResponse#readData(java.nio.ByteBuffer
     * )
     */
    @Override
    public void readData(ByteBuffer buffer) {
        if (buffer.get() != 0) {
            Log.w(TAG, "First byte not 0!");
        }
        if (buffer.get() != 3) {
            Log.w(TAG, "Second byte not 3!");
        }
        byte navigation = buffer.get();
        this.menuItemId = buffer.get();
        byte menuId = buffer.get();
        if (menuId != 10 && menuId != 20) {
            Log.w(TAG, "Unexpected menuId: " + menuId);
        }
        if (navigation != 32 && ((navigation < 1) || (navigation > 15))) {
            Log.w(TAG, "Navigation out of range: " + navigation);
        }
        this.inAlert = menuId == 20;

        if (navigation == 32) {
            this.navAction = MessageConstants.NAVACTION_PRESS;
            this.navType = MessageConstants.NAVTYPE_MENUSELECT;
        } else {
            this.navAction = (byte) ((navigation - 1) % 3);
            this.navType = (byte) ((navigation - 1) / 3);
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Navigation: Action=%d Type=%d MenuItem=%d %s",
                navAction, navType, menuItemId, inAlert ? "ALERT" : "");
    }
}
