package net.sourcewalker.olv.messages;

public class UByte {

    private short value;

    public byte getValue() {
        return (byte) (value & 0xFF);
    }

    public void setValue(byte value) {
        this.value = (short) (value & 0xFF);
    }

    public UByte(byte value) {
        setValue(value);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Short.toString(value);
    }

}
