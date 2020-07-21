package com.example.forvictim.HomePak.FragmentMyDataRevise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.List;

public class MyDataReviseFragment extends Fragment {
    private Context con;
    private View rootView;

    // 스피너 데이터
    private List<String> monDate, dayDate;

    // 스패닝 어뎁터

    private ArrayAdapter<String> monAdaptor, dayAdaptor;
    //날짜

    private int day,month;

    // 데이터 베이스

    private int sx = 0;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private Spinner Spinner_Withdrawal_Month, Spinner_Withdrawal_Day;
    private TextView TextView_Withdrawal_Man,TextView_Withdrawal_Girl,TextView_Withdrawal_Complete;
    private EditText EditText_Withdrawal_Pass,EditText_Withdrawal_Pass_Again;
    private EditText EditText_Withdrawal_Name,EditText_Withdrawal_Year,EditText_Withdrawal_Phone;

    private UserData data;
    //Withdrawal
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_fragment_mydata_revise, container, false);
        init();
        return rootView;
    }

    private void init(){

        con = getActivity();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        TextView_Withdrawal_Man = rootView.findViewById(R.id.TextView_Withdrawal_Man);
        TextView_Withdrawal_Girl = rootView.findViewById(R.id.TextView_Withdrawal_Girl);
        TextView_Withdrawal_Complete = rootView.findViewById(R.id.TextView_Withdrawal_Complete);

        TextView_Withdrawal_Man.setClickable(true);
        TextView_Withdrawal_Girl.setClickable(true);
        TextView_Withdrawal_Complete.setClickable(true);

        EditText_Withdrawal_Pass = rootView.findViewById(R.id.EditText_Withdrawal_Pass);
        EditText_Withdrawal_Pass_Again = rootView.findViewById(R.id.EditText_Withdrawal_Pass_Again);
        EditText_Withdrawal_Name = rootView.findViewById(R.id.EditText_Withdrawal_Name);
        EditText_Withdrawal_Year = rootView.findViewById(R.id.EditText_Withdrawal_Year );
        EditText_Withdrawal_Phone = rootView.findViewById(R.id.EditText_Withdrawal_Phone);


        Spinner_Withdrawal_Month = rootView.findViewById(R.id.Spinner_Withdrawal_Month);
        Spinner_Withdrawal_Day = rootView.findViewById(R.id.Spinner_Withdrawal_Day);

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

        monAdaptor = new ArrayAdapter<String>(con,android.R.layout.simple_list_item_1,monDate);
        Spinner_Withdrawal_Month.setAdapter(monAdaptor);

        dayAdaptor = new ArrayAdapter<String>(con,android.R.layout.simple_list_item_1,dayDate);
        Spinner_Withdrawal_Day.setAdapter(dayAdaptor);

        Spinner_Withdrawal_Month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = (position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner_Withdrawal_Day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = (position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView_Withdrawal_Man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sx = 0;
            }
        });

        TextView_Withdrawal_Girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sx = 1;
            }
        });

        databaseReference.child("users").child(((MyDataActivity)getActivity()).id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data = snapshot.getValue(UserData.class);

                long t = snapshot.getChildrenCount();
                if(t != 0){
                    EditText_Withdrawal_Name.setText(data.getName());
                    EditText_Withdrawal_Year.setText(data.getYear());
                    EditText_Withdrawal_Phone.setText(data.getPhone());
                    day = data.getDay(); month = data.getMonth();
                    sx = data.getSx();
                }
                else{
                    Toast.makeText(con,"에러",Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });


        TextView_Withdrawal_Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = EditText_Withdrawal_Pass.getText().toString();
                String passAgain = EditText_Withdrawal_Pass_Again.getText().toString();

                if(!pass.isEmpty() && pass.equals(passAgain)){
                    data.setPass(pass);
                }

                data.setSx(sx);
                data.setPhone(EditText_Withdrawal_Phone.getText().toString());
                data.setYear(EditText_Withdrawal_Year.getText().toString());
                data.setName(EditText_Withdrawal_Name.getText().toString());
                data.setMonth(month); data.setDay(day);

                databaseReference.child("users").child(data.getId()).setValue(data);

                Toast.makeText(con,"정보수정이 완료되었습니다.",Toast.LENGTH_LONG).show();
            }
        });

    }
}
