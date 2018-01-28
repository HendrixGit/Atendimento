package com.atendimento.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaDatabase;
    private static FirebaseAuth autenticacao;

    public DatabaseReference getFirebase(){
        if (referenciaDatabase == null) {
            referenciaDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaDatabase;
    }

    public FirebaseAuth getAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }




}
