package com.fakeit.fakeit.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class NewUserServiceImpl implements NewUserService {
    @Override
    public boolean addNewUser(String email, String username) {

        Map<String, Object> docData = new HashMap<>();
        docData.put("administrador", false);
        docData.put("correo", email);
        docData.put("fechaUnion", Timestamp.now());
        docData.put("fotoPerfil", "");
        docData.put("grupos", new ArrayList<>());
        docData.put("nombreUsuario", username);
        docData.put("puntosTotales", 0);
        docData.put("rango", "Nuevo");
        docData.put("ultimaSesion", null);
        docData.put("usuarioId", UUID.randomUUID().toString());

        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("usuarios");
        try {
            ApiFuture<WriteResult> result = collectionReference.document().set(docData);
            result.get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
