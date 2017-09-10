package hr.faleksic.android.cpnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NOTE_TABLE_NAME = "note";
    public static final String NOTE_COLUMN_ID = "_id";
    public static final String NOTE_COLUMN_TITLE = "title";
    public static final String NOTE_COLUMN_CONTENT = "content";
    public static final String NOTE_COLUMN_CATEGORY = "category";

    public static final String CATEGORY_TABLE_NAME = "category";
    public static final String CATEGORY_COLUMN_ID = "_id";
    public static final String CATEGORY_COLUMN_NAME = "name";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + "(" +
                CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                CATEGORY_COLUMN_NAME + " TEXT)"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + NOTE_TABLE_NAME + "(" +
                NOTE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                NOTE_COLUMN_TITLE + " TEXT, " +
                NOTE_COLUMN_CONTENT + " TEXT, " +
                NOTE_COLUMN_CATEGORY + " INTEGER, " +
                "FOREIGN KEY (" + NOTE_COLUMN_CATEGORY + ") REFERENCES " +
                CATEGORY_TABLE_NAME + "(" + CATEGORY_COLUMN_ID + ") ON DELETE CASCADE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertCategory(String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_NAME, name);
        db.insert(CATEGORY_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertNote(String title, String content, int category) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE, title);
        contentValues.put(NOTE_COLUMN_CONTENT, content);
        contentValues.put(NOTE_COLUMN_CATEGORY, category);
        db.insert(NOTE_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateCategory(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_NAME, name);
        db.update(CATEGORY_TABLE_NAME, contentValues, CATEGORY_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateNote(int id, String title, String content, int category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE, title);
        contentValues.put(NOTE_COLUMN_CONTENT, content);
        contentValues.put(NOTE_COLUMN_CATEGORY, category);
        db.update(NOTE_TABLE_NAME, contentValues, NOTE_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Cursor getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " +
                CATEGORY_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
        return res;
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + CATEGORY_TABLE_NAME, null );
        return res;
    }

    public Cursor getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + NOTE_TABLE_NAME + " WHERE " +
                NOTE_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
        return res;
    }

    public Cursor getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + NOTE_TABLE_NAME, null );
        return res;
    }

    public Integer deleteCategory(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CATEGORY_TABLE_NAME,
                CATEGORY_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteNote(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTE_TABLE_NAME,
                NOTE_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }
}
