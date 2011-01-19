package net.sourcewalker.olv.messages;

public class EncodingException extends RuntimeException {

    private static final long serialVersionUID = -7148079841490219145L;

    public EncodingException(Exception cause) {
        super("Error while encoding message: " + cause.getMessage(), cause);
    }

}
