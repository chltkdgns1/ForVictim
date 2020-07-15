package com.example.forvictim.HomePak.FragmentMyDataRevise;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.forvictim.R;

public class MyDataReviseFragment extends Fragment {
    private Context con;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_fragment_mydata_revise, container, false);
        init();
        return rootView;
    }

    private void init(){
        con = getActivity();
    }
}
