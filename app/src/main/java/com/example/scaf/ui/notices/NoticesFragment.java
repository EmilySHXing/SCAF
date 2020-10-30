package com.example.scaf.ui.notices;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.scaf.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Typeface.BOLD;

public class NoticesFragment extends Fragment {

    private NoticesViewModel mViewModel;
    private DatabaseReference mDatabase;
    private DatabaseReference refNotice;
    private LinearLayout scroll;
    private TextView[] notices = new TextView[50];

    public static NoticesFragment newInstance() {
        return new NoticesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notices, container, false);
        scroll = (LinearLayout) root.findViewById(R.id.linear);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        refNotice = mDatabase.child("notices");

        ValueEventListener statListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scroll.removeAllViews();
                int i = 0;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String timestamp = snap.getKey();
                    String status = (String) snap.getValue();

                    SimpleDateFormat parser = new SimpleDateFormat("yyyyMMddHHmm");
                    Date datetime = new Date();
                    try {
                        datetime = parser.parse(timestamp);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("MMM. dd, HH:mm");
                    timestamp = formatter.format(datetime);

                    notices[i] = new TextView(getContext());

                    String str;
                    int ind;
                    if (status.equals("full"))
                    {
                        ind = 24;
                        str = "Sufficient Food in Plate\n"+ timestamp;
                    }
                    else
                    {
                        ind = 16;
                        str = "No Food in Plate\n" + timestamp;
                    }


                    Spannable strSpan = new SpannableString(str);
                    strSpan.setSpan(
                            new StyleSpan(BOLD),
                            0,ind,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    strSpan.setSpan(
                            new RelativeSizeSpan(1.2f),
                            0,ind,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    strSpan.setSpan(
                            new ForegroundColorSpan(getResources().getColor(R.color.colorCoffee)),
                            0, ind,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    strSpan.setSpan(
                            new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
                            ind+1, ind+15,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    strSpan.setSpan(
                            new ForegroundColorSpan(getResources().getColor(R.color.colorBrown)),
                            ind+1, ind+15,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    notices[i].setText(strSpan);
                    notices[i].setPadding(50,10,50,10);
                    notices[i].setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    i += 1;
                }
                for (int j = i-1; j >= 0; j--)
                {
                    scroll.addView(notices[j]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refNotice.limitToLast(50).addValueEventListener(statListener);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NoticesViewModel.class);
        // TODO: Use the ViewModel
    }

}