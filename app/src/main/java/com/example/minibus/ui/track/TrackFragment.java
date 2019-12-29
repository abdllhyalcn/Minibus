package com.example.minibus.ui.track;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.minibus.MainNavigationActivity;
import com.example.minibus.R;
import com.example.minibus.Utils;
import com.google.android.material.button.MaterialButton;
import com.skyfishjy.library.RippleBackground;


public class TrackFragment extends Fragment {

    private TrackViewModel trackViewModel;

    // UI elements.
    private MaterialButton mRequestLocationUpdatesButton;
    private RippleBackground rippleBackground;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trackViewModel =
                ViewModelProviders.of(this).get(TrackViewModel.class);
        View root = inflater.inflate(R.layout.fragment_track, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        mRequestLocationUpdatesButton = root.findViewById(R.id.request_location_updates_button);
        rippleBackground=root.findViewById(R.id.ripple_background);

        trackViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();


        mRequestLocationUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.requestingLocationUpdates(getContext())) {
                    ((MainNavigationActivity)getActivity()).StopLocationUpdates();
                    setButtonsState(false);
                } else {
                    ((MainNavigationActivity)getActivity()).RequestLocationUpdates();
                    setButtonsState(true);
                }

            }
        });

        // Restore the state of the buttons when the activity (re)launches.
        setButtonsState(Utils.requestingLocationUpdates(getContext()));
    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            mRequestLocationUpdatesButton.setIcon(getResources().getDrawable(R.drawable.ic_action_location_off));
            rippleBackground.startRippleAnimation();
        } else {
            mRequestLocationUpdatesButton.setIcon(getResources().getDrawable(R.drawable.ic_action_location_on));
            rippleBackground.stopRippleAnimation();
        }
    }


}