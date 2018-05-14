package kr.hs.e_mirim.scaling.scheduler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleAdapter extends BaseAdapter {

    Context mContext;
    int i=0;
    int count = 160;
    String[] time={
            "06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
    DataBaseHelper helper;
    SQLiteDatabase db;

    String[] mWeekTitleIds ={
            " ",
            "월",
            "화",
            "수",
            "목",
            "금",
            "토",
            "일"
    };



    ScheduleAdapter(Context context){
        mContext = context;
        helper = new DataBaseHelper(mContext);
        db = helper.getReadableDatabase();
    }
    @Override
    public int getCount() {
        return count;
    }
    @Override
    public Object getItem(int arg0) {
        return null;
    }
    @Override
    public long getItemId(int arg0) {
        return 0;
    }
    @Override
    public View getView(int position, View oldView, ViewGroup parent) {
        View v=null;

        if(oldView == null)
        {
            v = new TextView(mContext);
            v.setLayoutParams(new GridView.LayoutParams(40 , 60));
        }
        else if (position < 8) {
            v = new TextView(mContext);
            ((TextView)v).setGravity(Gravity.CENTER);
            ((TextView)v).setText(mWeekTitleIds[position]);
            ((TextView) v).setTextColor(Color.BLACK);



        }
        else if (position >= 8 && position < count) {

            if(position%8==0) {
                v = new TextView(mContext);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setText(time[i]);
                ((TextView) v).setTextColor(Color.BLACK);
                i++;
            }
            else{
                v = new TextView(mContext);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setText(" ");
                ((TextView) v).setTextColor(Color.BLACK);

                v.setBackgroundColor(Color.parseColor("#9ec0f2"));

                viewDB(position,v);
            }
        }
        else {
            v = oldView;
        }

        return v;
    }

    public void viewDB(int pos, View v){
        String sql = "select time1, time2, indexs, color from addtable;";

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor !=null){
            int count  = cursor.getCount();
            String countI = String.valueOf(count);
            Log.i("count", countI);
            for(int i=0; i<count; i++){
                cursor.moveToNext();
                int starttime=cursor.getInt(0);
                int endtime=cursor.getInt(1);
                ArrayList<Integer> indexs=new ArrayList<>();
                String index[]=cursor.getString(2).split("/");
                String color=cursor.getString(3);

                for(int n=0;n<index.length;n++) {
                    indexs.add(Integer.parseInt(index[n]));
                }

                for(int n=0;n<index.length;n++) {
                    for (int k = 1; k <= endtime - starttime; k++) {
                        indexs.add(indexs.get(n) + (8 * k));
                    }
                }

                /*for (int n = 0; n < indexs.size(); n++) {
                    Log.i("count/index[" + n + "]", count + "/" + indexs.get(n));
                }*/

                for(int k=0;k<indexs.size();k++) {
                    if (pos == indexs.get(k)){
                        switch(color) {
                            case "RED": v.setBackgroundColor(Color.parseColor("#FBB9BA"));
                                break;
                            case "ORANGE": v.setBackgroundColor(Color.parseColor("#FDD3BA"));
                                break;
                            case "YELLOW": v.setBackgroundColor(Color.parseColor("#FDEBBB"));
                                break;
                            case "BLUE": v.setBackgroundColor(Color.parseColor("#B9E8FC"));
                                break;
                            case "PUPPLE": v.setBackgroundColor(Color.parseColor("#C1BAFE"));
                                break;
                            //
                        }
                    }
                }

            }

        }

    }

}