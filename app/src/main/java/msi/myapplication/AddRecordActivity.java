package msi.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class AddRecordActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        if(getIntent().hasExtra("projectName"))
        {
            ((EditText)findViewById(R.id.projectNameEditText)).setText(
                getIntent().getExtras().getString("projectName"));
            ((EditText)findViewById(R.id.addStudentName)).setText(
                    getIntent().getExtras().getString("student"));
            ((EditText)findViewById(R.id.addDescription)).setText(
                    getIntent().getExtras().getString("description"));
            ((EditText)findViewById(R.id.addLink)).setText(
                    getIntent().getExtras().getString("link"));
            ((EditText)findViewById(R.id.addLang)).setText(
                    getIntent().getExtras().getString("lang"));
        }
    }

    public void sendBack(View view) {
        ContentValues values = new ContentValues();

        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_TITLE,
                ((EditText)findViewById(R.id.projectNameEditText)).getText().toString());
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_STUDENT,
                ((EditText)findViewById(R.id.addStudentName)).getText().toString());
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_DESCRIPTION,
                ((EditText)findViewById(R.id.addDescription)).getText().toString());
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_LINK_TO_WEBSITE,
                ((EditText)findViewById(R.id.addLink)).getText().toString());
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_PROGRAMMING_LANGUAGE,
                ((EditText)findViewById(R.id.addLang)).getText().toString());


        if(!getIntent().hasExtra("projectName")) {
            getContentResolver().insert(
                    ProjectsContentProvider.CONTENT_URI, values);
        }
        else
        {
            long itemPosition = getIntent().getExtras().getLong("position");
            getContentResolver().update(
                    ProjectsContentProvider.CONTENT_URI,
                    values, RecordModel.RecordModelEntry._ID + "=?",
                    new String[]{String.valueOf(itemPosition)});
        }

        Intent myIntent = new Intent();
        setResult(Activity.RESULT_OK, myIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
