package com.atendimento.activity;

import android.os.Bundle;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.Calendar;

import android.util.Size;
import android.view.Display;
import com.prolificinteractive.materialcalendarview.CalendarUtils;
import com.prolificinteractive.materialcalendarview.WeekPagerAdapter;
import com.prolificinteractive.materialcalendarview.WeekView;
import com.prolificinteractive.materialcalendarview.format.CalendarWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.DateFormatDayFormatter;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter;
import java.time.Year;
import java.time.temporal.WeekFields;
import java.util.Date;

public class MarcarHorariosActivity extends BaseActivity {

    private MaterialCalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_horarios);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Marcar Horário");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        calendar = findViewById(R.id.calendarMarcarHorarios);
        calendar.setPagingEnabled(false);
        calendar.setShowOtherDates(MaterialCalendarView.SHOW_NONE);
        calendar.setAllowClickDaysOutsideCurrentMonth(false);

        Calendar calendarParametro = Calendar.getInstance();
        int diaSemana = calendarParametro.get(Calendar.DAY_OF_WEEK);
        int contadorDias = 0;
        switch (diaSemana){
            case Calendar.MONDAY    : contadorDias  = 6; break;
            case Calendar.TUESDAY   : contadorDias  = 5; break;
            case Calendar.WEDNESDAY : contadorDias  = 4; break;
            case Calendar.THURSDAY  : contadorDias  = 3; break;
            case Calendar.FRIDAY    : contadorDias  = 2; break;
            case Calendar.SATURDAY  : contadorDias  = 1; break;
            case Calendar.SUNDAY    : contadorDias  = 0; break;
        }
        calendarParametro.add(Calendar.DATE, contadorDias);

        calendar.state().edit()
                .setMaximumDate(calendarParametro)
                .setMinimumDate(CalendarDay.today())
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();

    }
}
