package net.sourcewalker.olv;

import net.sourcewalker.olv.service.BTReceiver;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LiveViewText extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sendBroadcast(new Intent(this, BTReceiver.class));
    }

}
