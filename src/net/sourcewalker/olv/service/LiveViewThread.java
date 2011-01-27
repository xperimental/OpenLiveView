package net.sourcewalker.olv.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import net.sourcewalker.olv.LiveViewPreferences;
import net.sourcewalker.olv.R;
import net.sourcewalker.olv.messages.DecodeException;
import net.sourcewalker.olv.messages.LiveViewCall;
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
import net.sourcewalker.olv.messages.calls.SetVibrate;
import net.sourcewalker.olv.messages.events.CapsResponse;
import net.sourcewalker.olv.messages.events.Navigation;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Robert &lt;xperimental@solidproject.de&gt;
 */
public class LiveViewThread extends Thread {

    private static final String TAG = "LiveViewThread";

    private static final UUID SERIAL = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final int SERVICE_NOTIFY = 100;

    private final byte[] menuImage;

    private final BluetoothAdapter btAdapter;

    private BluetoothServerSocket serverSocket;

    private long startUpTime;

    private LiveViewService parentService;

    private BluetoothSocket clientSocket;

    private Notification notification;

    public LiveViewThread(LiveViewService parentService) {
        super("LiveViewThread");
        this.parentService = parentService;

        notification = new Notification(R.drawable.icon,
                "LiveView connected...", System.currentTimeMillis());
        Context context = parentService.getApplicationContext();
        CharSequence contentTitle = parentService.getString(R.string.app_name);
        CharSequence contentText = parentService
                .getString(R.string.notify_service_running);
        Intent notificationIntent = new Intent(parentService,
                LiveViewPreferences.class);
        PendingIntent contentIntent = PendingIntent.getActivity(parentService,
                0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentIntent);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            InputStream stream = parentService.getAssets().open(
                    "menu_blank.png");
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
            throw new RuntimeException("Error reading menu icon: "
                    + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        parentService.startForeground(SERVICE_NOTIFY, notification);

        Log.d(TAG, "Starting LiveView thread.");
        startUpTime = System.currentTimeMillis();
        serverSocket = null;
        try {
            Log.d(TAG, "Starting server...");
            serverSocket = btAdapter.listenUsingRfcommWithServiceRecord(
                    "LiveView", SERIAL);
        } catch (IOException e) {
            Log.e(TAG, "Error starting BT server: " + e.getMessage());
            return;
        }
        try {
            Log.d(TAG, "Listening for LV...");
            clientSocket = serverSocket.accept();
            // Single connect only
            serverSocket.close();
            serverSocket = null;
            Log.d(TAG, "LV connected.");
            sendCall(new CapsRequest());
            Log.d(TAG, "Message sent.");
            byte[] buffer = new byte[4096];
            int read;
            do {
                read = clientSocket.getInputStream().read(buffer);
                Log.d(TAG, "Received " + read + " bytes.");
                if (read != -1) {
                    try {
                        LiveViewEvent response = MessageDecoder.decode(buffer,
                                read);
                        sendCall(new MessageAck(response.getId()));
                        Log.d(TAG, "Got message: " + response);
                        processEvent(response);
                    } catch (DecodeException e) {
                        Log.e(TAG, "Error decoding message: " + e.getMessage());
                    }
                }
            } while (read != -1);
            clientSocket.close();
        } catch (IOException e) {
            String msg = e.getMessage();
            if (!msg.contains("Connection timed out")) {
                Log.e(TAG, "Error communicating with LV: " + e.getMessage());
            }
        }
        Log.d(TAG, "Stopped LiveView thread.");

        // Log runtime
        long runtime = (System.currentTimeMillis() - startUpTime) / 1000;
        long runHour = runtime / 3600;
        runtime -= runHour * 3600;
        long runMinute = runtime / 60;
        runtime -= runMinute * 60;
        Log.d(TAG, String.format(
                "Service runtime: %d hours %d minutes %d seconds", runHour,
                runMinute, runtime));

        // Stop surrounding service
        ((NotificationManager) parentService
                .getSystemService(Context.NOTIFICATION_SERVICE))
                .cancel(SERVICE_NOTIFY);
        parentService.stopForeground(true);
        parentService.stopSelf();
    }

    /**
     * Process a message that was sent by the LiveView device.
     * 
     * @param event
     *            Event sent by device.
     * @throws IOException
     */
    private void processEvent(LiveViewEvent event) throws IOException {
        switch (event.getId()) {
        case MessageConstants.MSG_GETCAPS_RESP:
            CapsResponse caps = (CapsResponse) event;
            Log.d(TAG, "LV capabilities: " + caps.toString());
            Log.d(TAG, "LV Version: " + caps.getSoftwareVersion());
            sendCall(new SetMenuSize((byte) 1));
            sendCall(new SetVibrate(0, 50));
            break;
        case MessageConstants.MSG_GETTIME:
            Log.d(TAG, "Sending current time...");
            sendCall(new GetTimeResponse());
            break;
        case MessageConstants.MSG_DEVICESTATUS:
            Log.d(TAG, "Acknowledging status.");
            sendCall(new DeviceStatusAck());
            break;
        case MessageConstants.MSG_GETMENUITEMS:
            Log.d(TAG, "Sending menu items...");
            sendCall(new MenuItem((byte) 0, false, new UShort((short) 0),
                    "Test", menuImage));
            break;
        case MessageConstants.MSG_NAVIGATION:
            Navigation nav = (Navigation) event;
            if (nav.getNavAction() == MessageConstants.NAVACTION_PRESS
                    && nav.getNavType() == MessageConstants.NAVTYPE_MENUSELECT) {
                sendCall(new NavigationResponse(MessageConstants.RESULT_OK));
            } else {
                Log.d(TAG, "Bringing back to menu.");
                sendCall(new NavigationResponse(MessageConstants.RESULT_CANCEL));
            }
        }
    }

    /**
     * Send a message to the LiveView device if one is connected.
     * 
     * @param call
     *            {@link LiveViewCall} to send to device.
     * @throws IOException
     *             If the message could not be sent successfully.
     */
    private void sendCall(LiveViewCall call) throws IOException {
        if (clientSocket == null) {
            throw new IOException("No client connected!");
        } else {
            clientSocket.getOutputStream().write(call.getEncoded());
        }
    }

    public void stopLoop() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG,
                        "Error while closing server socket: " + e.getMessage());
            }
        }
    }

    public boolean isLooping() {
        return serverSocket != null;
    }

}
