package net.sourcewalker.olv.service;

import net.sourcewalker.olv.data.Prefs;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This receiver listens for system broadcasts related to the bluetooth device
 * and controls the LiveView service. It is also used for starting the service
 * on boot (if bluetooth is available).
 * 
 * @author Robert &lt;xperimental@solidproject.de&gt;
 */
public class BTReceiver extends BroadcastReceiver {

    private static final String TAG = "BTReceiver";

    /*
     * (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Prefs prefs = new Prefs(context);
        String address = prefs.getDeviceAddress();
        if (address == null) {
            Log.w(TAG, "No device configured!");
        } else {
            Log.d(TAG, "Device address: " + address);
            String action = intent.getAction();
            if (intent.getExtras() != null) {
                BluetoothDevice device = (BluetoothDevice) intent.getExtras()
                        .get(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "Received broadcast: " + action);
                if (device != null && address.equals(device.getAddress())) {
                    if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                        Log.d(TAG, "Connected -> Start service.");
                        sendIntent(context, true);
                    }
                    if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED
                            .equals(action)
                            || BluetoothDevice.ACTION_ACL_DISCONNECTED
                                    .equals(action)) {
                        Log.d(TAG, "Disconnected -> Stop service.");
                        sendIntent(context, false);
                    }
                }
            }
        }
    }

    private void sendIntent(Context context, boolean newState) {
        String action = newState ? LiveViewService.ACTION_START
                : LiveViewService.ACTION_STOP;
        Intent intent = new Intent(context, LiveViewService.class);
        intent.setAction(action);
        context.startService(intent);
    }

}
