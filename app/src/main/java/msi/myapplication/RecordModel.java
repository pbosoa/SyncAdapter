package msi.myapplication;// To prevent someone from accidentally instantiating the contract class,
// give it an empty constructor.

import android.provider.BaseColumns;

public final class RecordModel {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public RecordModel() {}

    private float rating = 0.0f;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    /* Inner class that defines the table contents */
    public static abstract class RecordModelEntry implements BaseColumns {
        public static final String TABLE_NAME = "projects";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_STUDENT = "student";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_LINK_TO_WEBSITE = "linkToWebsite";
        public static final String COLUMN_NAME_PROGRAMMING_LANGUAGE = "programmingLanguage";

        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_OBJECT_ID = "objectId";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }
}

/*
package msi.myapplication;

class RecordModel {
    private String projectName;
    private String projectDescription;
    private String authorName;
    private int logo;

    private String linkToWebsite;
    private String programmingLanguage;

    private float rating = 0.0f;

    RecordModel(String projectName,
                String projectDescription,
                String authorName,
                int imageId,
                String linkToWebsite,
                String programmingLanguage) {
        this.authorName = authorName;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.logo = imageId;

        this.linkToWebsite = linkToWebsite;
        this.programmingLanguage = programmingLanguage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getLinkToWebsite() {
        return linkToWebsite;
    }

    public void setLinkToWebsite(String linkToWebsite) {
        this.linkToWebsite = linkToWebsite;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectName() {

        return projectName;
    }


    public int getLogo() {
        return logo;
    }
}*/
