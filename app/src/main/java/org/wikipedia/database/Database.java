package org.wikipedia.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.ieatta.activity.search.RecentSearch;
import org.ieatta.activity.history.HistoryEntry;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "IEATTA.db";
    private static final int DATABASE_VERSION = 8;

    private final DatabaseTable[] databaseTables = {
            HistoryEntry.DATABASE_TABLE,
            RecentSearch.DATABASE_TABLE,
//            EditSummary.DATABASE_TABLE
    };

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (DatabaseTable table : databaseTables) {
            table.createTables(sqLiteDatabase, DATABASE_VERSION);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int from, int to) {
        for (DatabaseTable table : databaseTables) {
            table.upgradeSchema(sqLiteDatabase, from, to);
        }
    }
}
