package kr.hs.e_mirim.scaling.scheduler;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Update extends Activity implements View.OnClickListener{

    Button startTime, endTime;
    int st, et,s,e;
    TimePicker time;
    EditText addtitle, place, memo;
    ImageButton ok, delete;
    CheckBox days[] = new CheckBox[7];
    int daysbool[]=new int[7];
    ImageView colors[] = new ImageView[5];
    String colorname="null";
    int colorindex=-1;
    String indexs=new String();
    DataBaseHelper helper=new DataBaseHelper(this);
    SQLiteDatabase db;
    String data;
    int pcnt = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.update);

        startTime=(Button)findViewById(R.id.ustartTime);
        endTime=(Button)findViewById(R.id.uendTime);
        ok=(ImageButton)findViewById(R.id.uok);
        delete=(ImageButton)findViewById(R.id.delete);
        addtitle=(EditText)findViewById(R.id.uaddtitle);
        place=(EditText)findViewById(R.id.uplace);
        memo=(EditText)findViewById(R.id.umemo);

        for (int i = 0; i < days.length; i++) {
            days[i] = (CheckBox) findViewById(R.id.uday1 + i);
        }

        for (int i = 0; i < colors.length; i++) {
            colors[i] = (ImageView) findViewById(R.id.uucolor1 + i);
            Imageset(i,1);
            colors[i].setOnClickListener(this);
        }

        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        data=intent.getStringExtra("position");





        db = helper.getReadableDatabase();
        String al="select * from addtable;";
        Cursor c1=db.rawQuery(al,null);

        if(c1 !=null) {
            int count = c1.getCount();
            for (int i = 0; i < count; i++) {
                c1.moveToNext();
                int starttime =c1.getInt(10);
                int endtime = c1.getInt(11);
                ArrayList<Integer> indexsint = new ArrayList<>();
                String index[] = c1.getString(12).split("/");


                for(int n=0;n<index.length;n++) {
                    indexsint.add(Integer.parseInt(index[n]));
                }

                for(int n=0;n<index.length;n++) {
                    for (int k = 1; k <= endtime - starttime; k++) {
                        indexsint.add(indexsint.get(n) + (8 * k));
                    }
                }

                for(int p=0;p<indexsint.size();p++) {
                    if (indexsint.get(p).toString().equals(data.toString())) {
                        addtitle.setText(c1.getString(1));
                        place.setText(c1.getString(2));
                        for (int k = 0; k < 7; k++) {
                            daysbool[k] = c1.getInt(3 + k);
                        }
                        st=Integer.parseInt(c1.getString(10));
                        et=Integer.parseInt(c1.getString(11));
                        startTime.setText(st+": 00");
                        endTime.setText(et+": 00");
                        colorname = c1.getString(13);
                        //indexs=c1.getString(12);
                        memo.setText(c1.getString(14));
                    }
                }
            }
        }

        switch (colorname) {
            case "RED": {
                colorindex = 0;
                break;
            }
            case "ORANGE": {
                colorindex = 1;
                break;
            }
            case "YELLOW": {
                colorindex = 2;
                break;
            }
            case "BLUE": {
                colorindex = 3;
                break;
            }
            case "PUPPLE": {
                colorindex = 4;
                break;
            }
        }

        Imageset(colorindex,2);
        for(int i=0;i<days.length;i++){
            if(daysbool[i]==0){
                days[i].setChecked(false);}
            else if(daysbool[i]==1){
                days[i].setChecked(true);
            }
        }















        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.time, null);

                AlertDialog.Builder alt = new AlertDialog.Builder(Update.this);
                time = (TimePicker) view.findViewById(R.id.timepick);

                alt.setView(view);
                Toast.makeText(Update.this,"분에 상관없이 모든 시간은 1시간 단위로 일정이 저장됩니다.",Toast.LENGTH_LONG).show(); // 6시 ~ 24시 사이가 아니라면 일정표 시간 내 범주에서 데이터를 입력해달라고 토스트 생성
                alt.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                if(time.getHour()!=0) {
                                    s = st = time.getHour();
                                }
                                else{
                                    s=st=24;
                                } //1시간 단위
                                startTime.setText(st + ": 00");

                            }
                        });
                alt.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alt.setTitle("시작시간");

                AlertDialog alert = alt.create();
                alert.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.time, null);

                AlertDialog.Builder alt = new AlertDialog.Builder(Update.this);
                time = (TimePicker) view.findViewById(R.id.timepick);

                alt.setView(view);
                Toast.makeText(Update.this,"분에 상관없이 모든 시간은 1시간 단위로 일정이 저장됩니다.",Toast.LENGTH_LONG).show();
                alt.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(time.getHour()!=0) {
                                    e = et = time.getHour();
                                }
                                else{
                                    e=et=24;
                                }
                                endTime.setText(et + ": 00");
                            }
                        });
                alt.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alt.setTitle("마침시간");

                AlertDialog alert = alt.create();
                alert.show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check=0;
                int h = 6, cnt = 0;
                int hi[] = {8, 16, 24, 32, 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120, 128, 136, 144, 152, 160, 168, 176, 184};
                String title = addtitle.getText() + "";
                String placetext = place.getText() + "";
                String memotext = memo.getText() + "";


                for (int i = 0; i < daysbool.length; i++) {
                    if (days[i].isChecked()) daysbool[i] = 1;
                    else if(!(days[i].isChecked())) daysbool[i]=0;
                    else check++;
                }

                if((check==7)||(st > et)||(st>=1&&st<=5)||(et>=1&&et<=5)) {
                    if (check == 7) {
                        Toast.makeText(Update.this, "요일 중 하나라도 체크가 되어 있어야 합니다.", Toast.LENGTH_SHORT).show();
                        check = 0;
                    }


                    if (st > et) {
                        Toast.makeText(Update.this, "시작 시간이 마침 시간보다 큽니다.", Toast.LENGTH_SHORT).show();
                    }

                    if((st>=1&&st<=5)||(et>=1&&et<=5)){
                        Toast.makeText(Update.this, "시간은 06시부터 24시 사이여야만 합니다.", Toast.LENGTH_SHORT).show();
                    }
                }



                else {
                    s=st;
                    e=et;
                    indexs=new String();
                    while (s <= e) {
                        while (h <= 24) {
                            if (s == h) {
                                for (int i = 0; i < daysbool.length; i++) {
                                    if (daysbool[i] != 0) {
                                        indexs += hi[cnt] + i + 1;
                                        indexs += "/";
                                    }
                                }
                            }
                            h++;
                            cnt++;

                        }
                        s++;Log.i("s/h",s+"/"+h);
                    }

                    Log.i("title", title);
                    Log.i("placetext", placetext);
                    Log.i("memotext", memotext);
                    for (int i = 0; i < 7; i++) Log.i("day[" + i + "]", daysbool[i] + "");
                    Log.i("colorname", colorname);
                    Log.i("starttime", st + "");
                    Log.i("endtime", et + "");
                    Log.i("index", indexs); // indexs에 값이 안들어옴



                    db = helper.getReadableDatabase();
                    String al="select *from addtable;";

                    Cursor c1=db.rawQuery(al,null);

                    if(c1 !=null) {
                        int count = c1.getCount();
                        for (int i = 0; i < count; i++) {
                            c1.moveToNext();
                            db.execSQL("update addtable set sn='" + title + "',p='" + placetext + "',mo='" + daysbool[0] + "',tu='" + daysbool[1] + "',we='" + daysbool[2] + "',th='" + daysbool[3] + "',fr='" + daysbool[4] + "',sa='" + daysbool[5] +
                                    "',su='" + daysbool[6] + "',time1='" +st+ "" + "',time2='" + et + ""+ "',indexs='" + indexs + "',color='" + colorname + "',memo='" + memotext + "' where indexs='" + c1.getString(12) + "';");
                        }
                    }

                    Toast.makeText(Update.this, "일정 수정 성공", Toast.LENGTH_LONG).show();
                    finish();
                }




            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pcnt == 0) {
                    Toast.makeText(Update.this, "삭제 버튼을 누를 시 모든 일정들이 사라지게 됩니다. 한번 더 누를 시 삭제됩니다.", Toast.LENGTH_LONG).show();
                    pcnt = 1;
                } else if (pcnt == 1) {
                    db = helper.getReadableDatabase();
                    String al = "select *from addtable;";


                    Cursor c1 = db.rawQuery(al, null);

                    if (c1 != null) {
                        int count = c1.getCount();
                        for (int i = 0; i < count; i++) {
                            c1.moveToNext();
                            db.execSQL("DELETE FROM addtable WHERE indexs='" + c1.getString(12) + "';");

                        }
                    }
                    finish();
                }
            }
        });
    }
        @Override
        public void onClick(View v) {

            if (colorindex != -1) {
                Imageset(colorindex,1);
            }

                switch (v.getId()) {
                    case R.id.uucolor1:{
                        colorname = "RED";
                        colorindex = 0;
                        break;}
                    case R.id.uucolor2:{
                        colorname = "ORANGE";
                        colorindex = 1;
                        break;}
                    case R.id.uucolor3:{
                        colorname = "YELLOW";
                        colorindex = 2;
                        break;}
                    case R.id.uucolor4:{
                        colorname = "BLUE";
                        colorindex = 3;
                        break;}
                    case R.id.uucolor5:{
                        colorname = "PUPPLE";
                        colorindex = 4;
                        break;}
                }

                Imageset(colorindex,2);

            }

            public void Imageset(int i, int mode){
                if(mode==1) {
                    Glide.with(this)
                            .load(R.drawable.ucolor1 + i)
                            .fitCenter()
                            .crossFade()
                            .into(colors[i]);
                }
                if(mode==2) {
                    Glide.with(this)
                            .load(R.drawable.ccolor1 + i)
                            .fitCenter()
                            .crossFade()
                            .into(colors[i]);
                }
            }
}
