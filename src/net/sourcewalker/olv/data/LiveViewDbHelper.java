package net.sourcewalker.olv.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database currently only holds logs.
 * 
 * @author Xperimental
 */
public class LiveViewDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "liveview.db";

    private static final int DB_VERSION = 1;

    public LiveViewDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LiveViewData.Log.SCHEMA);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != DB_VERSION) {
            db.execSQL("DROP TABLE " + LiveViewData.Log.TABLE);
            onCreate(db);
        }
    }

    public static void logMessage(Context context, String message) {
        LiveViewDbHelper helper = new LiveViewDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LiveViewData.Log.TIMESTAMP, System.currentTimeMillis());
        values.put(LiveViewData.Log.MESSAGE, message);
        db.insert(LiveViewData.Log.TABLE, LiveViewData.Log._ID, values);
        db.close();
    }

}
