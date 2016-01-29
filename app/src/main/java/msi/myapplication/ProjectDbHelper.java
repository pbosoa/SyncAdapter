package msi.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProjectDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "StudentProjects.db";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RecordModel.RecordModelEntry.TABLE_NAME + " (" +
                    RecordModel.RecordModelEntry._ID + " INTEGER PRIMARY KEY," +
                    RecordModel.RecordModelEntry.COLUMN_NAME_TITLE + " TEXT," +
                    RecordModel.RecordModelEntry.COLUMN_NAME_STUDENT + " TEXT," +
                    RecordModel.RecordModelEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    RecordModel.RecordModelEntry.COLUMN_NAME_RATING + " INTEGER," +
                    RecordModel.RecordModelEntry.COLUMN_NAME_LINK_TO_WEBSITE + " TEXT," +
                    RecordModel.RecordModelEntry.COLUMN_NAME_PROGRAMMING_LANGUAGE + " TEXT," +
                    RecordModel.RecordModelEntry.COLUMN_NAME_UPDATED_AT + " TEXT," +
                    RecordModel.RecordModelEntry.COLUMN_NAME_CREATED_AT + " TEXT," +
                    RecordModel.RecordModelEntry.COLUMN_NAME_OBJECT_ID + " TEXT" +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RecordModel.RecordModelEntry.TABLE_NAME;

    public ProjectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
