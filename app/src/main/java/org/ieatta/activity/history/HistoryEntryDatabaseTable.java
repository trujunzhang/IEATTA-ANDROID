package org.ieatta.activity.history;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.ieatta.provide.MainSegueIdentifier;
import org.wikipedia.Site;
import org.wikipedia.database.DatabaseTable;

import java.util.Date;

public class HistoryEntryDatabaseTable extends DatabaseTable<HistoryEntry> {

    private static final int DB_VER_NAMESPACE_ADDED = 6;

    private static final String COL_SITE = "site";
    private static final String COL_TITLE = "title";
    private static final String COL_NAMESPACE = "namespace";
    private static final String COL_TIMESTAMP = "timestamp";
    private static final String COL_SOURCE = "source";

    public static final String[] SELECTION_KEYS = {
            COL_SITE,
            COL_NAMESPACE,
            COL_TITLE
    };

    @Override
    public HistoryEntry fromCursor(Cursor c) {
        Site site = new Site(getString(c, COL_SITE));
//        PageTitle title = new PageTitle(getString(c, COL_NAMESPACE), getString(c, COL_TITLE), site);
        Date timestamp = new Date(getLong(c, COL_TIMESTAMP));
        int source = getInt(c, COL_SOURCE);
        return new HistoryEntry(MainSegueIdentifier.nearbyRestaurants);
    }

    @Override
    protected ContentValues toContentValues(HistoryEntry obj) {
        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_SITE, obj.getTitle().getSite().getDomain());
//        contentValues.put(COL_TITLE, obj.getTitle().getText());
//        contentValues.put(COL_NAMESPACE, obj.getTitle().getNamespace());
        contentValues.put(COL_TIMESTAMP, obj.getTimestamp().getTime());
        contentValues.put(COL_SOURCE, obj.getSource());
        return contentValues;
    }

    @Override
    public String getTableName() {
        return "history";
    }

    @Override
    public Column[] getColumnsAdded(int version) {
        switch (version) {
            case 1:
                return new Column[] {
                        new Column("_id", "integer primary key"),
                        new Column(COL_SITE, "string"),
                        new Column(COL_TITLE, "string"),
                        new Column(COL_TIMESTAMP, "integer"),
                        new Column(COL_SOURCE, "integer")
                };
            case DB_VER_NAMESPACE_ADDED:
                return new Column[] {
                        new Column(COL_NAMESPACE, "string")
                };
            default:
                return new Column[0];
        }
    }

    @Override
    protected String getPrimaryKeySelection(@NonNull HistoryEntry obj,
                                            @NonNull String[] selectionArgs) {
        return super.getPrimaryKeySelection(obj, SELECTION_KEYS);
    }

    @Override
    protected String[] getUnfilteredPrimaryKeySelectionArgs(@NonNull HistoryEntry obj) {
        return new String[] {
//                obj.getTitle().getSite().getDomain(),
//                obj.getTitle().getNamespace(),
//                obj.getTitle().getText()
        };
    }

    @Override
    protected int getDBVersionIntroducedAt() {
        return INITIAL_DB_VERSION;
    }

    @Override
    protected void convertAllTitlesToUnderscores(SQLiteDatabase db) {
        Cursor cursor = db.query(getTableName(), null, null, null, null, null, null);
        int idIndex = cursor.getColumnIndexOrThrow("_id");
        int titleIndex = cursor.getColumnIndexOrThrow(COL_TITLE);
        ContentValues values = new ContentValues();
        while (cursor.moveToNext()) {
            String title = cursor.getString(titleIndex);
            if (title.contains(" ")) {
                values.put(COL_TITLE, title.replace(" ", "_"));
                String id = Long.toString(cursor.getLong(idIndex));
                db.updateWithOnConflict(getTableName(), values, "_id = ?", new String[]{id}, SQLiteDatabase.CONFLICT_REPLACE);
            }
        }
        cursor.close();
    }
}
