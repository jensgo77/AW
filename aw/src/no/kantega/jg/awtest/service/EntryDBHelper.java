package no.kantega.jg.awtest.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import no.kantega.jg.awtest.domain.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 30.10.13
 * Time: 10:20
 */
public class EntryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "presocach";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "entry";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " (" +
                    "_id integer primary key autoincrement, " +
                    "id TEXT UNIQUE, " +
                    "title TEXT, " +
                    "summary TEXT," +
                    "batchupdate INTEGER);";

    private static final String PK_COLUMN = "_id";
    private static final String ID_COLUMN = "id";
    private static final String TITLE_COLUMN = "title";
    private static final String SUMMARY_COLUMN = "summary";
    private static final String BATCHUPDATE_COLUMN = "batchupdate";

    EntryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseUtils.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //CRUD                               // måling, 114 insert = 3370 millisek.
    public void addEntry(Entry entry) {
        SQLiteDatabase db = null;
        DatabaseUtils.InsertHelper ih = null;
        try {
            db = getWritableDatabase();
            ih = new DatabaseUtils.InsertHelper(db, TABLE_NAME);

            final int idColumn = ih.getColumnIndex(ID_COLUMN);
            final int titleColumn = ih.getColumnIndex(TITLE_COLUMN);
            final int summaryColumn = ih.getColumnIndex(SUMMARY_COLUMN);
            final int batchupdateColumn = ih.getColumnIndex(BATCHUPDATE_COLUMN);

            ih.prepareForInsert();
            ih.bind(idColumn, entry.getId());
            ih.bind(titleColumn, entry.getTitle());
            ih.bind(summaryColumn, entry.getSummary());
            ih.bind(batchupdateColumn, 1);
            ih.execute();
        } finally {
            try { if( ih != null) ih.close(); } catch( Exception e) { Log.e("presoservice", e.toString()); }
            try { if( db != null) db.close(); } catch( Exception e) { Log.e("presoservice", e.toString()); }
        }
    }

    public void addEntries(List<Entry> list) {    // måling 114 insert som over 185milli
        SQLiteDatabase db = null;
        DatabaseUtils.InsertHelper ih = null;
        try {
            db = getWritableDatabase();
            ih = new DatabaseUtils.InsertHelper(db, TABLE_NAME);

            final int idColumn = ih.getColumnIndex(ID_COLUMN);
            final int titleColumn = ih.getColumnIndex(TITLE_COLUMN);
            final int summaryColumn = ih.getColumnIndex(SUMMARY_COLUMN);
            final int batchupdateColumn = ih.getColumnIndex(BATCHUPDATE_COLUMN);

            db.beginTransaction();
            for( Entry entry : list) {
                ih.prepareForInsert();
                ih.bind(idColumn, entry.getId());
                ih.bind(titleColumn, entry.getTitle());
                ih.bind(summaryColumn, entry.getSummary());
                ih.bind(batchupdateColumn, 1);
                ih.execute();
            }
            db.setTransactionSuccessful();
        } finally {
            try { if( db != null) db.endTransaction(); } catch(Exception e) { Log.e("presoservice", e.toString()); }
            try { if( ih != null) ih.close(); } catch( Exception e) { Log.e("presoservice", e.toString()); }
            try { if( db != null) db.close(); } catch( Exception e) { Log.e("presoservice", e.toString()); }
        }
    }

    public void addEntries2(List<Entry> list) {  // måling 114 insert 194 milli
        SQLiteDatabase db = null;
        SQLiteStatement stm = null;
        try {
            db = getWritableDatabase();
            stm = db.compileStatement("insert into entry (id, title, summary, batchupdate) values(?, ?, ?, 1)");

            db.beginTransaction();
            for( Entry e : list) {
                stm.bindAllArgsAsStrings(new String[]{e.getId(), e.getTitle(), e.getSummary()});
                stm.executeInsert();
            }
            db.setTransactionSuccessful();
        } finally {
            try { if( db != null) db.endTransaction(); } catch(Exception e) { Log.e("presoservice", e.toString()); }
            try { if( stm != null) stm.close(); } catch( Exception e) { Log.e("presoservice", e.toString()); }
            try { if( db != null) db.close(); } catch( Exception e) { Log.e("presoservice", e.toString()); }
        }
    }

    public void prepareBatchUpdate() {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            //db.rawQuery("update entry set batchupdate=0", null);
            ContentValues val = new ContentValues();
            val.put(BATCHUPDATE_COLUMN, 0);
            db.update(TABLE_NAME, val, null, null);
        }
        finally {
            try { if( db != null ) db.close(); } catch( Exception e) { Log.e("presoservice", e.toString());  }
        }
    }

    public void cleanupBatchUpdate() {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.delete(TABLE_NAME, "batchupdate=?", new String[]{"0"});
        }
        finally {
            try { if( db != null ) db.close(); } catch( Exception e) { Log.e("presoservice", e.toString());  }
        }
    }

    public Entry getEntryByPrimaryId(int id) {
        return getEntry("select * from entry where _id = ?",new String[]{Integer.toString(id)});
    }

    private Entry getEntry(String sql, String[] param) {
        SQLiteDatabase db = null;
        Cursor c = null;
        Entry toReturn = null;
        try {
            db = getReadableDatabase();
            c = db.rawQuery(sql, param);
            c.moveToFirst();

            if( c.isAfterLast() == false ) {
                Entry e = new Entry();
                e.setId(c.getString(c.getColumnIndex(ID_COLUMN)));
                e.setTitle(c.getString(c.getColumnIndex(TITLE_COLUMN)));
                e.setSummary(c.getString(c.getColumnIndex(SUMMARY_COLUMN)));

                toReturn = e;
            }
        } finally {
            try { if( c != null ) c.close(); } catch(Exception e) { Log.e("presoservice", e.toString());  }
            try { if( db != null ) db.close(); } catch(Exception e) { Log.e("presoservice", e.toString());  }
        }
        return toReturn;
    }

    public Entry getEntryByRealId(String id) {
        return getEntry("select * from entry where id = ?",new String[]{id});
    }

    public List<Entry> getAllEntry() {
        SQLiteDatabase db = null;
        Cursor c = null;
        List<Entry> toReturn = new ArrayList();

        try {
            db = getReadableDatabase();

            c = db.rawQuery("select * from entry", null);

            c.moveToFirst();
            while (c.isAfterLast() == false)
            {
                Entry e = new Entry();
                e.setId(c.getString(c.getColumnIndex(ID_COLUMN)));
                e.setTitle(c.getString(c.getColumnIndex(TITLE_COLUMN)));
                e.setSummary(c.getString(c.getColumnIndex(SUMMARY_COLUMN)));
                c.moveToNext();
                toReturn.add(e);
            }
        } finally {
            try { if( c != null) c.close(); } catch(Exception e) { Log.e("presoservice", e.toString()); }
            try { if( db != null) db.close(); } catch(Exception e) { Log.e("presoservice", e.toString()); }
        }
        return toReturn;
    }

    public int getEntryCount() {
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getReadableDatabase();
            db.execSQL("delete from entry");
            c = getReadableDatabase().rawQuery("select count(*) from entry", null);
        } finally {
            try { if( db != null) db.close(); } catch(Exception e) { Log.e("presoservice", e.toString());  }
        }


        return c.getInt(1);
    }

    public int updateEntry(Entry entry) {
        SQLiteDatabase db = null;
        int retVal = 0;
        try {

            db = getWritableDatabase();
            ContentValues val = new ContentValues();
            val.put(TITLE_COLUMN, entry.getTitle());
            val.put(SUMMARY_COLUMN, entry.getSummary());
            val.put(BATCHUPDATE_COLUMN, 1);

            retVal = db.update(TABLE_NAME, val, "id=?", new String[]{entry.getId()});
        } finally {
            try { if( db != null) db.close(); } catch( Exception e) { Log.e("presoservice", e.toString());  }
        }

        return retVal;
    }

    public void deleteEntry(Entry entry) {

    }

    public void deleteAllEnties() {
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            db.execSQL("delete from entry");
        } finally {
            try { if( db != null) db.close(); } catch(Exception e) { Log.e("presoservice", e.toString()); }
        }
    }
}