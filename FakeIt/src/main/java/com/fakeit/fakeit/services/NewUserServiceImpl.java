package com.fakeit.fakeit.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

public class NewUserServiceImpl implements NewUserService {
    @Override
    public boolean addNewUser(String email, String username) {
        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("usuarios");
        ApiFuture<WriteResult> result = collectionReference.document().create();
        return false;
    }
}
