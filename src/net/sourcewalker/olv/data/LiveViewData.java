package net.sourcewalker.olv.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class contains constants definiting the database schema and the content
 * provider.
 * 
 * @author Xperimental
 */
public final class LiveViewData {

    public static final String AUTHORITY = "net.sourcewalker.olv";

    /**
     * The "log" database contains a persistent log for the application.
     * 
     * @author Xperimental
     */
    public static final class Log implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/log");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/net.sourcewalker.olv.log";

        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/net.sourcewalker.olv.log";

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

}
