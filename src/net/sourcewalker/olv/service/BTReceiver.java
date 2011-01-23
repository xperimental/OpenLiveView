package net.sourcewalker.olv.service;

import android.bluetooth.BluetoothAdapter;
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
        String action = intent.getAction();
        Log.d(TAG, "Received broadcast: " + action);
        if (action == null || Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                sendIntent(context, true);
            }
        }
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.STATE_OFF);
            Log.d(TAG, "New state: " + state);
            switch (state) {
            case BluetoothAdapter.STATE_OFF:
            case BluetoothAdapter.STATE_TURNING_OFF:
            case BluetoothAdapter.STATE_TURNING_ON:
                sendIntent(context, false);
                break;
            case BluetoothAdapter.STATE_ON:
                sendIntent(context, true);
                break;
            default:
                throw new IllegalArgumentException("Unknown bluetooth state: "
                        + state);
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
