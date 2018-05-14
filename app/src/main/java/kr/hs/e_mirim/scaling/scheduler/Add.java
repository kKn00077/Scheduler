package kr.hs.e_mirim.scaling.scheduler;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

public class Add extends Activity implements View.OnClickListener {

    DataBaseHelper dbh;
    Button startTime, endTime;
    int st, et,s,e;
    TimePicker time;
    EditText addtitle, place, memo;
    ImageButton ok;
    CheckBox days[] = new CheckBox[7];
    int daysbool[]=new int[7];
    ImageView colors[] = new ImageView[5];
    String colorname="null";
    int colorindex=-1;
    String indexs=new String();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.add);

        addtitle = (EditText) findViewById(R.id.addtitle);
        place = (EditText) findViewById(R.id.place);
        memo = (EditText) findViewById(R.id.memo);
        startTime = (Button) findViewById(R.id.startTime);
        endTime = (Button) findViewById(R.id.endTime);
        ok = (ImageButton) findViewById(R.id.ok);

        for (int i = 0; i < days.length; i++) {
            days[i] = (CheckBox) findViewById(R.id.day1 + i);
        }

        for (int i = 0; i < colors.length; i++) {
            colors[i] = (ImageView) findViewById(R.id.color1 + i);
            Imageset(i,1);
            colors[i].setOnClickListener(this);
        }

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.time, null);

                AlertDialog.Builder alt = new AlertDialog.Builder(Add.this);
                time = (TimePicker) view.findViewById(R.id.timepick);

                alt.setView(view);
                Toast.makeText(Add.this,"분에 상관없이 모든 시간은 1시간 단위로 일정이 저장됩니다.",Toast.LENGTH_LONG).show(); // 6시 ~ 24시 사이가 아니라면 일정표 시간 내 범주에서 데이터를 입력해달라고 토스트 생성
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

                AlertDialog.Builder alt = new AlertDialog.Builder(Add.this);
                time = (TimePicker) view.findViewById(R.id.timepick);

                alt.setView(view);
                Toast.makeText(Add.this,"분에 상관없이 모든 시간은 1시간 단위로 일정이 저장됩니다.",Toast.LENGTH_LONG).show();
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



                Intent intent = getIntent();
                int month = intent.getIntExtra("month", 0);//
                int week = intent.getIntExtra("week", 0);//


                for (int i = 0; i < daysbool.length; i++) {
                    if (days[i].isChecked()) daysbool[i] = 1;
                    else check++;
                }

                if((check==7)||(startTime.getText().equals("시작시간")||endTime.getText().equals("마침시간"))||(colorname.equals("null"))||(st > et)||(st>=1&&st<=5)||(et>=1&&et<=5)) {
                    if (check == 7) {
                        Toast.makeText(Add.this, "요일 중 하나라도 체크가 되어 있어야 합니다.", Toast.LENGTH_SHORT).show();
                        check = 0;
                    }

                    if (startTime.getText().equals("시작시간") || endTime.getText().equals("마침시간"))
                        Toast.makeText(Add.this, "반드시 시간설정을 해주세요.", Toast.LENGTH_SHORT).show();

                    if (colorname.equals("null"))
                        Toast.makeText(Add.this, "원하는 색 하나 선택해주세요.", Toast.LENGTH_SHORT).show();//


                    if (st > et) {
                        Toast.makeText(Add.this, "시작 시간이 마침 시간보다 큽니다.", Toast.LENGTH_SHORT).show();
                    }

                    if((st>=1&&st<=5)||(et>=1&&et<=5)){
                        Toast.makeText(Add.this, "시간은 06시부터 24시 사이여야만 합니다.", Toast.LENGTH_SHORT).show();
                    }
                }


                else {
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
                        s++;
                    }

                    Log.i("indexs",indexs);

                    dbh=new DataBaseHelper(Add.this);

                    SQLiteDatabase db = dbh.getWritableDatabase();//
                    db.execSQL("INSERT INTO addtable VALUES(null,'"+title+"','"+placetext+"',"+daysbool[0]+","+daysbool[1]+","+daysbool[2]+","+daysbool[3]+","+daysbool[4]+","+daysbool[5]+","+daysbool[6]+","
                    +st+","+et+",'"+indexs+"','"+colorname+"','"+memotext+"')");

                    Toast.makeText(Add.this,"일정 추가 성공",Toast.LENGTH_LONG).show();

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
            case R.id.color1:{
                colorname = "RED";
                colorindex = 0;
                break;}
            case R.id.color2:{
                colorname = "ORANGE";
                colorindex = 1;
                break;}
            case R.id.color3:{
                colorname = "YELLOW";
                colorindex = 2;
                break;}
            case R.id.color4:{
                colorname = "BLUE";
                colorindex = 3;
                break;}
            case R.id.color5:{
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