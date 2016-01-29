package msi.myapplication;


import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

class ViewWrapper {
    View base;
    RatingBar rate = null;
    TextView project = null;
    ImageView logo = null;

    ViewWrapper(View base) {
        this.base = base;
    }

    RatingBar getRatingBar() {
        if (rate == null) {
            rate = (RatingBar) base.findViewById(R.id.rate1);
        }

        return (rate);
    }

    TextView getProject() {
        if (project == null) {
            project = (TextView) base.findViewById(R.id.projectName);
        }

        return (project);
    }

    ImageView getLogo() {
        if (logo == null) {
            logo = (ImageView) base.findViewById(R.id.logo);
        }

        return (logo);
    }
}