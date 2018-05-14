package kr.hs.e_mirim.scaling.scheduler;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by 소시지 2마리 on 2017-11-04.
 */

public class WidgetProvider extends AppWidgetProvider {
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        /**
         * 현재 시간 정보를 가져오기 위한 Calendar
         */
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd",
                Locale.KOREA);
        int day=mCalendar.get(Calendar.DAY_OF_WEEK);


        /**
         * RemoteViews를 이용해 Text설정
         */
        RemoteViews updateViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_activity);

        updateViews.setTextViewText(R.id.date,
                mFormat.format(mCalendar.getTime()));

        //
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String dayi=null;
        switch (day){
            case 1 : dayi="su"; break; // 일요일
            case 2 : dayi="mo"; break; // 월요일
            case 3 : dayi="tu"; break; // 화요일
            case 4 : dayi="we"; break; // 수요일
            case 5 : dayi="th"; break; // 목요일
            case 6 : dayi="fr"; break; // 금요일
            case 7 : dayi="sa"; break; // 토요일
        }

        if(dayi!=null) {
            String sql = "select sn, p from addtable where "+ dayi +"=1;";
            Cursor cursor = db.rawQuery(sql, null);
            String plan=new String();

            if (cursor != null) {
                int count = cursor.getCount();

                for (int i = 0; i < count; i++) {
                    cursor.moveToNext();
                    String title=cursor.getString(0), place=cursor.getString(1);
                    if(title.equals("")) title="제목 없음";
                    if(place.equals("")) place="장소 없음";
                    plan+=(i+1)+". "+title+" - "+place+"\n";
                }
            }

            plan+="\n※위젯은 30분 간격으로 업데이트됩니다.";

            updateViews.setTextViewText(R.id.plans,plan);
        }
            //
            /**
             * 위젯 업데이트
             */
            appWidgetManager.updateAppWidget(appWidgetId, updateViews);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    /* 위젯을 갱신할때 호출됨. 주의 : Configure Activity를 정의했을때는 위젯 등록시 처음 한번은 호출이 되지 않습니다*/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    /* 위젯이 처음 생성될때 호출되며, 동일한 위젯의 경우 처음 호출됩니다 */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /*위젯의 마지막 인스턴스가 제거될때 호출됨. onEnabled()에서 정의한 리소스 정리할때*/
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /*위젯이 사용자에 의해 제거될때 호출됨*/
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /*public void viewDB(){
        String sql = "select month, day, flower, means, file from writeFiles;";

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor !=null){
            int count  = cursor.getCount();
            for(int i=0; i<count; i++){
                cursor.moveToNext();

            }

        }

    }*/

}



