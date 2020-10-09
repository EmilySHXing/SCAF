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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private PopupWindow mPopupWindow;
    private ConstraintLayout mConstraintLayout;
    private Button edit_btn, update_btn;
    private View customView;
    private TextView petNameTV, petGenderTV, petAgeTV, petWeightTV, useremailTV, usernameTV;
    private static final String TAG = "AccountFragment";
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String pet_name, pet_gender, pet_age, pet_weight, username, email;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Log.d(TAG, "Test on account fragment");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        edit_btn = view.findViewById(R.id.editbutton);
        mConstraintLayout = view.findViewById(R.id.account);
        useremailTV = (TextView)view.findViewById(R.id.ac_useremail);
        usernameTV = (TextView)view.findViewById(R.id.ac_username);
        petNameTV = view.findViewById(R.id.petname);
        petGenderTV = view.findViewById(R.id.petgender);
        petAgeTV = view.findViewById(R.id.petage);
        petWeightTV = view.findViewById(R.id.petweight);
        String uid = user.getUid();

        mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, String.valueOf((HashMap<String, Object>) snapshot.getValue()));  //prints "Do you have data? You'll love Firebase."
                HashMap<String, Object> user_profile = (HashMap<String, Object>)snapshot.getValue();
                if (user_profile != null){
                    pet_name=(String)user_profile.get("petname");
                    pet_gender=(String)user_profile.get("gender");
                    pet_age=(String)user_profile.get("age");
                    pet_weight=(String)user_profile.get("weight");
                    username = (String)user_profile.get("username");
                    email = (String)user_profile.get("email");

                    usernameTV.setText(username);
                    useremailTV.setText(email);
                    petNameTV.setText("Name: "+pet_name);
                    petGenderTV.setText("Gender: "+pet_gender);
                    petAgeTV.setText("Age: "+pet_age+ " yrs");
                    petWeightTV.setText("Weight: "+pet_weight+ " lbs");

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                customView = inflater.inflate(R.layout.account_edit,null);

                EditText petNameTextView = customView.findViewById(R.id.edit_pet_name);
                EditText petAgeTextView = customView.findViewById(R.id.edit_pet_age);
                EditText petWeightTextView = customView.findViewById(R.id.edit_pet_weight);
                RadioButton petGenderButton;
                if (pet_gender.equals("Male")){
                    petGenderButton = customView.findViewById(R.id.radio_male);
                }
                else{
                    petGenderButton = customView.findViewById(R.id.radio_female);
                }

                petNameTextView.setText(pet_name);
                petGenderButton.setChecked(true);
                petAgeTextView.setText(pet_age);
                petWeightTextView.setText(pet_weight);

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

                        String pet_name = petNameTextView.getText().toString();
                        String pet_age = petAgeTextView.getText().toString();
                        String pet_weight = petWeightTextView.getText().toString();
                        String pet_gender = petGenderButton.getText().toString();

                        String uid = user.getUid();
                        mDatabase.child("users").child(uid).child("petname").setValue(pet_name);
                        mDatabase.child("users").child(uid).child("age").setValue(pet_age);
                        mDatabase.child("users").child(uid).child("weight").setValue(pet_weight);
                        mDatabase.child("users").child(uid).child("gender").setValue(pet_gender);

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