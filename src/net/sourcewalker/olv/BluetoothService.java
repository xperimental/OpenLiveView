package net.sourcewalker.olv;

import java.io.IOException;
import java.util.UUID;

import net.sourcewalker.olv.messages.DecodeException;
import net.sourcewalker.olv.messages.LiveViewResponse;
import net.sourcewalker.olv.messages.MessageConstants;
import net.sourcewalker.olv.messages.MessageDecoder;
import net.sourcewalker.olv.messages.impl.CapsRequest;
import net.sourcewalker.olv.messages.impl.CapsResponse;
import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

public class BluetoothService extends IntentService {

    private static final String TAG = "BluetoothService";

    private static final UUID SERIAL = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothService() {
        super("BluetoothServiceThread");
    }

    /*
     * (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent i) {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter.isEnabled()) {
            Log.d(TAG, "BT enabled");
            BluetoothServerSocket serverSocket = null;
            try {
                Log.d(TAG, "Starting server...");
                serverSocket = btAdapter.listenUsingRfcommWithServiceRecord(
                        "LiveView", SERIAL);
            } catch (IOException e) {
                Log.e(TAG, "Error starting BT server: " + e.getMessage());
                return;
            }
            do {
                try {
                    Log.d(TAG, "Listening for LV...");
                    BluetoothSocket socket = serverSocket.accept();
                    Log.d(TAG, "LV connected.");
                    byte[] request = new CapsRequest().getEncoded();
                    socket.getOutputStream().write(request);
                    Log.d(TAG, "Message sent.");
                    byte[] buffer = new byte[4096];
                    int read;
                    do {
                        read = socket.getInputStream().read(buffer);
                        Log.d(TAG, "Received: " + read);
                        if (read != -1) {
                            try {
                                LiveViewResponse response = MessageDecoder
                                        .decode(buffer, read);
                                if (response.getId() == MessageConstants.MSG_GETCAPS_RESP) {
                                    CapsResponse caps = (CapsResponse) response;
                                    Log.d(TAG,
                                            "LV capabilities: "
                                                    + caps.toString());
                                }
                            } catch (DecodeException e) {
                                Log.e(TAG,
                                        "Error decoding message: "
                                                + e.getMessage());
                            }
                        }
                    } while (read != -1);
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error communicating with LV: " + e.getMessage());
                }
            } while (true);
        }
    }
}
