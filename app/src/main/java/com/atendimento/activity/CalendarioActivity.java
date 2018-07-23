package com.atendimento.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarioActivity extends BaseActivity {

    private MaterialCalendarView calendario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Horários");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        setSupportActionBar(toolbarBase);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        calendario = findViewById(R.id.calendarView);
        calendario.state().edit()
                .setMinimumDate(CalendarDay.today())
                .setFirstDayOfWeek(2)
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();


        calendario.setBackgroundColor(getResources().getColor(R.color.colorFacebook));
        calendario.setDateTextAppearance(getResources().getColor(R.color.colorBranco));
        calendario.setArrowColor(getResources().getColor(R.color.colorBranco));

        calendario.setWeekDayTextAppearance(getResources().getColor(R.color.colorBranco));
        calendario.setHeaderTextAppearance(getResources().getColor(R.color.colorBranco));
        calendario.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorBranco));

        final List<Integer> weekDays = new ArrayList<>();
        weekDays.add(Calendar.MONDAY);
        calendario.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (weekDays.contains(date.getCalendar().get(Calendar.DAY_OF_WEEK))){
                    Toast.makeText(getApplicationContext(),"Segunda",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
