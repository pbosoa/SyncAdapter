package msi.myapplication;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static msi.myapplication.SignInActivity.ARG_ACCOUNT_TYPE;
import static msi.myapplication.SignInActivity.KEY_ERROR_MESSAGE;
import static msi.myapplication.SignInActivity.PARAM_USER_PASS;

public class SignUpActivity extends Activity {

    private String TAG = getClass().getSimpleName();
    private String mAccountType;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.alreadyMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
        mContext = this;
    }

    private void createAccount() {
        new AsyncTask<String, Void, Intent>() {

            String name = ((TextView) findViewById(R.id.name)).getText().toString().trim();
            //String accountName = ((TextView) findViewById(R.id.accountName)).getText().toString().trim();
            String accountPassword = ((TextView) findViewById(R.id.accountPassword)).getText().toString().trim();

            @Override
            protected Intent doInBackground(String... params) {
                String authtoken = null;
                Bundle data = new Bundle();
                try {
                    /*android.os.Debug.waitForDebugger();
                    authtoken = AccountGeneral.sServerAuthenticate.userSignUp
                            (name, accountPassword,
                            AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
                    String userObjectId = AccountGeneral.
                            sServerAuthenticate.getUserObjectId();

                    MainActivity.userName = name;
                    MainActivity.authToken = authtoken;
                    MainActivity.userObjectId = userObjectId;

                    data.putString("userObjectId",userObjectId);
                    data.putString("authtoken",authtoken);
                    data.putString("SUCCESS", "SUCCESS");*/

                    if(AccountGeneral.sServerAuthenticate.userSignUpWithoutToken
                            (name, accountPassword,
                                    AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS))
                    {
                        data.putString("SUCCESS", "SUCCESS");
                    }

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, name);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, accountPassword);
                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent res = new Intent(mContext, MainActivity.class);
                //startActivity(res);

                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                android.os.Debug.waitForDebugger();
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else if (intent.hasExtra("SUCCESS")) {
                    /*android.os.Debug.waitForDebugger();
                    SharedPreferences prefs = PreferenceManager.
                            getDefaultSharedPreferences(mContext);

                    String t = intent.getStringExtra("authtoken");

                    prefs.edit().putString("token_" + name,
                            intent.getStringExtra("authtoken"));
                    prefs.edit().putString("userObjectId_" + name,
                            intent.getStringExtra("userObjectId"));
                    MainActivity.userName = name;
                    prefs.edit().commit();*/

                    Toast.makeText(getBaseContext(), "Success",
                            Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(mContext, MainActivity.class);
                    startActivityForResult(myIntent, 1);
                }
                else
                {
                    SharedPreferences prefs = PreferenceManager.
                            getDefaultSharedPreferences(mContext);

                    String t = intent.getStringExtra("authtoken");

                    prefs.edit().putString("token_" + name,
                            intent.getStringExtra("authtoken"));
                    prefs.edit().putString("userObjectId_" + name,
                            intent.getStringExtra("userObjectId"));
                    MainActivity.userName = name;
                    prefs.edit().commit();
                    Toast.makeText(getBaseContext(), "ACCESS DENIED",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
