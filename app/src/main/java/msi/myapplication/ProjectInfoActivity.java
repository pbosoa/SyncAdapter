package msi.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


public class ProjectInfoActivity extends AppCompatActivity
{
    private Context context;
    long itemPosition;
    private Intent myIntent;
    private TextView projectNameTextView;
    private TextView studentNameTextView;
    private TextView projectDescriptionTextView;
    private TextView langTextView;
    private TextView linkTextView;
    private RatingBar ratingBar;
    private Intent shareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);

        ratingBar = (RatingBar) this.findViewById(R.id.rate2);
        projectNameTextView = (TextView) this.findViewById(R.id.project);
        studentNameTextView = (TextView) this.findViewById(R.id.student);
        projectDescriptionTextView = (TextView) this.findViewById(R.id.description);
        langTextView = (TextView) this.findViewById(R.id.programmingLanguage);
        linkTextView = (TextView) this.findViewById(R.id.websiteLink);
        ImageView logo = (ImageView) this.findViewById(R.id.logo);
        context = this;

        itemPosition = getIntent().getExtras().getLong("position");

        String[] projection = {
                RecordModel.RecordModelEntry._ID,
                RecordModel.RecordModelEntry.COLUMN_NAME_TITLE,
                RecordModel.RecordModelEntry.COLUMN_NAME_PROGRAMMING_LANGUAGE,
                RecordModel.RecordModelEntry.COLUMN_NAME_LINK_TO_WEBSITE,
                RecordModel.RecordModelEntry.COLUMN_NAME_DESCRIPTION,
                RecordModel.RecordModelEntry.COLUMN_NAME_RATING,
                RecordModel.RecordModelEntry.COLUMN_NAME_STUDENT};

        String where = "_ID=?";
        String[] args = {String.valueOf(itemPosition)};

        Cursor cursor = getContentResolver().query(ProjectsContentProvider.CONTENT_URI,
                projection, where, args, null);
        cursor.moveToFirst();

        projectNameTextView.setText(cursor.getString(1));
        langTextView.setText(cursor.getString(2));
        linkTextView.setText(cursor.getString(3));
        projectDescriptionTextView.setText(cursor.getString(4));

        String r = cursor.getString(5);
        ratingBar.setRating(Float.parseFloat(cursor.getString(5) == null ?
                "0" : cursor.getString(5)));

        studentNameTextView.setText(cursor.getString(6));

        RatingBar.OnRatingBarChangeListener l =
                new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar,
                                                float rating, boolean fromTouch) {
                        myIntent = new Intent();

                        String projectNameText = projectNameTextView
                                .getText().toString();
                        String student = studentNameTextView
                                .getText().toString();
                        String description = projectDescriptionTextView
                                .getText().toString();
                        String link = linkTextView
                                .getText().toString();
                        String lang = langTextView
                                .getText().toString();

                        ContentValues values = new ContentValues();

                        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_TITLE,
                                projectNameText);
                        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_STUDENT,
                                student);
                        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_DESCRIPTION,
                                description);
                        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_LINK_TO_WEBSITE,
                                link);
                        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_PROGRAMMING_LANGUAGE,
                                lang);
                        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_RATING,
                                ratingBar.getRating());

                        getContentResolver().update(
                                ProjectsContentProvider.CONTENT_URI,
                                values, RecordModel.RecordModelEntry._ID + "=?",
                                new String[]{String.valueOf(itemPosition)});

                        setResult(Activity.RESULT_OK, myIntent);
                    }
                };
        ratingBar.setOnRatingBarChangeListener(l);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_project_info, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_project_info, menu);

        MenuItem vkshare = menu.findItem(R.id.vk_share);
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Description of the project");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                projectDescriptionTextView.getText());

        ShareActionProvider shareActionProvider=
                (ShareActionProvider)MenuItemCompat.getActionProvider(vkshare);

        if(shareActionProvider != null)
        {
            shareActionProvider.setShareIntent(shareIntent);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                return true;
            case R.id.vk_share:
                startActivity(Intent.createChooser(shareIntent,
                        "Share project's description with..."));
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void delete(View v) {
        getContentResolver().delete(ProjectsContentProvider.CONTENT_URI,
                RecordModel.RecordModelEntry._ID + "=?",
                new String[]{String.valueOf(itemPosition)});

        Intent myIntent = new Intent();
        setResult(Activity.RESULT_OK, myIntent);
        finish();
    }

    public void update(View v) {
        ContentValues values = new ContentValues();

        String projectNameText = projectNameTextView
                .getText().toString();
        String student = studentNameTextView
                .getText().toString();
        String description = projectDescriptionTextView
                .getText().toString();
        String link = linkTextView
                .getText().toString();
        String lang = langTextView
                .getText().toString();

        Intent myIntent = new Intent(v.getContext(), AddRecordActivity.class);
        myIntent.putExtra("position", itemPosition);
        myIntent.putExtra("projectName", projectNameText);
        myIntent.putExtra("student", student);
        myIntent.putExtra("description", description);
        myIntent.putExtra("link", link);
        myIntent.putExtra("lang", lang);

        startActivityForResult(myIntent, 1);

        Intent myIntent2 = new Intent();
        setResult(Activity.RESULT_OK, myIntent2);
        finish();
    }

    public void linkToWebView(View v) {
        Intent myIntent = new Intent(v.getContext(), WebOpenLinkActivity.class);
        String link = linkTextView
                .getText().toString();
        myIntent.putExtra("link", link);
        startActivityForResult(myIntent, 1);
    }
}
