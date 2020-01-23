package com.example.minibus.ui.report;

import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.minibus.R;
import com.example.minibus.Report;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ReportFragment extends Fragment {

    private static final String TAG = ReportFragment.class.getSimpleName();

    private List<Report> reportList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReportsAdapter mAdapter;
    private Spinner spinner;
    private MaterialButton secim_btn;
    int itempos;
    long datefark;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_report, container, false);

        secim_btn=root.findViewById(R.id.secim_button);
        spinner=root.findViewById(R.id.spinner1);

        recyclerView=root.findViewById(R.id.recycler_view);

        mAdapter=new ReportsAdapter(reportList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itempos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itempos=0;
            }
        });

        secim_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(itempos){
                    case 0:
                        datefark=System.currentTimeMillis()-86400000L;
                    case 1:
                        datefark=System.currentTimeMillis()-604800000L;
                        break;
                    case 2:
                        datefark=System.currentTimeMillis()-1296000000L;
                        break;
                    case 3:
                        datefark=System.currentTimeMillis()-259200000L;
                        break;
                    case 4:
                        datefark=0;
                        break;
                }
                reportList.clear();
                prepareData();
            }
        });

    }

    private void prepareData(){
        /*Firebase Inıtıalizations
         * */
        String authEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail().
                substring(0 , FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf('@'));

        String path = getString(R.string.firebase_report_path) ;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path).child(authEmail);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Report report=dataSnapshot.getValue(Report.class);
                if(report.getStartDate()>=datefark){
                    reportList.add(0,report);
                    mAdapter.notifyDataSetChanged();
                }



                //String time= DateFormat.getDateTimeInstance().format(report.getStartDate());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toException().toString());
            }
        });


    }
}
