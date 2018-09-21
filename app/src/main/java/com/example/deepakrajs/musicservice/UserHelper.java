package com.example.deepakrajs.musicservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "USERINFO.DB";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_QUERY =
            "CREATE TABLE " + UserContract.NewUserInfo.TABLE_NAME + "(" + UserContract.NewUserInfo.USER_ID + " TEXT primary key," +
                    UserContract.NewUserInfo.USER_NAME + " TEXT," + UserContract.NewUserInfo.USER_PASSWORD + " TEXT);";

    public UserHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DataBase Operations", "DataBase Created/Opened...");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creating Table
        db.execSQL(CREATE_QUERY);
        Log.e("DataBase Operations", "Table Created...");
    }

    //Inserting in DataBase
    public boolean addInformations(String id, String name, String password, SQLiteDatabase db) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserContract.NewUserInfo.USER_ID, id);
        contentValues.put(UserContract.NewUserInfo.USER_NAME, name);
        contentValues.put(UserContract.NewUserInfo.USER_PASSWORD, password);
        Long ins = db.insert(UserContract.NewUserInfo.TABLE_NAME, null, contentValues);

        if (ins == -1) {
            Log.e("DataBase Operations", "Insertion Failed...");
            return false;
        } else {
            Log.e("DataBase Operations", "One Row Inserted...");
            return true;
        }
    }

    //Checking if the Id exists
    public Boolean checkId(String id) {
        String CHECK_QUERY =
                "select * from " + UserContract.NewUserInfo.TABLE_NAME + " where " + UserContract.NewUserInfo.USER_ID + "= ?;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(CHECK_QUERY, new String[]{id});

        if (cursor.getCount() > 0) {
            return false;
        } else {
            return true;
        }
    }

    //Checking the Id and Password
    public Boolean checkIdPassword(String id, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String CHECK_QUERY =
                "select * from " + UserContract.NewUserInfo.TABLE_NAME + " where "
                        + UserContract.NewUserInfo.USER_ID + "= ? and " + UserContract.NewUserInfo.USER_PASSWORD + "= ?;";

        Cursor cursor = db.rawQuery(CHECK_QUERY, new String[]{id, password});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getName(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String NAME_QUERY = "select " + UserContract.NewUserInfo.USER_NAME + " from " + UserContract.NewUserInfo.TABLE_NAME +
                " where " + UserContract.NewUserInfo.USER_ID + " =?;";
        Cursor cursor = db.rawQuery(NAME_QUERY, new String[]{id});
        String name = cursor.toString();
        return name;
    }

    //Getting the Name
    /*public String getName(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
//        String NAME_QUERY = "select " + UserContract.NewUserInfo.USER_NAME + " from " + UserContract.NewUserInfo.TABLE_NAME +
//                " where " + UserContract.NewUserInfo.USER_ID + " =?;";
//
//        Cursor cursor = db.rawQuery(NAME_QUERY, new String[]{id});
//        return NAME_QUERY;

        String [] projection = {UserContract.NewUserInfo.USER_NAME};
        String selection = UserContract.NewUserInfo.USER_ID+ " ?";
        String [] selection_args = {id};
        Cursor cursor =
                db.query(UserContract.NewUserInfo.TABLE_NAME, projection, selection, selection_args, null, null, null);
        if (cursor.moveToFirst()) {
            String uName = cursor.getString(0);
            String name = uName.toUpperCase();
            return name;
        } else {
            return null;
        }
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
