package net.sourcewalker.olv;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

        SQLiteDatabase db = new LiveViewDbHelper(this).getReadableDatabase();
        Cursor cursor = db.query(LiveViewDbHelper.Log.TABLE,
                LiveViewDbHelper.Log.DEFAULT_PROJECTION, null, null, null,
                null, LiveViewDbHelper.Log.DEFAULT_ORDER);
        startManagingCursor(cursor);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item, cursor, new String[] {
                        LiveViewDbHelper.Log.DATETIME,
                        LiveViewDbHelper.Log.MESSAGE }, new int[] {
                        android.R.id.text1, android.R.id.text2 });
        setListAdapter(adapter);
    }

}
