package com.example.scaf.ui.functions;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.scaf.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.zip.DataFormatException;

public class FunctionsFragment extends Fragment {

    private FunctionsViewModel mViewModel;
    private DatabaseReference mDatabase;
    private DatabaseReference refStat;
    private DatabaseReference refConfig;
    private Button setMeal;
    private EditText dateET;
    private EditText timeET;
    private EditText portionET;

    public static FunctionsFragment newInstance() {
        return new FunctionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_functions, container, false);

        setMeal = root.findViewById(R.id.set_btn);
        dateET = root.findViewById(R.id.dateET);
        timeET = root.findViewById(R.id.timeET);
        portionET = root.findViewById(R.id.portionET);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        refStat = mDatabase.child("stat");
        refConfig = mDatabase.child("config");
        final GraphView graph = (GraphView) root.findViewById(R.id.graph);

        ValueEventListener statListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SimpleDateFormat parser = new SimpleDateFormat("yyyyMMddHHmm");
                final SimpleDateFormat formatter = new SimpleDateFormat("E HH:mm");
                DataPoint[] points = new DataPoint[30];
                int i = 0;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String timestamp = snap.getKey();
                    Date datetime = new Date();
                    try {
                        datetime = parser.parse(timestamp);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    double weight = Double.parseDouble(snap.getValue().toString());
                    points[i] = new DataPoint(datetime.getTime(), weight);
                    i += 1;
                }
                LineGraphSeries <DataPoint> series = new LineGraphSeries<> (points);
                graph.addSeries(series);
                GridLabelRenderer renderer = graph.getGridLabelRenderer();
                renderer.setHorizontalLabelsAngle(135);
                renderer.setLabelFormatter(new DefaultLabelFormatter()
                {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            return formatter.format(new Date((long) value));
                        }
                        else {
                            return super.formatLabel(value, isValueX);
                        }
                    }
                });
                renderer.setPadding(64);
                renderer.setTextSize(30f);
                graph.getViewport().setMinX(points[0].getX());
                graph.getViewport().setMaxX(points[29].getX());
                graph.getViewport().setXAxisBoundsManual(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refStat.limitToLast(30).addValueEventListener(statListener);

        setMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateET.getText().toString();
                String time = timeET.getText().toString();
                String portion = portionET.getText().toString();
                if (date.length() != 8) {
                    dateET.setError("Please enter correct date!");
                    dateET.requestFocus();
                }
                else if (time.length() != 4) {
                    timeET.setError("Please enter correct time!");
                    timeET.requestFocus();
                }
                else if (portion.isEmpty()) {
                    portionET.setError("Please enter correct portion!");
                    portionET.requestFocus();
                }
                else {
                    String config = date + time + portion;
                    refConfig.setValue(config);
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FunctionsViewModel.class);
        // TODO: Use the ViewModel
    }

}