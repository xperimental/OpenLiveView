package net.sourcewalker.olv;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

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
        db.execSQL(Log.SCHEMA);
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
            db.execSQL("DROP TABLE " + Log.TABLE);
            onCreate(db);
        }
    }

    public static final class Log implements BaseColumns {

        public static final String TABLE = "log";

        public static final String TIMESTAMP = "time";

        public static final String MESSAGE = "message";

        public static final String DATETIME = "datetime";

        public static final String SCHEMA = "CREATE TABLE " + TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY, " + TIMESTAMP + " INTEGER, "
                + MESSAGE + " TEXT)";

        public static final String[] DEFAULT_PROJECTION = new String[] {
                _ID,
                TIMESTAMP,
                "DATETIME(" + TIMESTAMP
                        + " / 1000, 'unixepoch', 'localtime') AS " + DATETIME,
                MESSAGE };

        public static final String DEFAULT_ORDER = TIMESTAMP + " DESC, " + _ID
                + " ASC";

    }

    public static void logMessage(Context context, String message) {
        LiveViewDbHelper helper = new LiveViewDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Log.TIMESTAMP, System.currentTimeMillis());
        values.put(Log.MESSAGE, message);
        db.insert(Log.TABLE, Log._ID, values);
        db.close();
    }

}
