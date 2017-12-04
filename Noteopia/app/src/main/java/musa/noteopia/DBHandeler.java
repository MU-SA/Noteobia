package musa.noteopia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by lenovoo on 8/28/2017.
 */

public class DBHandeler extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "NOTES";
    public static final String CLIENT_ID = "ID";
    private static final String DATABASE_NAME = "50ThingDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String NOTES = "NOTE";


    public DBHandeler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SESSION = "CREATE TABLE " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTES + " VARCHAR(50) NOT NULL );";
        db.execSQL(CREATE_SESSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public int getSize() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res.getCount();
    }

    public List<String> getObject() {
        List<String> objects = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String object = cursor.getString(cursor.getColumnIndex(NOTES));
                objects.add(object);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return objects;
    }


    public boolean insertObject(String object) {
        long res = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(NOTES, object);
        res = db.insert(TABLE_NAME, null, content);
        return res > -1;
    }

    public List<String> deleteData(final int Position) {
        final List<String> objects = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        final SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String object = cursor.getString(cursor.getColumnIndex(NOTES));
                objects.add(object);
            } while (cursor.moveToNext());

        }

        db.delete(TABLE_NAME, "NOTE = ?", new String[]{objects.get(Position)});

        cursor.close();
        db.close();
        return objects;
    }

    String getDataById(int position) {
        String query = "SELECT " + NOTES + " FROM " + TABLE_NAME + " WHERE ID = " + position;
        SQLiteDatabase db = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        String note = null;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() != true) {
                note = cursor.getString(cursor.getColumnIndex("NOTE"));
            }
        }

        return note;
    }

    public boolean updateData(String object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES, object);
        db.update(TABLE_NAME, contentValues, "Name = ?", new String[]{object});
        return true;
    }


}
