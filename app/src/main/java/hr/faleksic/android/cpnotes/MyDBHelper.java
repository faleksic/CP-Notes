package hr.faleksic.android.cpnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.cpnotes.R;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public static final String NOTE_TABLE_NAME = "note";
    public static final String NOTE_COLUMN_ID = "_id";
    public static final String NOTE_COLUMN_TITLE = "title";
    public static final String NOTE_COLUMN_CONTENT = "content";
    public static final String NOTE_COLUMN_CATEGORY = "category";

    public static final String CATEGORY_TABLE_NAME = "category";
    public static final String CATEGORY_COLUMN_ID = "_id";
    public static final String CATEGORY_COLUMN_NAME = "name";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + "(" +
                CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                CATEGORY_COLUMN_NAME + " TEXT UNIQUE)"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + NOTE_TABLE_NAME + "(" +
                NOTE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                NOTE_COLUMN_TITLE + " TEXT, " +
                NOTE_COLUMN_CONTENT + " TEXT, " +
                NOTE_COLUMN_CATEGORY + " INTEGER, " +
                "FOREIGN KEY (" + NOTE_COLUMN_CATEGORY + ") REFERENCES " +
                CATEGORY_TABLE_NAME + "(" + CATEGORY_COLUMN_ID + ") ON DELETE CASCADE)"
        );
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_NAME, context.getResources().getString(R.string.uncategorised));
        sqLiteDatabase.insert(CATEGORY_TABLE_NAME, null, contentValues);
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
        db.update(CATEGORY_TABLE_NAME, contentValues, CATEGORY_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean updateNote(int id, String title, String content, int category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE, title);
        contentValues.put(NOTE_COLUMN_CONTENT, content);
        contentValues.put(NOTE_COLUMN_CATEGORY, category);
        db.update(NOTE_TABLE_NAME, contentValues, NOTE_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Cursor getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " +
                CATEGORY_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public int getCategoryId(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + CATEGORY_COLUMN_ID + " FROM " + CATEGORY_TABLE_NAME + " WHERE " +
                CATEGORY_COLUMN_NAME + "=?", new String[]{name});
        int id = -1;
        while (res.moveToNext()) {
            id = res.getInt(0);
        }
        res.close();

        return id;
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + CATEGORY_COLUMN_ID + ", " + CATEGORY_COLUMN_NAME + ", (SELECT COUNT(*) FROM " + NOTE_TABLE_NAME
                + " WHERE " + NOTE_COLUMN_CATEGORY + "=" + CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ID + ") AS count" +
                " FROM " + CATEGORY_TABLE_NAME, null);
        return res;
    }

    public int countAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT COUNT(*) FROM " + NOTE_TABLE_NAME, null);
        int count = 0;
        while (res.moveToNext()) {
            count = res.getInt(0);
        }
        res.close();

        return count;
    }

    public Cursor getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT n." + NOTE_COLUMN_ID + ", n." + NOTE_COLUMN_TITLE + ", n." + NOTE_COLUMN_CONTENT + ", c." + CATEGORY_COLUMN_NAME +
                " FROM " + NOTE_TABLE_NAME + " n JOIN " + CATEGORY_TABLE_NAME + " c ON n." + NOTE_COLUMN_CATEGORY + "=c." + CATEGORY_COLUMN_ID + " WHERE n." +
                NOTE_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT n." + NOTE_COLUMN_ID + ", n." + NOTE_COLUMN_TITLE + ", n." + NOTE_COLUMN_CONTENT + ", c." + CATEGORY_COLUMN_NAME +
                " FROM " + NOTE_TABLE_NAME + " n JOIN " + CATEGORY_TABLE_NAME + " c ON n." + NOTE_COLUMN_CATEGORY + "=c." + CATEGORY_COLUMN_ID, null);
        return res;
    }

    public Cursor getAllNotesWithCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT n." + NOTE_COLUMN_ID + ", n." + NOTE_COLUMN_TITLE + ", n." + NOTE_COLUMN_CONTENT + ", c." + CATEGORY_COLUMN_NAME +
                " FROM " + NOTE_TABLE_NAME + " n JOIN " + CATEGORY_TABLE_NAME + " c ON n." + NOTE_COLUMN_CATEGORY + "=c." + CATEGORY_COLUMN_ID + " WHERE " +
                NOTE_COLUMN_CATEGORY + "=?", new String[]{Integer.toString(id)}, null);
        return res;
    }

    public Integer deleteCategory(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CATEGORY_TABLE_NAME,
                CATEGORY_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    public Integer deleteNote(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTE_TABLE_NAME,
                NOTE_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }
}
