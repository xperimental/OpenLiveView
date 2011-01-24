package net.sourcewalker.olv;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class LiveViewPreferences extends PreferenceActivity {

    private BluetoothAdapter btAdapter;
    private ListPreference devicePreference;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        addPreferencesFromResource(R.xml.preferences);

        devicePreference = (ListPreference) findPreference(getString(R.string.prefs_deviceaddress_key));
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        fillDevices();
    }

    /**
     * Get the list of paired devices from the system and fill the preference
     * list.
     */
    private void fillDevices() {
        Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
        List<String> names = new ArrayList<String>();
        List<String> addresses = new ArrayList<String>();
        for (BluetoothDevice dev : devices) {
            names.add(dev.getName());
            addresses.add(dev.getAddress());
        }
        devicePreference.setEntries(names.toArray(new String[0]));
        devicePreference.setEntryValues(addresses.toArray(new String[0]));
    }
}
