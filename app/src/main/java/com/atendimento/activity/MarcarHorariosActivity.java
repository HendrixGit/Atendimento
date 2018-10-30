package com.atendimento.activity;

import android.content.Intent;
import android.os.Bundle;
import com.atendimento.R;
import com.atendimento.adapter.AdapterHorarios;
import com.atendimento.bases.BaseActivity;
import com.atendimento.config.ConfiguracaoFirebase;
import com.atendimento.model.Empresa;
import com.atendimento.model.Horario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MarcarHorariosActivity extends BaseActivity {

    private MaterialCalendarView calendar;
    private TextView titulo;
    private TextView subTitulo;
    private CircleImageView circleImageViewMarcar;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewMarcarHorarios;
    private ChildEventListener childEventListenerHorarios;
    private DatabaseReference firebase;
    private List<Horario> listaHorarios =  new ArrayList<>();
    private AdapterHorarios adapterHorarios;
    private Query query;
    private TextView horariosDisponiveis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_horarios);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Marcar Horário");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        titulo    = findViewById(R.id.textViewTituloMarcar);
        subTitulo = findViewById(R.id.textViewSubTituloMarcar);
        circleImageViewMarcar = findViewById(R.id.imageViewImagemMarcar);
        recyclerViewMarcarHorarios = findViewById(R.id.recyclerViewMarcarHorarios);
        progressBar = findViewById(R.id.progressBarMarcarHorarios);
        progressBar.setVisibility(View.VISIBLE);
        horariosDisponiveis = findViewById(R.id.textViewHorariosDiponiveis);
        firebase = ConfiguracaoFirebase.getFirebaseDatabase();

        Intent intent = getIntent();
        final Empresa empresa = (Empresa) intent.getSerializableExtra("empresa");
        if (empresa != null){
            titulo.setText(empresa.getNome());
            subTitulo.setText(empresa.getCategoria());
            try {
                Picasso.with(getApplicationContext()).
                        load(empresa.getUrlImagem()).
                        error(R.drawable.atendimento).
                        resize(64,64).
                        into(circleImageViewMarcar, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                                circleImageViewMarcar.setImageDrawable(getResources().getDrawable(R.drawable.atendimento));
                            }
                        });
            }
            finally{

            }
        }

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

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int diaCalendario = date.getCalendar().get(Calendar.DAY_OF_WEEK);
                setarDiasSemana(empresa, diaCalendario);
            }
        });


        Calendar calendario = Calendar.getInstance();
        int dia = calendario.get(Calendar.DAY_OF_WEEK);
        dia = 1;
        setarDiasSemana(empresa, dia);
        RecyclerView.LayoutManager layoutManager   = new LinearLayoutManager(this);
        recyclerViewMarcarHorarios.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mudarTelaFinish(getApplicationContext(), MainActivity.class);
    }

    public void setarDiasSemana(Empresa empresa, Integer dia){
        listaHorarios.clear();
        horariosDisponiveis.setText("Sem horários");
        adapterHorarios = new AdapterHorarios(listaHorarios, MarcarHorariosActivity.this);
        recyclerViewMarcarHorarios.setAdapter(adapterHorarios);
        query = firebase.child("horariosUsuarios").child(empresa.getId()).child(String.valueOf(dia)).orderByChild("ordem");
        childEventListenerHorarios = query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setHorario(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setHorario(DataSnapshot dataSnapshot){
        Horario horario = dataSnapshot.getValue(Horario.class);
        listaHorarios.add(horario);
        horariosDisponiveis.setText(horario.getDescricaoDia().toString());
        adapterHorarios.notifyDataSetChanged();
    }


}
