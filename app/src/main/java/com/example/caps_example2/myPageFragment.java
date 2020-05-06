package com.example.caps_example2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class myPageFragment extends Fragment {

    // Firebase
    private FirebaseAuth mAuth = null;

    // private String

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        mAuth = FirebaseAuth.getInstance();
//        firestore = FirebaseFirestore.getInstance();


        return view;


    }
}
