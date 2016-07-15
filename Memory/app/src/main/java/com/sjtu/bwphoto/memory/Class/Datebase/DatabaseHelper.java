package com.sjtu.bwphoto.memory.Class.Datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/14.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER_DETAIL="create table UserDetail("
            + "id integer primary key autoincrement,"
            + "account text,"
            + "nickname text，"
            + "emailAddress text,"
            + "age integer,"
            + "birthday text)";

    public static final  String CREATE_VIEW_STORE_PAGE1= "create table Page1("
        + "id integer primary key autoincrement,"
                + "account text,"
                + "rankNum integer,"
                + "location text,"
                +  "memoryText text,"
                +  "imageURL text)";

    public static final  String CREATE_VIEW_STORE_PAGE2= "create table Page2("
            + "id integer primary key autoincrement,"
            + "account text,"
            + "rankNum integer,"
            + "location text,"
            +  "memoryText text,"
            +  "imageURL text)";

    public static final  String CREATE_VIEW_STORE_PAGE3= "create table Page3("
            + "id integer primary key autoincrement,"
            + "account text,"
            + "rankNum integer,"
            + "location text,"
            +  "memoryText text,"
            +  "imageURL text)";

    private Context mcontext;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_DETAIL);
        sqLiteDatabase.execSQL(CREATE_VIEW_STORE_PAGE1);
        sqLiteDatabase.execSQL(CREATE_VIEW_STORE_PAGE2);
        sqLiteDatabase.execSQL(CREATE_VIEW_STORE_PAGE3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
