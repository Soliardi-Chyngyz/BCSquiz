package com.example.bcsquiz.ThirdActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bcsquiz.GameActivity;
import com.example.bcsquiz.MainActivity;
import com.example.bcsquiz.R;
import com.example.bcsquiz.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment {
    private List<Users> list = new ArrayList<>();
    private AdapterOfThirdActivity adapter = new AdapterOfThirdActivity(list);
    private FirebaseFirestore fb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fb = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        fb
                .collection("Users")
                .orderBy("points", Query.Direction.valueOf("desc"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List <Users> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            }
                            adapter.updateList(list);
                        } else {
                            Log.e("TAG", "onComplete: FAILED");
                        }
                    }
                });

        Button button = view.findViewById(R.id.btn_third);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThirdActivity.start(getContext());
            }
        });
    }
}