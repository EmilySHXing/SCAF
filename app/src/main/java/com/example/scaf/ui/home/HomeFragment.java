package com.example.scaf.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.scaf.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.graphics.Typeface.BOLD;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private DatabaseReference mDatabase;
    private DatabaseReference refConfig;
    private DatabaseReference refStat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView dateTextView = root.findViewById(R.id.date);
        final TextView monthTextView = root.findViewById(R.id.month);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd\nE\nyyyy", Locale.US);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.US);
        String currentDate = dateFormat.format(date);
        String currentMonth = monthFormat.format(date);
        Spannable dateSpan = new SpannableString(currentDate);
        dateSpan.setSpan(
                new ForegroundColorSpan(0xffa2999e),
                3,6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateSpan.setSpan(
                new RelativeSizeSpan(0.7f),
                3,6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateSpan.setSpan(
                new StyleSpan(BOLD),
                0,2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateSpan.setSpan(
                new RelativeSizeSpan(2.5f),
                0,2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateSpan.setSpan(
                new RelativeSizeSpan(0.8f),
                7,11,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateTextView.setText(dateSpan);
        monthTextView.setText(currentMonth);

        final TextView mealTextView = root.findViewById(R.id.meal_right);
        final TextView plateTextView = root.findViewById(R.id.plate_right);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        refConfig = mDatabase.child("config");
        refStat = mDatabase.child("stat");

        ValueEventListener configListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String config = snapshot.getValue().toString();
                String portion = config.substring(12);
                String datetime = config.substring(0,12);
                SimpleDateFormat parser = new SimpleDateFormat("yyyyMMddHHmm");
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "MMM. dd, yyyy\nE HH:mm", Locale.US);
                Date nextDate = new Date();
                try {
                    nextDate = parser.parse(datetime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datetime = formatter.format(nextDate);
                mealTextView.setText(String.format("%s\nPortion: %s",datetime,portion));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refConfig.addValueEventListener(configListener);

        ValueEventListener statListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    double weight = Double.parseDouble(snap.getValue().toString());
                    plateTextView.setText(String.format("%.1f grams", weight));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refStat.limitToLast(1).addValueEventListener(statListener);

        /*
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
         */
        return root;
    }
}