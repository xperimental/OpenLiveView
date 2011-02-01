package net.sourcewalker.olv;

import net.sourcewalker.olv.data.LiveViewData;
import android.app.ListActivity;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
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

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_menu, menu);
        return true;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_clearlog:
            clearLog();
            break;
        default:
            throw new IllegalArgumentException("Unknown menu item: "
                    + item.getItemId());
        }
        return true;
    }

    /**
     * Clears the current contents of the application log.
     */
    private void clearLog() {
        getContentResolver().delete(LiveViewData.Log.CONTENT_URI, null, null);
    }

}
