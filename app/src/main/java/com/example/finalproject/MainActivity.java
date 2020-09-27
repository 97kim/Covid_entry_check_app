package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RadioButton chkDisagree, chkAgree;
    Button btnForeignReset;
    EditText textName, textNation, textPhone, textTemp, textTwoweeks;

    private final String LOG_TAG = "LifeMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("입장 안내");

        chkDisagree = findViewById(R.id.radioDisagree);
        chkAgree = findViewById(R.id.radioAgree);

        btnForeignReset = findViewById(R.id.btnForeignReset);

        textName = findViewById(R.id.editName);
        textNation = findViewById(R.id.editNation);
        textPhone = findViewById(R.id.editPhone);
        textTemp = findViewById(R.id.editTemp);
        textTwoweeks = findViewById(R.id.editTwoweeks);

        chkAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chkAgree.isChecked() == true) { //라디오 버튼 '있음'이 선택되면 보이게 하고 텍스트를 공백으로 설정
                    textTwoweeks.setVisibility(View.VISIBLE);
                    btnForeignReset.setVisibility(View.VISIBLE);
                    textTwoweeks.setText("");
                }
            }
        });

        chkDisagree.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chkDisagree.isChecked() == true){ //라디오 버튼 '없음'이 선택되면 editText가 안 보이게 하고 텍스트를 "없음"으로 설정
                    textTwoweeks.setVisibility(View.INVISIBLE);
                    btnForeignReset.setVisibility(View.INVISIBLE);
                    textTwoweeks.setText("없음");
                }
            }
        }));
    }

    public void openSubActivity(View v) {
        Intent intent = new Intent(this, SubActivity.class);    //명시적 인텐트
        intent.putExtra("name", textName.getText().toString());         //입력한 이름을 서브액티비티로 전달
        intent.putExtra("nation", textNation.getText().toString());     //입력한 국적을 서브액티비티로 전달
        intent.putExtra("phone", textPhone.getText().toString());       //입력한 연락처를 서브액티비티로 전달
        intent.putExtra("temp", textTemp.getText().toString());         //입력한 체온을 서브액티비티로 전달
        intent.putExtra("foreign", textTwoweeks.getText().toString());   //2주 내로 해외 다녀온 여부를 서브액티비티로 전달
        startActivity(intent); //서브 액티비티 화면으로 이동

        Toast.makeText(this, "저장되었습니다!", Toast.LENGTH_LONG).show(); //토스트 메세지 출력
    }

    public void openThirdActivity(View v) {
        Intent intent = new Intent(this, ThirdActivity.class);    //명시적 인텐트
        startActivity(intent); //Third 액티비티 화면으로 이동
    }

    public void resetName(View v) { //온클릭 리셋함수(이름)
        String name = "";
        textName.setText(name);
    }

    public void resetNation(View v) { //온클릭 리셋함수(국적)
        String nation = "";
        textNation.setText(nation);
    }

    public void resetPhone(View v) { //온클릭 리셋함수(연락처)
        String phone = "";
        textPhone.setText(phone);
    }

    public void resetTemp(View v) { //온클릭 리셋함수(체온)
        String temp = "";
        textTemp.setText(temp);
    }

    public void resetForeign(View v) { //온클릭 리셋함수(2주 내로 해외 다녀온 여부)
        String foreign = "";
        textTwoweeks.setText(foreign);
    }

    // 라이프사이클 오버라이딩 - 로그 출력
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Main onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Main onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Main onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Main onRestart");
    }
}