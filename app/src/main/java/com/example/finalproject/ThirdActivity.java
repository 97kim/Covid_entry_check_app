package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ThirdActivity extends AppCompatActivity {

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    TextView txtNumber, menuResult, quantityResult;
    Spinner spinner;
    String spinnertxt;
    EditText edtQuantity;
    Button btnInit, btnInsert, btnSelect, btnUpdate, btnDelete, btnOK;

    private final String LOG_TAG = "LifeThirdActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        spinner = findViewById(R.id.spinner);
        edtQuantity = findViewById(R.id.editQuantity);

        txtNumber = findViewById(R.id.textNum);
        menuResult = findViewById(R.id.txtMenuResult);
        quantityResult = findViewById(R.id.txtQuantityResult);

        btnInit = findViewById(R.id.btnInit);
        btnInsert = findViewById(R.id.btnInsert);
        btnSelect = findViewById(R.id.btnSelect);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnOK = findViewById(R.id.btnOK);

        //스피너에 들어갈 item을 simple_spinner_item 레이아웃 형태로 표현
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sppiner_entries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 드랍다운 되었을 때 보여지는 뷰에서 사용될 레이아웃 별도 설정
        spinner.setAdapter(adapter); //스피너에 위에서 설정한 어댑터 연결
        spinner.setPrompt("메뉴 선택"); //다이얼로그 형태 스피너의 제목 설정

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //스피너 item이 선택되었을 때 설정
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //선택되었을 때
                spinnertxt = spinner.getSelectedItem().toString(); //스피너에서 선택된 값을 String 변수에 저장
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {} //선택 안 됐을 때
    });

        myHelper = new myDBHelper((this));
        btnInit.setOnClickListener(new View.OnClickListener() { //초기화 버튼 클릭했을 때 설정
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase(); //읽기, 쓰기 가능
                myHelper.onUpgrade(sqlDB, 1, 2);

                btnSelect.callOnClick(); //조회 버튼의 onClickListener 이벤트 호출
                sqlDB.close();

                Toast.makeText(getApplicationContext(), "초기화!", Toast.LENGTH_SHORT).show(); //토스트 메세지 출력
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() { //입력 버튼 클릭했을 때 설정
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase(); //읽기, 쓰기 가능
                sqlDB.execSQL("INSERT INTO groupOrder(gMenu, gQuantity) VALUES (?,?);", new String[]{spinnertxt, edtQuantity.getText().toString()});
                //테이블의 두 항목에 데이터 추가
                btnSelect.callOnClick(); //조회 버튼의 onClickListener 이벤트 호출
                sqlDB.close();

                Toast.makeText(getApplicationContext(), "입력!", Toast.LENGTH_SHORT).show(); //토스트 메세지 출력
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase(); //읽기, 쓰기 가능
                sqlDB.execSQL("UPDATE groupOrder SET gQuantity=? WHERE gMenu=?;", new String[]{edtQuantity.getText().toString(), spinnertxt});
                //선택된 메뉴의 수량을 edittext에 입력한 수량으로 수정
                btnSelect.callOnClick(); //조회 버튼의 onClickListener 이벤트 호출
                sqlDB.close();

                Toast.makeText(getApplicationContext(), "수정!", Toast.LENGTH_SHORT).show(); //토스트 메세지 출력
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase(); //읽기, 쓰기 가능
                sqlDB.execSQL("DELETE FROM groupOrder WHERE gMenu=?;", new String[]{spinnertxt});
                //선택된 메뉴가 해당된 행 삭제
                btnSelect.callOnClick(); //조회 버튼의 onClickListener 이벤트 호출
                sqlDB.close();

                Toast.makeText(getApplicationContext(), "삭제!", Toast.LENGTH_SHORT).show(); //토스트 메세지 출력
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sqlDB = myHelper.getReadableDatabase(); //읽기만 가능

                Cursor cursor; //cursor 객체는 쿼리에 의하여 생성된 행들을 가리킨다.
                cursor = sqlDB.rawQuery("SELECT * FROM groupOrder;", null);
                //groupOrder 테이블 내 데이터 조회, SELECT는 rawQuery 사용
                String strNum = "번호" + "\r\n" + "-----------" + "\r\n"; //윈도우에서 줄바꿈 -> \r\n
                String strMenus = "메뉴" + "\r\n" + "-----------" + "\r\n";
                String strQuantities = "수량" + "\r\n" + "-----------" + "\r\n";

                while(cursor.moveToNext()) { //커서가 다음 행으로 이동하면 반복 종료
                    strNum += cursor.getString(0) + "\r\n";        //0열의 데이터를 가져와 변수에 저장
                    strMenus += cursor.getString(1) + "\r\n";      //1열의 데이터를 가져와 변수에 저장
                    strQuantities += cursor.getString(2) + "\r\n"; //2열의 데이터를 가져와 변수에 저장
                }

                txtNumber.setText(strNum);
                menuResult.setText(strMenus);
                quantityResult.setText(strQuantities); //텍스트뷰에 위에서 저장된 변수 표시

                cursor.close();
                sqlDB.close();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //액션바 홈(메인 액티비티)으로 버튼 사용
        getSupportActionBar().setTitle("주문"); //액션바에 뜨는 제목 설정
        Log.d(LOG_TAG,"-------");
        Log.d(LOG_TAG,"Third onCreate");
    }

    public void BackBtnClick(View v) {
        finish(); //Third 액티비티를 종료해 이전 화면으로 돌아감
    }

    public void openMainActivity(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); //메인 액티비티 화면으로 이동
        Toast.makeText(this, "완료되었습니다!", Toast.LENGTH_LONG).show();
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupOrder (Num INTEGER PRIMARY KEY AUTOINCREMENT, gMenu TEXT, gQuantity INTEGER);");
        } // groupOrder(번호, 메뉴, 수량) 테이블 생성, 기본키인 번호는 자동으로 1씩 증가
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupOrder");
            onCreate(db);
        } //버전이 높아지면 기존의 테이블을 지운 후 테이블 새로 생성
    }

    // 라이프사이클 오버라이딩 - 로그 출력
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Third onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Third onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Third onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "------");
        Log.d(LOG_TAG,"Third onRestart");
    }

}
