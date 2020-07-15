package com.example.forvictim.HomePak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forvictim.R;
import com.example.forvictim.UserLog.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    // 뷰

    private TextView TextView_Home_My_Data,TextView_Home_Logout;

    // 아이디
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    private void init(){
        Intent it = getIntent();
        Bundle bun = it.getExtras();
        id = (String) bun.get("id");

        TextView_Home_My_Data = findViewById(R.id.TextView_Home_My_Data);
        TextView_Home_Logout = findViewById(R.id.TextView_Home_Logout);

        TextView_Home_Logout.setClickable(true);
        TextView_Home_My_Data.setClickable(true);

        TextView_Home_My_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyDataActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });



    }
}
