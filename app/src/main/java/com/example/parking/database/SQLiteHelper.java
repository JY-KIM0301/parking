package com.example.parking.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import com.example.parking.model.Memo;

import java.util.ArrayList;

public class SQLiteHelper {
    private static final String dbName = "myMemotest";
    private static final String table1 = "MemoTable";
    private static final int dbVersion = 1;

    private OpenHelper opener; //db 관련객체
    private SQLiteDatabase db;

    private Context context;

    public SQLiteHelper(Context context) {
        this.context = context;
        this.opener = new OpenHelper(context,dbName, null, dbVersion);
        db = opener.getWritableDatabase();
    }

    private class OpenHelper extends SQLiteOpenHelper{

        public OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) { //db 생성
            String create = "CREATE TABLE "+ table1 + " (" +
                    "seq integer PRIMARY KEY AUTOINCREMENT, "+
                    "maintext text,"+
                    "subtext text,"+
                    "isdone integer)";
            sqLiteDatabase.execSQL(create);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+table1);
            onCreate(sqLiteDatabase);
        }
    }

    //<메모입력> INSERT INTO MemoTable VALUES(NULL,'MAINTEXT','SUBTEXT',0);
    public void insertMemo(Memo memo){//메모입력
        String sql = "INSERT INTO "+table1+" VALUES(NULL, '"+memo.getMaintext()+"','"+memo.getSubtext()+"',"+memo.getIsdone()+");";
        db.execSQL(sql);
    }

    //<메모 조회> SELECT*FROM MemoTable;
    public ArrayList<Memo> selectAll(){
        String sql = "SELECT * FROM "+ table1;

        ArrayList<Memo> list = new ArrayList<>();

        Cursor results = db.rawQuery(sql,null);
        results.moveToFirst();

        while (!results.isAfterLast()){
            Memo memo = new Memo(results.getInt(0),results.getString(1),results.getString(2),results.getInt(3));
            list.add(memo);
            results.moveToNext();
        }
        results.close();
        return list;
    }
}
