package net.sourcewalker.olv.data;

import net.sourcewalker.olv.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Provides access to the preferences of this application.
 * 
 * @author Robert &lt;xperimental@solidproject.de&gt;
 */
public class Prefs {

    private final SharedPreferences preferences;
    private final String keyDeviceAddress;

    public Prefs(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        keyDeviceAddress = context.getString(R.string.prefs_deviceaddress_key);
    }

    public String getDeviceAddress() {
        return preferences.getString("device.address", null);
    }

    public void setDeviceAddress(String address) {
        Editor editor = preferences.edit();
        editor.putString(keyDeviceAddress, address);
        editor.commit();
    }
}
