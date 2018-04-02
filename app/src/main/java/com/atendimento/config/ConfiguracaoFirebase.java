package com.atendimento.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

final public class ConfiguracaoFirebase {

    private static DatabaseReference referenciaDatabase;
    private static FirebaseAuth      autenticacao;
    private static StorageReference  storage;
    private static FirebaseStorage   firebaseStorage;
    private static String            storageUrl = "gs://atendimento-23915.appspot.com/";

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

    public static StorageReference getStorage(){
        if (storage == null){
            firebaseStorage = FirebaseStorage.getInstance();
            storage = firebaseStorage.getReferenceFromUrl(storageUrl);
        }
        return storage;
    }
}
