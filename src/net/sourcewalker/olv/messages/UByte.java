package net.sourcewalker.olv.messages;

public class UByte {

    private short value;

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = (short) (value & 0xFF);
    }

    public UByte(short value) {
        setValue(value);
    }

    public UByte(byte value) {
        this.value = (short) (value & 0xFF);
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
