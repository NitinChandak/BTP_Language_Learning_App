package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Bar extends AppCompatActivity {
    ArrayList<String> s;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);

        BarChart barChart=findViewById(R.id.barChart);
        ArrayList<BarEntry> visitors= new ArrayList<>();
        s= Score1.Give();
        System.out.println(s+"hello");
        visitors.add(new BarEntry(1,Integer.parseInt(s.get(0))));
        visitors.add(new BarEntry(2,Integer.parseInt(s.get(1))));
        visitors.add(new BarEntry(3,Integer.parseInt(s.get(2))));
//        visitors.add(new BarEntry(1,2014));
//        visitors.add(new BarEntry(2,2015));
//        visitors.add(new BarEntry(3,2016));

        BarDataSet barDataSet =new BarDataSet(visitors,"scores");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData =new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Progress Visualization");
        barChart.animateY(2000);

    }
}
