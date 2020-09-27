package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SubActivity extends AppCompatActivity {
    TextView intentName, intentTemp, txt5, txt6, txt7, txt8;
    String intentNation, intentPhone, intentTwoweeks;
    RadioButton radio1, radio2;

    private final String LOG_TAG = "LifeSubActivity";

    int mode = Context.MODE_APPEND; // Context.MODE_APPEND로 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        intentName = findViewById(R.id.subName);
        intentTemp = findViewById(R.id.subTemp);
        txt5 = findViewById(R.id.text5);
        txt6 = findViewById(R.id.text6);
        txt7 = findViewById(R.id.text7);
        txt8 = findViewById(R.id.text8);
        radio1 = findViewById(R.id.radioPrivate);
        radio2 = findViewById(R.id.radioAppend);

        radio1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radio1.isChecked() == true) { //radio1 선택되면 Context.MODE_PRIVATE
                    mode = Context.MODE_PRIVATE;
                }
            }
        });

        radio2.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radio2.isChecked() == true){ //radio2 선택되면 Context.MODE_APPEND
                    mode = Context.MODE_APPEND;
                }
            }
        }));

        Intent intent = getIntent();
        intentName.setText(intent.getStringExtra("name")); //메인 액티비티에서 전달한 이름을 텍스트로 설정
        intentTemp.setText(intent.getStringExtra("temp")); //메인 액티비티에서 전달한 체온을 텍스트로 설정

        intentNation = (intent.getStringExtra("nation"));   //메인 액티비티에서 전달한 국적을 문자열 변수에 저장
        intentPhone = (intent.getStringExtra("phone"));     //메인 액티비티에서 전달한 연락처를 문자열 변수에 저장
        intentTwoweeks = (intent.getStringExtra("foreign")); //메인 액티비티에서 전달한 2주 내로 해외 다녀온 여부를 문자열 변수에 저장

        if(Double.parseDouble(intentTemp.getText().toString()) >= 37.5) { //체온이 37.5도 이상이면
            txt5.setVisibility(View.VISIBLE); //입장이 불가능하다는 텍스트를 화면에 표시
            txt6.setVisibility(View.VISIBLE);
        } else { //체온이 37.5 미만이면
            txt7.setVisibility(View.VISIBLE); //입장이 가능하다는 텍스트를 화면에 표시
            txt8.setVisibility(View.VISIBLE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //액션바에 홈(메인 액티비티)으로 돌아가기 버튼 사용
        getSupportActionBar().setTitle("안내"); //액션바에 뜨는 제목 설정
        Log.d(LOG_TAG,"-------");
        Log.d(LOG_TAG,"Sub onCreate");
    }

    public void BackBtnClick(View v) {
        finish(); //서브 액티비티를 종료해 이전 화면으로 돌아감
    }

    public void openThirdActivity(View v) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent); //Third 액티비티 화면으로 이동

        //LocalDateTime currentDateTime = LocalDateTime.now(); //컴퓨터의 현재 시간을 currentDateTime 변수에 저장
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")); //서울의 현재 시간을 currentDateTime 변수에 저장

        String tp = intentTemp.getText().toString();

        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("data.txt", mode))); //성능을 위해 버퍼에 저장해놨다가 한번에 출력 스트림에 반영
            bw.write(" ● 입장 시간: " + currentDateTime + "\r\n");
            bw.write(" ● 이름: " + intentName.getText() + "\r\n ● 국적: " + intentNation + "\r\n ● 연락처 :" + intentPhone + "\r\n ● 체온: " + intentTemp.getText() + "℃" + "\r\n ● 2주 내 해외 경험: " + intentTwoweeks + "\r\n");
            if(Double.parseDouble(tp) >= 37.5) { //체온이 37.5℃ 이상이면 입장 불가, 미만이면 입장 가능
                bw.write(" ▷▷▷ 입장 불가");
            } else {
                bw.write(" ▶▶▶ 입장 가능");
            }
            bw.write("\r\n-------------------------------------------------\r\n");
            bw.close(); //스트림에 있는 데이터를 내보내고 닫음(close()는 flush() 자동 호출)
        } catch(Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "완료되었습니다!", Toast.LENGTH_LONG).show();
    }

    public void shareBtnClick(View v) { //온클릭 공유 버튼
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = "[안내 메세지]\n코로나로 인하여 가능한 한 사회적 거리를 유지해주시고 마스크 착용을 필수화해 주시기 바랍니다.\n\n본 메세지 화면을 직원에게 보여주시면 감사하겠습니다!";
        intent.putExtra(Intent.EXTRA_TEXT, text);

        Intent chooser = Intent.createChooser(intent, "문자 전송");
        startActivity(chooser); //공유 화면 이동
    }

    // 라이프사이클 오버라이딩 - 로그 출력
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Sub onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Sub onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Sub onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Sub onRestart");
    }
}
