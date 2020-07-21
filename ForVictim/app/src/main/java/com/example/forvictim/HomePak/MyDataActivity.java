package com.example.forvictim.HomePak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forvictim.HomePak.FragmentMyData.MyDataFragment;
import com.example.forvictim.HomePak.FragmentMyDataRevise.EmailCertificationFragment;
import com.example.forvictim.HomePak.FragmentMyDataRevise.MyDataReviseFragment;
import com.example.forvictim.R;

public class MyDataActivity extends AppCompatActivity {

    private TextView TextView_My_Data,TextView_My_Data_Revise,TextView_My_Data_Out;


    // 프레그먼트

    private EmailCertificationFragment emailCertificationFragment;
    private MyDataFragment myDataFragment;
    private MyDataReviseFragment myDataReviseFragment;


    // 아이디, 회원정보 수정 탈퇴
    public String id;
    public int check = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydata);

        init();
    }

    private void init(){
        Intent it = getIntent();
        Bundle bun = it.getExtras();
        id = (String) bun.get("id");

        myDataFragment = new MyDataFragment();
        myDataReviseFragment = new MyDataReviseFragment();
        emailCertificationFragment= new EmailCertificationFragment();

        TextView_My_Data = findViewById(R.id.TextView_My_Data);
        TextView_My_Data_Revise = findViewById(R.id.TextView_My_Data_Revise);
        TextView_My_Data_Out = findViewById(R.id.TextView_My_Data_Out);

        TextView_My_Data_Out.setClickable(true);
        TextView_My_Data_Revise.setClickable(true);
        TextView_My_Data.setClickable(true);

        //    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_make_map, mapFragment).commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_MyData,myDataFragment ).commit();

        TextView_My_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_MyData,myDataFragment ).commit();
            }
        });

        TextView_My_Data_Revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 1;
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_MyData,emailCertificationFragment).commit();
            }
        });

        TextView_My_Data_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 2;
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_MyData,emailCertificationFragment).commit();
            }
        });

    }

}
