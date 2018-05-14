package kr.hs.e_mirim.scaling.scheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 소시지 2마리 on 2017-11-07.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    Context context;

    public DataBaseHelper(Context context) {
        super(context, "Scheduler.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table addtable(" +//
                "num integer primary key autoincrement, " +
                "sn text not null, " +
                "p text, " +
                "mo integer not null, " +
                "tu integer not null, " +
                "we integer not null, " +
                "th integer not null, " +
                "fr integer not null, " +
                "sa integer not null, " +
                "su integer not null, " +
                "time1 integer not null, " +
                "time2 integer not null, " +
                "indexs text not null, " + // index가 여러개일 수 있음. ','로 구분하여 분리해서 사용.
                "color text not null, " +
                "memo text" +
                ");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exist addtable";
        db.execSQL(sql);
        onCreate(db);
    }
}
