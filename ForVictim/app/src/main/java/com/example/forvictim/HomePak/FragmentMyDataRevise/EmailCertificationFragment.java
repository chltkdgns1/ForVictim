package com.example.forvictim.HomePak.FragmentMyDataRevise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.forvictim.GMailSender;
import com.example.forvictim.HomePak.HomeActivity;
import com.example.forvictim.HomePak.MyDataActivity;
import com.example.forvictim.R;
import com.example.forvictim.UserLog.LoginActivity;
import com.example.forvictim.UserLog.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class EmailCertificationFragment extends Fragment {

    // 데이터베이스

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    // 뷰
    private Context con;
    private View rootView;
    private Button Button_Email_Certification,Button_Email_Same_Check;
    private EditText EditText_Email_Checked;

    // 프레그먼트

    private MyDataReviseFragment myDataReviseFragment;

    // 이메일 아이디

    private String id,emailrecv,email;
    private int check = -1;


    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // 네이버 검색했다
                EditText_Email_Checked.setVisibility(View.VISIBLE);
                Button_Email_Same_Check.setVisibility(View.VISIBLE);
                Toast.makeText(con , "이메일로 인증번호가 전송되었습니다.", Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 2){
                Toast.makeText(con , "회원탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_send_email, container, false);
        init();
        return rootView;
    }

    private void init(){
        //                 getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_MyData,emailCertificationFragment).commit();

        myDataReviseFragment = new MyDataReviseFragment();

        id = ((MyDataActivity) getActivity()).id;

        check = ((MyDataActivity) getActivity()).check;

        con = ((MyDataActivity) getActivity());
        Button_Email_Certification = rootView.findViewById(R.id.Button_Email_Certification);
        EditText_Email_Checked = rootView.findViewById(R.id.EditText_Email_Checked);
        Button_Email_Same_Check = rootView.findViewById(R.id.Button_Email_Same_Check);

        EditText_Email_Checked.setVisibility(View.GONE);
        Button_Email_Same_Check.setVisibility(View.GONE);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();


        Button_Email_Certification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                synchronized (this) {
                    databaseReference.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserData data = snapshot.getValue(UserData.class);

                            email = data.getEmail();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        GMailSender gMailSender = new GMailSender("AppEmailSenders@gmail.com", "gkftndlTek12!");
                                        emailrecv = gMailSender.getEmailCode();
                                        gMailSender.sendMail("For 조난자 앱 이메일 인증번호입니다.", emailrecv, email);

                                        Message msg = Message.obtain();
                                        msg.what = 1;
                                        handlerPushMessage.sendMessage(msg);
                                    } catch (SendFailedException e) {
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
            }
        });

        Button_Email_Same_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = EditText_Email_Checked.getText().toString();

                if(str.isEmpty()){
                    Toast.makeText(con ,"인증번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                if(str.equals(emailrecv)){

                    Toast.makeText(con ,"인증되었습니다.",Toast.LENGTH_LONG).show();
                    EditText_Email_Checked.setVisibility(View.GONE);
                    Button_Email_Same_Check.setVisibility(View.GONE);

                    if(check == 1) {
                        ((MyDataActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_MyData,myDataReviseFragment).commit();
                    }
                    else{
                        databaseReference.child("users").child(id).removeValue();
                        final String token = email.replace("@", " ").replace(".", " ");
                        databaseReference.child("signuped").child(token).removeValue();

                        Intent intent = new Intent(con, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        ((MyDataActivity) getActivity()).finish();
                    }

                   // Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                   // intent.putExtra("id",data.getId());
                   // startActivity(intent);
                }
                else{
                    Toast.makeText(con,"올바른 인증번호가 아닙니다.",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });


    }
}
