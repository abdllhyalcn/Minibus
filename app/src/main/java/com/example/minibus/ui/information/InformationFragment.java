package com.example.minibus.ui.information;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.minibus.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParsePush;

import org.json.JSONException;
import org.json.JSONObject;

// Imports to the JSONObject object, necessary for the push message
// Parse Dependencies
// Imports to the JSONObject object, necessary for the push message
// Parse Dependencies

public class InformationFragment extends Fragment {

    private InformationViewModel informationViewModel;

    private MaterialButton submit;
    private RadioGroup radiogroup;
    private RadioButton ozel;
    private TextInputEditText ozeledittext;
    private TextInputLayout ozeleditlayout;

    private String SelectedText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState) {
        informationViewModel =
                ViewModelProviders.of(this).get(InformationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_information, container, false);

        submit =root.findViewById(R.id.submit);
        radiogroup= root.findViewById(R.id.radiogroup);
        ozel=root.findViewById(R.id.ozel);
        ozeledittext=root.findViewById(R.id.custom_notfy_edit_text);
        ozeleditlayout=root.findViewById(R.id.custom_notfy_input);







        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioButton temp=radiogroup.findViewById(radiogroup.getCheckedRadioButtonId());
        SelectedText=temp.getText().toString();




        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton temp=radiogroup.findViewById(checkedId);
                SelectedText=temp.getText().toString();
            }
        });

        ozel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ozeleditlayout.setVisibility(View.VISIBLE);
                }else{
                    ozeleditlayout.setVisibility(View.GONE);
                    ozeledittext.setText(null);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radiogroup.getCheckedRadioButtonId()==R.id.ozel){
                    if(ozeledittext.getText().length()==0){
                        ozeledittext.setError(getString(R.string.minimum_setting));
                        return;
                    }
                    SelectedText=ozeledittext.getText().toString();
                }
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Emin misiniz?")
                        .setMessage("Bildirim tüm kullanıcılara gönderilecektir.")
                        .setPositiveButton("Gönder",alertClickListener)
                        .setNegativeButton("İptal",null)
                        .show();
            }
        });
    }

    private DialogInterface.OnClickListener alertClickListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            pushNotification("Bilgilendirme",SelectedText);
        }
    };

    private void pushNotification(String Title,String Text){
        JSONObject data = new JSONObject();
        // Put data in the JSON object
        try {
            data.put("alert", Title);
            data.put("title", Text);
        } catch ( JSONException e) {
            // should not happen
            throw new IllegalArgumentException("unexpected parsing error", e);
        }
        // Configure the push
        ParsePush push = new ParsePush();
        push.setChannel("Everyone");
        push.setData(data);
        push.sendInBackground();
    }
}