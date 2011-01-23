package net.sourcewalker.olv.messages.request;

import net.sourcewalker.olv.messages.LiveViewRequest;
import net.sourcewalker.olv.messages.MessageConstants;

/**
 * @author Xperimental
 */
public class NavigationResponse extends LiveViewRequest {

    private byte response;

    public NavigationResponse(byte response) {
        super(MessageConstants.MSG_NAVIGATION_RESP);
        this.response = response;
    }

    /*
     * (non-Javadoc)
     * @see net.sourcewalker.olv.messages.LiveViewRequest#getPayload()
     */
    @Override
    protected byte[] getPayload() {
        return new byte[] { response };
    }

}
