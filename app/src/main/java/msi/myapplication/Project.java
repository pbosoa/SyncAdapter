package msi.myapplication;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MSI! on 08.10.2015.
 */
public class Project implements Serializable {
    public String title;
    public String updatedAt;
    public String author;
    public String createdAt;
    public String description;
    public String link;
    public String objectId;
    public List tags;

    public Project(String title, String updatedAt, String author,
                   String createdAt, String description, String link,
                   String objectId) {
        this.title = title;
        this.updatedAt = updatedAt;
        this.author = author;
        this.createdAt = createdAt;
        this.description = description;
        this.link = link;
        this.objectId = objectId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List getTags() {
        return tags;
    }

    public void setTags(List tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Convenient method to get the objects data members in ContentValues object.
     * This will be useful for Content Provider operations,
     * which use ContentValues object to represent the data.
     *
     * @return
     */
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_TITLE, title);
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_LINK_TO_WEBSITE, link);
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_DESCRIPTION,
                description);
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_STUDENT, author);
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_CREATED_AT, createdAt);
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_UPDATED_AT, updatedAt);
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_OBJECT_ID, objectId);
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_PROGRAMMING_LANGUAGE,
                "");
        values.put(RecordModel.RecordModelEntry.COLUMN_NAME_RATING,
                "0");

        return values;
    }

    // Create a TvShow object from a cursor
    public static Project fromCursor(Cursor cursor) {;

        String title = cursor.getString(cursor.getColumnIndex(
                RecordModel.RecordModelEntry.COLUMN_NAME_TITLE));

        String updatedAt = cursor.getString(cursor.getColumnIndex(
                RecordModel.RecordModelEntry.COLUMN_NAME_UPDATED_AT));
        String author = cursor.getString(cursor.getColumnIndex(
                RecordModel.RecordModelEntry.COLUMN_NAME_STUDENT));


        String createdAt = cursor.getString(cursor.getColumnIndex(
                RecordModel.RecordModelEntry.COLUMN_NAME_CREATED_AT));

        String description = cursor
                .getString(cursor.getColumnIndex(
                        RecordModel.RecordModelEntry.COLUMN_NAME_DESCRIPTION));
        String link = cursor.getString(cursor.getColumnIndex(
                RecordModel.RecordModelEntry.COLUMN_NAME_LINK_TO_WEBSITE));
        String objectId = cursor.getString(cursor.getColumnIndex(
                RecordModel.RecordModelEntry.COLUMN_NAME_OBJECT_ID));

        return new Project(title, updatedAt, author, createdAt,
                description, link, objectId);
    }
}
