package com.example.forvictim.UserLog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forvictim.HomePak.HomeActivity;
import com.example.forvictim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // 뷰

    private TextView Login_Signup,TextView_Login;
    private EditText EditText_Input_Id, EditText_Input_Pass;

    // 데이터베이스

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        EditText_Input_Id = findViewById(R.id.EditText_Input_Id);
        EditText_Input_Pass = findViewById(R.id.EditText_Input_Pass);
        TextView_Login = findViewById(R.id.TextView_Login);
        Login_Signup = findViewById(R.id.Login_Signup);
        Login_Signup.setClickable(true);
        TextView_Login.setClickable(true);

        TextView_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = EditText_Input_Id.getText().toString();
                final String pass = EditText_Input_Pass.getText().toString();

                EditText_Input_Id.setText("");
                EditText_Input_Pass.setText("");

                if(id.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"아이디를 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                if(pass.isEmpty()){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                databaseReference.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserData data = snapshot.getValue(UserData.class);

                        long t = snapshot.getChildrenCount();
                        if(t != 0){
                            String inpass = data.getPass();

                            if(!pass.equals(inpass)){
                                Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                                return;
                            }

                       //     databaseReference.child("Logined").child(data.getId()).setValue(1);
                            Toast.makeText(getApplicationContext(),"로그인되었습니다.",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("id",data.getId());
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"존재하지 않은 아이디입니다.",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });



            }
        });

        Login_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}