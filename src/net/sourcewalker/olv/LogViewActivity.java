package net.sourcewalker.olv;

import net.sourcewalker.olv.data.LiveViewData;
import android.app.ListActivity;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

/**
 * Displays the log from the database to the user.
 * 
 * @author Xperimental
 */
public class LogViewActivity extends ListActivity {

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        Cursor cursor = managedQuery(LiveViewData.Log.CONTENT_URI, null, null,
                null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item, cursor, new String[] {
                        LiveViewData.Log.DATETIME, LiveViewData.Log.MESSAGE },
                new int[] { android.R.id.text1, android.R.id.text2 });
        setListAdapter(adapter);
    }

}
