package com.lily.YumYum.database;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Bookmark.db";
    public static final String BOOKMARK_TABLE_NAME = "Foods";
    public static final String BOOKMARK_COLUMN_ID = "id";
    public static final String BOOKMARK_COLUMN_POSITION = "position";
    public static final String BOOKMARK_COLUMN_NAME = "name";
    private HashMap hp;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table Foods " +
                        "(id integer primary key, name text, position text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Foods");
        onCreate(db);
    }

    public static void deleteDatabase(Context mContext) {
        mContext.deleteDatabase(DATABASE_NAME);
    }

    public boolean insertFood(String name, String position) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete from locations");
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("position", position);
        Log.i("inset", "insertFood: " + name + position + "to " + BOOKMARK_TABLE_NAME);
        long res = db.insert("Foods", null, contentValues);
        if (res == -1){
            Log.i("inset", "insertFood: " + "did not");
            return false;
        }
        else {
            Log.i("inset", "insertFood: " + "did not");
        }
        return true;
    }

    public Cursor getDataByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Foods where name="+'"'+name+'"'+"", null );
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Foods", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, BOOKMARK_TABLE_NAME);
        return numRows;
    }

    public boolean updateFood (Integer id, String name, String position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("position", position);
        db.update("Foods", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteFood (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Foods",
                "name = ? ",
                new String[] { name });
    }


    public ArrayList<String> getAllFoods() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Foods", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(BOOKMARK_COLUMN_NAME)));
            res.moveToNext();
        }
        Log.i("arraylist", "onClick: " + array_list);
        return array_list;
    }

    public Pair getAllPositions() {
        ArrayList<Pair> array_list = new ArrayList<Pair>();
        ArrayList<String> array_names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Foods", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_names.add(res.getString(res.getColumnIndex(BOOKMARK_COLUMN_NAME)));
            array_list.add(new Pair(res.getString(res.getColumnIndex(BOOKMARK_COLUMN_NAME)),res.getString(res.getColumnIndex(BOOKMARK_COLUMN_POSITION))));
            res.moveToNext();
        }
        Log.i("arraylist", "onClick: " + array_list);
        return new Pair(array_list, array_names);
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from Foods");
    }
}
