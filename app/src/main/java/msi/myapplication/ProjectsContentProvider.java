package msi.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ProjectsContentProvider extends ContentProvider {
    public ProjectsContentProvider() {
    }

    SQLiteDatabase db;

    static final String PROVIDER_NAME = "msi.projects.contentprovider";
    static final String URL = "content://" + PROVIDER_NAME + "/projects";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final UriMatcher uriMatcher;
    static final int PROJECTS = 1;
    static final int PROJECTS_ID = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "projects", PROJECTS);
        uriMatcher.addURI(PROVIDER_NAME, "projects/#", PROJECTS_ID);
    }

    @Override
    public boolean onCreate() {
        final ProjectDbHelper dbHelper = new ProjectDbHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return !db.isReadOnly();
    }

    @Override
    public String getType(Uri uri) {
        return RecordModel.RecordModelEntry.TABLE_NAME;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int deletedEntries = db.delete(getType(uri), selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedEntries;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final long id = db.insert(getType(uri), null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return db.query(getType(uri),
                projection, selection, selectionArgs, null, null, sortOrder);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final int updatedEntries = db.update(getType(uri), values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedEntries;
    }
}
