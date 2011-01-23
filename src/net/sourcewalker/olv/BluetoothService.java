package net.sourcewalker.olv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import net.sourcewalker.olv.messages.DecodeException;
import net.sourcewalker.olv.messages.LiveViewEvent;
import net.sourcewalker.olv.messages.MessageConstants;
import net.sourcewalker.olv.messages.MessageDecoder;
import net.sourcewalker.olv.messages.UShort;
import net.sourcewalker.olv.messages.calls.CapsRequest;
import net.sourcewalker.olv.messages.calls.DeviceStatusAck;
import net.sourcewalker.olv.messages.calls.GetTimeResponse;
import net.sourcewalker.olv.messages.calls.MenuItem;
import net.sourcewalker.olv.messages.calls.MessageAck;
import net.sourcewalker.olv.messages.calls.NavigationResponse;
import net.sourcewalker.olv.messages.calls.SetMenuSize;
import net.sourcewalker.olv.messages.events.CapsResponse;
import net.sourcewalker.olv.messages.events.Navigation;
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

    private byte[] menuImage;

    public BluetoothService() {
        super("BluetoothServiceThread");
    }

    /*
     * (non-Javadoc)
     * @see android.app.IntentService#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            InputStream stream = getAssets().open("menu_blank.png");
            ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (stream.available() > 0) {
                int read = stream.read(buffer);
                arrayStream.write(buffer, 0, read);
            }
            stream.close();
            menuImage = arrayStream.toByteArray();
            Log.d(TAG, "Menu icon size: " + menuImage.length);
        } catch (IOException e) {
            Log.e(TAG, "Error reading menu icon: " + e.getMessage());
        }
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
                        Log.d(TAG, "Received " + read + " bytes.");
                        if (read != -1) {
                            try {
                                LiveViewEvent response = MessageDecoder.decode(
                                        buffer, read);
                                socket.getOutputStream().write(
                                        new MessageAck(response.getId())
                                                .getEncoded());
                                Log.d(TAG, "Got message: " + response);
                                switch (response.getId()) {
                                case MessageConstants.MSG_GETCAPS_RESP:
                                    CapsResponse caps = (CapsResponse) response;
                                    Log.d(TAG,
                                            "LV capabilities: "
                                                    + caps.toString());
                                    socket.getOutputStream().write(
                                            new SetMenuSize((byte) 1)
                                                    .getEncoded());
                                    break;
                                case MessageConstants.MSG_GETTIME:
                                    Log.d(TAG, "Sending current time...");
                                    socket.getOutputStream().write(
                                            new GetTimeResponse().getEncoded());
                                    break;
                                case MessageConstants.MSG_DEVICESTATUS:
                                    Log.d(TAG, "Acknowledging status.");
                                    socket.getOutputStream().write(
                                            new DeviceStatusAck().getEncoded());
                                    break;
                                case MessageConstants.MSG_GETMENUITEMS:
                                    Log.d(TAG, "Sending menu items...");
                                    socket.getOutputStream().write(
                                            new MenuItem((byte) 0, false,
                                                    new UShort((short) 0),
                                                    "Test", menuImage)
                                                    .getEncoded());
                                    break;
                                case MessageConstants.MSG_NAVIGATION:
                                    Navigation nav = (Navigation) response;
                                    if (nav.getNavAction() == MessageConstants.NAVACTION_PRESS
                                            && nav.getNavType() == MessageConstants.NAVTYPE_MENUSELECT) {
                                        socket.getOutputStream()
                                                .write(new NavigationResponse(
                                                        MessageConstants.RESULT_OK)
                                                        .getEncoded());
                                    } else {
                                        Log.d(TAG, "Bringing back to menu.");
                                        socket.getOutputStream()
                                                .write(new NavigationResponse(
                                                        MessageConstants.RESULT_CANCEL)
                                                        .getEncoded());
                                    }
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
