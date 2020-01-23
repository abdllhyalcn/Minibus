package com.example.minibus.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.minibus.LoginActivity;
import com.example.minibus.MainNavigationActivity;
import com.example.minibus.R;
import com.example.minibus.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsFragment extends Fragment {

    private static final String TAG = MainNavigationActivity.class.getSimpleName();

    private MaterialButton btn_surucu, btn_sifre, btn_plaka;
    private MaterialButton btn_surucu_no, btn_sifre_no, btn_plaka_no;
    private TextInputEditText txt_surucu, txt_sifre, txt_plaka;
    private TextInputLayout txt_surucu_lyt, txt_sifre_lyt, txt_plaka_lyt;
    private TextView plaka, surucu_id, plaka_id;
    private ProgressBar progressBar;
    private MaterialButton btn_sign_out;
    private TextView adres_text;

    private FirebaseUser user;

    boolean surucu_enable, sifre_enable, plaka_enable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        componentInitialize(root);

        user = FirebaseAuth.getInstance().getCurrentUser();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        plaka.setText(user.getEmail().substring(0,user.getEmail().indexOf('@')));
        surucu_id.setText(user.getDisplayName());
        plaka_id.setText(user.getEmail().substring(0,user.getEmail().indexOf('@')));

    }

    private void updateSurucu(){
        final String surucu=txt_surucu.getText().toString();

        if(surucu.length()<1){
            txt_surucu.setError(getString(R.string.minimum_setting));
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(surucu).build();
        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if(!task.isSuccessful()){
                    Toast.makeText(getContext(), R.string.surucu_error, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),R.string.surucu_complete,Toast.LENGTH_LONG).show();
                    switchEditing(0,R.drawable.ic_action_arrange,View.GONE, false);
                    surucu_id.setText(user.getDisplayName());
                }
            }
        });
    }

    private void updatePlaka(){
        final String txtplaka=txt_plaka.getText().toString()+"@minibusdomain.com";

        if(txtplaka.length()==18){
            txt_plaka.setError(getString(R.string.minimum_setting));
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        user.updateEmail(txtplaka).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if(!task.isSuccessful()){
                    Toast.makeText(getContext(), R.string.plaka_error, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),R.string.plaka_complete,Toast.LENGTH_LONG).show();
                    switchEditing(1,R.drawable.ic_action_arrange,View.GONE, false);

                    plaka_id.setText(user.getEmail().substring(0,user.getEmail().indexOf('@')));
                    plaka.setText(user.getEmail().substring(0,user.getEmail().indexOf('@')));
                }
            }
        });
    }

    private void updatePassword(){

        final String password=txt_sifre.getText().toString();

        if(password.length()<5){
            txt_sifre.setError(getString(R.string.minimum_password));
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if(!task.isSuccessful()){
                    Toast.makeText(getContext(), R.string.sifre_error, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),R.string.sifre_complete,Toast.LENGTH_LONG).show();
                    switchEditing(2,R.drawable.ic_action_arrange,View.GONE, false);
                }
            }
        });
    }

    private boolean componentInitialize(View root){
        try {
            btn_surucu = root.findViewById(R.id.btn_surucu);
            btn_sifre = root.findViewById(R.id.btn_sifre);
            btn_plaka = root.findViewById(R.id.btn_plaka);

            btn_surucu_no = root.findViewById(R.id.btn_surucu_no);
            btn_sifre_no = root.findViewById(R.id.btn_sifre_no);
            btn_plaka_no = root.findViewById(R.id.btn_plaka_no);

            txt_surucu = root.findViewById(R.id.edittext_surucu);
            txt_sifre = root.findViewById(R.id.edittext_sifre);
            txt_plaka = root.findViewById(R.id.edittext_plaka);

            txt_surucu_lyt=root.findViewById(R.id.layout_surucu);
            txt_sifre_lyt=root.findViewById(R.id.layout_sifre);
            txt_plaka_lyt=root.findViewById(R.id.layout_plaka);

            progressBar=root.findViewById(R.id.progressBar);

            btn_sign_out=root.findViewById(R.id.btn_sign_out);

            plaka=root.findViewById(R.id.plaka_textview);
            surucu_id=root.findViewById(R.id.surucu_id);
            plaka_id =root.findViewById(R.id.plaka_id);

            btn_surucu.setOnClickListener(defOnClickListener);
            btn_sifre.setOnClickListener(defOnClickListener);
            btn_plaka.setOnClickListener(defOnClickListener);

            btn_surucu_no.setOnClickListener(closeOnClickListener);
            btn_plaka_no.setOnClickListener(closeOnClickListener);
            btn_sifre_no.setOnClickListener(closeOnClickListener);

            return true;
        }catch (Exception e){
            Log.w(TAG,e);
            return false;
        }
    }

    private View.OnClickListener defOnClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==btn_surucu.getId()){
                if(!surucu_enable){
                    /**
                     * First Click to open editing place.
                     */
                    switchEditing(0,R.drawable.ic_action_confirm,View.VISIBLE, true);
                }else{
                    /**
                     * Second Click to checkout editing.
                     */
                    updateSurucu();

                }
            }else if(v.getId()== btn_plaka.getId()){
                if(!plaka_enable){
                    switchEditing(1,R.drawable.ic_action_confirm,View.VISIBLE, true);
                }else{
                  updatePlaka();
                }
            }else if(v.getId()==btn_sifre.getId()){
                if(!sifre_enable){
                    switchEditing(2,R.drawable.ic_action_confirm,View.VISIBLE, true);
                }else{

                    updatePassword();
                }

            }
        }
    };

    private View.OnClickListener closeOnClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==btn_surucu_no.getId()){
                switchEditing(0,R.drawable.ic_action_arrange,View.GONE, false);
            }else if(v.getId()== btn_plaka_no.getId()){
                switchEditing(1,R.drawable.ic_action_arrange,View.GONE, false);
            }else if(v.getId()==btn_sifre_no.getId()){
                switchEditing(2,R.drawable.ic_action_arrange,View.GONE, false);
            }
        }
    };

    private void switchEditing(int place, int btnDrawableRes, int visibility, boolean checkOut){
        switch (place){
            case 0:
                btn_surucu.setIconResource(btnDrawableRes);
                txt_surucu_lyt.setVisibility(visibility);
                btn_surucu_no.setVisibility(visibility);
                txt_surucu.setText(null);
                surucu_enable=checkOut;

                return;
            case 1:
                btn_plaka.setIconResource(btnDrawableRes);
                txt_plaka_lyt.setVisibility(visibility);
                btn_plaka_no.setVisibility(visibility);
                txt_plaka.setText(null);
                plaka_enable =checkOut;
                return;
            case 2:
                btn_sifre.setIconResource(btnDrawableRes);
                txt_sifre_lyt.setVisibility(visibility);
                btn_sifre_no.setVisibility(visibility);
                txt_sifre.setText(null);
                sifre_enable=checkOut;
                return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    Intent intent=new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }
        });

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.requestingLocationUpdates(getContext())){
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("Konum Alınıyor")
                            .setMessage("Konum almayı durdurmalısınız!")
                            .setPositiveButton("Tamam", null)
                            .show();
                    return;
                }
                FirebaseAuth.getInstance().signOut();
            }
        });
    }
}
