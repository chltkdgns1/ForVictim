package com.example.forvictim.HomePak.FragmentMyData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MyDataFragment extends Fragment{
    private Context con;
    private View rootView;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    // 액티비티

    private Activity act;

    // 뷰

    private TextView TextView_My_Data_Name,TextView_My_Data_Sx,TextView_My_Data_Age;
    private TextView TextView_My_Data_date,TextView_My_Data_Phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_fragment_mydata, container, false);
        init();
        return rootView;
    }

    private void init(){
        TextView_My_Data_Name = rootView.findViewById(R.id.TextView_My_Data_Name);
        TextView_My_Data_Sx = rootView.findViewById(R.id.TextView_My_Data_Sx);
        TextView_My_Data_Age = rootView.findViewById(R.id.TextView_My_Data_Age);
        TextView_My_Data_date = rootView.findViewById(R.id.TextView_My_Data_date);
        TextView_My_Data_Phone = rootView.findViewById(R.id.TextView_My_Data_Phone);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        con = getActivity();
        act = (MyDataActivity)getActivity();

        databaseReference.child("users").child(((MyDataActivity) act).id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData data = snapshot.getValue(UserData.class);

                long t = snapshot.getChildrenCount();
                if(t != 0){
                    TextView_My_Data_Name.setText(data.getName());




                }
                else{
                    Toast.makeText(con,"에러입니다",Toast.LENGTH_LONG).show();
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
}
