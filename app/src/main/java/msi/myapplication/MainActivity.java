package msi.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity implements
        android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private RecordModel[] values = new RecordModel[3];
    private ListView listview;
    private static CustomCursorAdapter adapter;
    private Cursor c;
    private Context context;
    //private String authToken = null;

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.auth";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    private Account mAccount;

    private AccountManager mAccountManager;
    private Account mConnectedAccount;

    public static final String TokensPreferences = "TokenPrefs" ;
    //public static SharedPreferences prefs;
    public static String userName;
    public static String authToken = null;
    public static String userObjectId;

    SyncStatusObserver syncObserver = new SyncStatusObserver() {
        @Override
        public void onStatusChanged(final int which) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshSyncStatus();
                }
            });
        }
    };

    private void refreshSyncStatus() {
        String status;

        int a = 1 + 5;
    }

    Object handleSyncObserver;

    @Override
    protected void onResume() {
        super.onResume();
        handleSyncObserver = ContentResolver.addStatusChangeListener(ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE |
                ContentResolver.SYNC_OBSERVER_TYPE_PENDING, syncObserver);
    }

    @Override
    protected void onPause() {
        if (handleSyncObserver != null)
            ContentResolver.removeStatusChangeListener(handleSyncObserver);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProjectDbHelper mDbHelper = new ProjectDbHelper(this);
        //mAccountManager = AccountManager.get(this);

        //mAccount = CreateSyncAccount(this);
        mAccount = new Account("dummyaccount", "com.auth");

     /*   final String authToken = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("token_" + userName,"");
        final String userObjectId = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("userObjectId_"
                + userName, "");*/


        //Unfortunately dummy account doesn't work
      /*  Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true); // Performing a sync no matter if it's off
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // Performing a sync no matter if it's off

        getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE,
                AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
        ContentResolver.requestSync(mAccount,
                AUTHORITY, bundle);*/

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + RecordModel.RecordModelEntry.TABLE_NAME);

        if (!doesTableExist(db, RecordModel.RecordModelEntry.TABLE_NAME)) {
            db.execSQL(ProjectDbHelper.SQL_CREATE_ENTRIES);
        }

        ParseServerData serverAccessor = new ParseServerData();

        List<Project> projectList = null;
        try {
            projectList = serverAccessor.getProject("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Project> localProjects = new ArrayList<Project>();
        Cursor cursorProjects = getContentResolver().query(
                ProjectsContentProvider.CONTENT_URI,
                null, null, null, null);
        if (cursorProjects != null) {
            while (cursorProjects.moveToNext()) {
                localProjects.add(Project.fromCursor(cursorProjects));
            }
            cursorProjects.close();
        }

        for(Project project : projectList) {
            if (!localProjects.contains(project))
                getContentResolver().insert(
                        ProjectsContentProvider.CONTENT_URI,
                        project.getContentValues());
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(100);

                    android.os.Debug.waitForDebugger();

                    ParseServerData parseComService = new ParseServerData();

                    List<Project> remoteProjects =
                            parseComService.getProject(authToken);

                    ArrayList<Project> localProjects = new ArrayList<Project>();
                    Cursor cursorProjects = getContentResolver().query(
                            ProjectsContentProvider.CONTENT_URI,
                            null, null, null, null);
                    if (cursorProjects != null) {
                        while (cursorProjects.moveToNext()) {
                            localProjects.add(Project.fromCursor(cursorProjects));
                        }
                        cursorProjects.close();
                    }

                    ArrayList<Project> projectsToRemote =
                            new ArrayList<Project>();
                    for (Project localProject : localProjects) {
                        if (!remoteProjects.contains(localProject))
                            projectsToRemote.add(localProject);
                    }

                    ArrayList<Project> projectsToLocal = new ArrayList<Project>();
                    for (Project remoteProject: remoteProjects) {
                        if (!localProjects.contains(remoteProject)
                            //&& remoteTvShow.year != 1
                                ) // TODO REMOVE THIS
                            projectsToLocal.add(remoteProject);
                    }

                    if (projectsToRemote.size() != 0) {
                        // Updating remote ProjectContract.ProjectEntrys
                        for (Project remoteProject : projectsToRemote) {
                            parseComService.putProject(authToken, userObjectId,
                                    remoteProject);
                        }
                    }
/*
                    if (showsToLocal.size() != 0) {
                        // Updating local ProjectContract.ProjectEntrys
                        int i = 0;
                        ContentValues showsToLocalValues[] =
                                new ContentValues[showsToLocal.size()];
                        for (Project localTvShow : showsToLocal) {
                            showsToLocalValues[i++] = localTvShow.getContentValues();
                        }
                        provider.bulkInsert(ProjectsContentProvider.CONTENT_URI,
                                showsToLocalValues);
                    }*/

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewactivity);
        context = this;

        listview = (ListView) findViewById(R.id.listview);

        useDatabaseToDisplayProjects();

        Button addNewRecordButton = (Button) findViewById(R.id.addButton);
        addNewRecordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AddRecordActivity.class);
                startActivityForResult(myIntent, 1);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */

        try {
            accountManager.addAccountExplicitly(newAccount, null, null);
        } catch (Exception e) {

        }

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            return null;
        }
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                //adapter.notifyDataSetChanged();
            }
        }
    }*/

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void useDatabaseToDisplayProjects() {
        Uri students = Uri.parse(ProjectsContentProvider.URL);
        c = managedQuery(students, null, null, null,
                null);

        adapter = new CustomCursorAdapter(
                this,
                c,
                0);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(context, ProjectInfoActivity.class);
                ViewWrapper wrapper = new ViewWrapper(view);
                view.setTag(wrapper);
                RatingBar rate = wrapper.getRatingBar();

                myIntent.putExtra("position", id);
                ((Activity) context).startActivity(myIntent);//.startActivityForResult(myIntent, 1);
            }
        });

        listview.setAdapter(adapter);
    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RecordModel.RecordModelEntry._ID,
                RecordModel.RecordModelEntry.COLUMN_NAME_TITLE,
                RecordModel.RecordModelEntry.COLUMN_NAME_RATING
        };
        android.content.CursorLoader cursorLoader = new android.content.CursorLoader(this,
                ProjectsContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        cursor.setNotificationUri(getContentResolver(), ProjectsContentProvider.CONTENT_URI);
        cursor.registerContentObserver(new ContentObserver(new Handler()) {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onChange(boolean selfChange) {
                getLoaderManager().restartLoader(0, null, MainActivity.this);
            }
        });
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    /**
     * Get an auth token for the account.
     * If not exist - add it and then return its auth token.
     * If one exist - return its auth token.
     * If more than one exists - show a picker and return the select account's auth token.
     *
     * @param accountType
     * @param authTokenType
     */
    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {
                            android.os.Debug.waitForDebugger();
                            bnd = future.getResult();
                            authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            if (authToken != null) {
                                String accountName = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
                                mConnectedAccount = new Account(accountName, AccountGeneral.ACCOUNT_TYPE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                , null);
    }
}