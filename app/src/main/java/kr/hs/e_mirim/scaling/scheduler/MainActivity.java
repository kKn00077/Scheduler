package kr.hs.e_mirim.scaling.scheduler; //position (그리드 레이아웃의 index값)을 이용해 데이터 저장 매번 해주는 식으로 가보기.

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.util.Calendar;

public class MainActivity extends Activity {

    GridView GridSchedule;
    ImageButton pre, add;
    TextView date1, date2;
    ScheduleAdapter adapter;
    Intent intent;

    int month, week, po;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        GridSchedule = (GridView) findViewById(R.id.schedule);
        pre = (ImageButton) findViewById(R.id.pre);
        add = (ImageButton) findViewById(R.id.add);
        date1=(TextView) findViewById(R.id.month);
        date2=(TextView) findViewById(R.id.week);

        Calendar cal=Calendar.getInstance();
        month=cal.get(Calendar.MONTH)+1;
        week=cal.get(Calendar.WEEK_OF_MONTH);

        date1.setText(month+"월");
        date2.setText(week+"주차");

        adapter = new ScheduleAdapter(this);

        GridSchedule.setAdapter(adapter);

        GridSchedule.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //   Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                intent=new Intent(MainActivity.this, Update.class);
                intent.putExtra("position","" +position);
                po=position;
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Preferences.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), Add.class);
                intent.putExtra("month",month);
                intent.putExtra("week",week);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ScheduleAdapter(this);
        GridSchedule.setAdapter(adapter);
    }
}
