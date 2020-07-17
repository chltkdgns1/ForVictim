package com.example.forvictim.UserLog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forvictim.GMailSender;
import com.example.forvictim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SignUpActivity extends AppCompatActivity {


    // 스피너 데이터
    private List<String> monDate, dayDate;

    // 스패닝 어뎁터

    private ArrayAdapter<String> monAdaptor, dayAdaptor;

    // 스패닝 클릭된 값

    private int day = 1, month = 1;
    private int sx = -1;

    // 아이디 중복 체크

    private boolean check = false;

    // 뷰
    private Spinner Spinner_Month, Spinner_Day;
    private TextView TextView_Man,TextView_Girl,TextView_Complete,TextView_Id_Overlap,TextView_Email_Overlap;
    private EditText EditText_Id,EditText_Pass,EditText_Pass_Again;
    private EditText EditText_Name,EditText_Year,EditText_Phone,EditText_Email,EditText_Email_Check;

    // 데이터 베이스

    private String emailrev = "";
    private boolean mailcheck = false; // 이메일 인증이 확인되었는지 확인

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init() {

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        TextView_Email_Overlap = findViewById(R.id.TextView_Email_Overlap);
        TextView_Id_Overlap = findViewById(R.id.TextView_Id_Overlap);
        TextView_Man = findViewById(R.id.TextView_Man);
        TextView_Girl = findViewById(R.id.TextView_Girl);
        TextView_Complete = findViewById(R.id.TextView_Complete);

        TextView_Man.setClickable(true);
        TextView_Girl.setClickable(true);
        TextView_Complete.setClickable(true);
        TextView_Id_Overlap.setClickable(true);
        TextView_Email_Overlap.setClickable(true);

        EditText_Id = findViewById(R.id.EditText_Id);
        EditText_Pass = findViewById(R.id.EditText_Pass);
        EditText_Pass_Again = findViewById(R.id.EditText_Pass_Again);
        EditText_Name = findViewById(R.id.EditText_Name);
        EditText_Year = findViewById(R.id.EditText_Year );
        EditText_Phone = findViewById(R.id.EditText_Phone);
        EditText_Email = findViewById(R.id.EditText_Email);
        EditText_Email_Check = findViewById(R.id.EditText_Email_Check);

        TextView_Man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sx = 0;
            }
        });

        TextView_Girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sx = 1;
            }
        });

        TextView_Email_Overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = EditText_Email.getText().toString();

                System.out.println("클릭됬음?ㅇㅁㄴㅇㅁㅁㅇㅁㅇㅁㅇㅁㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ");
                if(email.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"이메일을 입력해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                final String token = email.replace("@", " ").replace(".", " ");
                databaseReference.child("signuped").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(getApplicationContext(),"이미 등록된 이메일 번호입니다.",Toast.LENGTH_LONG).show();
                            return;
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    GMailSender gMailSender = new GMailSender("AppEmailSenders@gmail.com", "gkftndlTek12!");
                                    emailrev = gMailSender.getEmailCode();
                                    gMailSender.sendMail("For 조난자 앱 이메일 인증번호입니다.", emailrev, email);
                                    Toast.makeText(getApplicationContext(), "이메일로 인증번호가 전송되었습니다.", Toast.LENGTH_LONG).show();
                                }
                                catch (SendFailedException e) {
                                    //Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                } catch (MessagingException e) {
                                    //Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    //Toast.makeText(getApplicationContext(), "에러에러", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });

            }
        });

        TextView_Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = EditText_Id.getText().toString();
                String pass = EditText_Pass.getText().toString();
                String passAgain = EditText_Pass_Again.getText().toString();
                String name = EditText_Name.getText().toString();
                String year = EditText_Year.getText().toString();
                String phone = EditText_Phone.getText().toString();
                String email = EditText_Email.getText().toString();

                if(id.isEmpty()){
                    Toast.makeText(getApplicationContext(),"아이디를 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!check){
                    Toast.makeText(getApplicationContext(),"아이디 중복체크 해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                if(pass.isEmpty() || passAgain.isEmpty()){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!pass.equals(passAgain)){
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                    return;
                }

                if(name.isEmpty()){
                    Toast.makeText(getApplicationContext(),"이름을 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                if(year.isEmpty()){
                    Toast.makeText(getApplicationContext(),"생년을 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                if(day == -1){
                    Toast.makeText(getApplicationContext(),"생월을 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                if(month == -1){
                    Toast.makeText(getApplicationContext(),"생일을 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                if(phone.isEmpty()){
                    Toast.makeText(getApplicationContext(),"휴대전화번호를 입력해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                if(email.isEmpty()){
                    Toast.makeText(getApplicationContext(),"이메일을 입력해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!emailrev.equals(EditText_Email_Check.getText().toString())){
                    Toast.makeText(getApplicationContext(),"이메일을 인증해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                UserData data = new UserData();
                data.setId(id);
                data.setName(name);
                data.setYear(year);
                data.setPhone(phone);
                data.setDay(day);
                data.setMonth(month);
                data.setPass(pass);
                data.setSx(sx);
                data.setEmail(email);

                System.out.println("년 : " + year);
                System.out.println("월 : " + month);
                System.out.println("일 : " + day);

                databaseReference.child("users").child(id).setValue(data);
                final String token = email.replace("@", " ").replace(".", " ");
                databaseReference.child("signuped").child(token).setValue(1);

                Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        TextView_Id_Overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = EditText_Id.getText().toString();

                databaseReference.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       long t = snapshot.getChildrenCount();
                       if(t != 0) {
                           Toast.makeText(getApplicationContext(),"중복된 아이디가 존재합니다.",Toast.LENGTH_LONG).show();
                       }
                       else {
                           check =true;
                           Toast.makeText(getApplicationContext(),"중복 확인되었습니다.",Toast.LENGTH_LONG).show();
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


        Spinner_Month = findViewById(R.id.Spinner_Month);
        Spinner_Day = findViewById(R.id.Spinner_Day);

        monDate = new ArrayList<>();
        dayDate = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            String date = Integer.toString(i) + "월";
            monDate.add(date);
        }

        for (int i = 1; i <= 31; i++) {
            String date = Integer.toString(i) + "일";
            dayDate.add(date);
        }

        monAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,monDate);
        Spinner_Month.setAdapter(monAdaptor );

        dayAdaptor = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dayDate);
        Spinner_Day.setAdapter(dayAdaptor);


        Spinner_Month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = (position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner_Day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = (position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
