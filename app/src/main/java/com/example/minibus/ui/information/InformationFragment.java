package com.example.minibus.ui.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.minibus.R;
import com.parse.Parse;
import com.parse.ParseInstallation;
// Imports to the JSONObject object, necessary for the push message
import org.json.JSONException;
import org.json.JSONObject;
// Parse Dependencies
import com.parse.ParsePush;

import java.util.ArrayList;

public class InformationFragment extends Fragment {

    private InformationViewModel informationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        informationViewModel =
                ViewModelProviders.of(this).get(InformationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_information, container, false);
       // final TextView textView = root.findViewById(R.id.text_notifications);
        informationViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });



        return root;
    }
}