package com.example.scaf.ui.account;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.scaf.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private PopupWindow mPopupWindow;
    private ConstraintLayout mConstraintLayout;
    private Button edit_btn, update_btn;
    private View customView;
    private TextView petNameTV, petGenderTV, petAgeTV, petWeightTV;
    private static final String TAG = "AccountFragment";

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Log.d(TAG, "Test on account fragment");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address
            String name = user.getDisplayName();
            String email = user.getEmail();

            for (UserInfo userInfo : user.getProviderData()) {
                if (name == null && userInfo.getDisplayName() != null) {
                    name = userInfo.getDisplayName();
                }
            }

            TextView useremail = (TextView)view.findViewById(R.id.ac_useremail);
            String e = "Email: " + email;
            useremail.setText(e);

            TextView username = (TextView)view.findViewById(R.id.ac_username);
            String n = "Username: " + name;
            username.setText(n);
        }

        edit_btn = view.findViewById(R.id.editbutton);
        mConstraintLayout = view.findViewById(R.id.account);
        petNameTV = view.findViewById(R.id.petname);
        petGenderTV = view.findViewById(R.id.petgender);
        petAgeTV = view.findViewById(R.id.petage);
        petWeightTV = view.findViewById(R.id.petweight);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                customView = inflater.inflate(R.layout.account_edit,null);
                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true
                );
                mPopupWindow.showAtLocation(mConstraintLayout, Gravity.CENTER,0,0);
                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                update_btn = customView.findViewById(R.id.edit_btn);

                update_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText petNameTextView = customView.findViewById(R.id.edit_pet_name);
                        EditText petAgeTextView = customView.findViewById(R.id.edit_pet_age);
                        EditText petWeightTextView = customView.findViewById(R.id.edit_pet_weight);
                        RadioGroup petSexGroup=customView.findViewById(R.id.edit_pet_gender);
                        int selectedId=petSexGroup.getCheckedRadioButtonId();
                        RadioButton petGenderButton = customView.findViewById(selectedId);

                        String pet_name = "Pet Name: "+petNameTextView.getText().toString();
                        String pet_age = "Age: "+petAgeTextView.getText().toString() + " yrs";
                        String pet_weight = "Weight: "+petWeightTextView.getText().toString()+ " lbs";
                        String pet_gender = "Gender: "+petGenderButton.getText().toString();

                        petNameTV.setText(pet_name);
                        petGenderTV.setText(pet_gender);
                        petAgeTV.setText(pet_age);
                        petWeightTV.setText(pet_weight);

                        petNameTextView.setText(pet_name);
                        petGenderButton.setChecked(true);
                        petAgeTextView.setText(pet_age);
                        petWeightTextView.setText(pet_weight);

                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });
            }

        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        // TODO: Use the ViewModel

    }

}