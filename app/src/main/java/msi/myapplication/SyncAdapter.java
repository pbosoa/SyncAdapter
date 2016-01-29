package msi.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSI! on 08.10.2015.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String AUTHORITY = "msi.projects.contentprovider";
    private static final String PREFIX = "content://" + AUTHORITY + "/";
    private final AccountManager mAccountManager;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {
        StringBuilder sb = new StringBuilder();
        if (extras != null) {
            for (String key : extras.keySet()) {
                sb.append(key + "[" + extras.get(key) + "] ");
            }
        }

        try {
            String authToken = mAccountManager.blockingGetAuthToken(account,
                    AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
            String userObjectId = mAccountManager.getUserData(account,
                    AccountGeneral.USERDATA_USER_OBJ_ID);

            ParseServerData parseComService = new ParseServerData();

            List<Project> remoteProjects = parseComService.getProject(authToken);

            ArrayList<Project> localProjects = new ArrayList<Project>();
            Cursor cursorProjects = provider.query(
                    ProjectsContentProvider.CONTENT_URI,
                    null, null, null, null);
            if (cursorProjects != null) {
                while (cursorProjects.moveToNext()) {
                    localProjects.add(Project.fromCursor(cursorProjects));
                }
                cursorProjects.close();
            }

            ArrayList<Project> projectsToRemote = new ArrayList<Project>();
            for (Project localProject : localProjects) {
                if (!remoteProjects.contains(localProject))
                    projectsToRemote.add(localProject);
            }

            // See what Remote projects are missing on Local
            ArrayList<Project> projectsToLocal = new ArrayList<Project>();
            for (Project remoteTvproject : remoteProjects) {
                if (!localProjects.contains(remoteTvproject)
                        //&& remoteTvproject.year != 1
                        ) // TODO REMOVE THIS
                    projectsToLocal.add(remoteTvproject);
            }

            if (projectsToRemote.size() != 0) {
                // Updating remote ProjectContract.ProjectEntrys
                for (Project remoteProject : projectsToRemote) {
                    parseComService.putProject(authToken, userObjectId, remoteProject);
                }
            }

            if (projectsToLocal.size() != 0) {
                // Updating local ProjectContract.ProjectEntrys
                int i = 0;
                ContentValues projectsToLocalValues[] = new ContentValues[projectsToLocal.size()];
                for (Project localTvproject : projectsToLocal) {
                   projectsToLocalValues[i++] = localTvproject.getContentValues();
                }
                provider.bulkInsert(ProjectsContentProvider.CONTENT_URI,
                        projectsToLocalValues);
            }
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            syncResult.stats.numIoExceptions++;
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            syncResult.stats.numAuthExceptions++;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}