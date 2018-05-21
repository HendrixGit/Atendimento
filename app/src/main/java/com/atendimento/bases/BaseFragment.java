package com.atendimento.bases;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class BaseFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private DatabaseReference firebaseDatabase;
    private ValueEventListener valueEventListener;

    protected void mudarTela(Context contexto, Class classe) {
        Intent intent = new Intent(contexto, classe);
        startActivity(intent);
    }

    protected void mudarTelaObject(Context context, Class classe, Object object){
        Intent intent = new Intent(context, classe);
        intent.putExtra("objeto", object.getClass());
        startActivity(intent);
    }

}
