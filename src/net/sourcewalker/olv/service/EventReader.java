package net.sourcewalker.olv.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.sourcewalker.olv.messages.DecodeException;
import net.sourcewalker.olv.messages.LiveViewEvent;
import net.sourcewalker.olv.messages.MessageDecoder;

/**
 * This reader reads an input stream and extracts LiveViewMessages from it.
 * 
 * @author Robert &lt;xperimental@solidproject.de&gt;
 */
public class EventReader {

    private final InputStream stream;

    /**
     * Construct a MessageReader using the {@link InputStream} provided.
     * 
     * @param inputStream
     *            Stream to read messages from.
     */
    public EventReader(InputStream inputStream) {
        super();
        this.stream = inputStream;
    }

    /**
     * Closes the underlying input stream.
     */
    public void close() throws IOException {
        stream.close();
    }

    /**
     * Try to read a single {@link LiveViewEvent} from the stream. If there is
     * more than one event available on the stream then subsequent calls to this
     * method will return them.
     * 
     * @return Event read from the stream.
     * @throws IOException
     *             If there is an error reading from the underlying stream.
     * @throws DecodeException
     *             If an invalid message was read.
     */
    public LiveViewEvent readMessage() throws IOException, DecodeException {
        ByteArrayOutputStream msgStream = new ByteArrayOutputStream();
        int needRead = 1;
        ReaderState state = ReaderState.ID;
        do {
            byte read = (byte) stream.read();
            if (read == -1) {
                throw new DecodeException("Invalid message received (length="
                        + msgStream.size() + ")");
            }
            needRead--;
            msgStream.write(read);
            if (needRead == 0) {
                switch (state) {
                case ID:
                    state = ReaderState.HEADER_LEN;
                    needRead = 1;
                    break;
                case HEADER_LEN:
                    state = ReaderState.HEADER;
                    needRead = read;
                    break;
                case HEADER:
                    int payloadSize = getLastInt(msgStream);
                    state = ReaderState.PAYLOAD;
                    needRead = payloadSize;
                    break;
                }
            }
        } while (needRead > 0);
        byte[] msgArray = msgStream.toByteArray();
        return MessageDecoder.decode(msgArray, msgArray.length);
    }

    /**
     * Get last integer written to stream.
     * 
     * @param stream
     *            Stream to read from.
     * @return Last integer written to stream.
     */
    private int getLastInt(ByteArrayOutputStream stream) {
        byte[] array = stream.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(array, array.length - 4, 4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getInt();
    }

    /**
     * Enumeration containing the possible internal status of the reader.
     */
    private enum ReaderState {
        ID, HEADER_LEN, HEADER, PAYLOAD
    }

}
