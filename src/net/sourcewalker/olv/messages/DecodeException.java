package net.sourcewalker.olv.messages;

public class DecodeException extends Exception {

    private static final long serialVersionUID = 1102616192132639136L;

    public DecodeException() {
        super();
    }

    public DecodeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DecodeException(String detailMessage) {
        super(detailMessage);
    }

    public DecodeException(Throwable throwable) {
        super(throwable);
    }

}
