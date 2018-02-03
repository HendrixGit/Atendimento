package com.atendimento.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

final public class ConfiguracaoFirebase {

    private static DatabaseReference referenciaDatabase;
    private static FirebaseAuth      autenticacao;

    public static DatabaseReference getFirebaseDatabase(){
        if (referenciaDatabase == null) {
            referenciaDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaDatabase;
    }

    public static FirebaseAuth getAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }




}
