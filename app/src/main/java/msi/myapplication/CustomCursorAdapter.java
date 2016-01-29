package msi.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

/**
 * Created by MSI! on 04.10.2015.
 */
public class CustomCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    ImageView imageView;

    // Default constructor
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTitle = (TextView) view.findViewById(R.id.projectName);
        String title = cursor.getString( cursor.getColumnIndex(
                RecordModel.RecordModelEntry.COLUMN_NAME_TITLE ) );
        textViewTitle.setText(title);

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rate1);
        String value = cursor.getString( cursor.getColumnIndex(
                RecordModel.RecordModelEntry.COLUMN_NAME_RATING ) );

        if(value != null) {
            float f = Float.parseFloat(value);
            ratingBar.setRating(f);
        }

       /* imageView = (ImageView) view.findViewById(R.id.logo);
        String URL = "https://tancap.in/wallpaper/30/10/awesome-wallpaper-background-hd.jpg";

        imageView.setTag(URL);
        new DownloadImageTask().execute((Runnable) imageView);*/
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.activity_record, parent, false);
    }

    public Bitmap loadImageFromNetwork(String imgUrl) throws IOException {
        URL url;
        url = new URL(imgUrl);
        Bitmap img = BitmapFactory.decodeStream(url.openStream());

        return img;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        /**	The	system	calls	this	to	perform	work	in	a	worker	thread	and
         *	delivers	it	the	parameters	given	to	AsyncTask.execute()	*/
        protected Bitmap doInBackground(String... urls) {
            try {
                return loadImageFromNetwork(urls[0]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**	The	system	calls	this	to	perform	work	in	the	UI	thread	and	delivers
         *	the	result	from	doInBackground()	*/
        protected void onPostExecute(Bitmap	result) {
            imageView.setImageBitmap(result);
        }
    }
}