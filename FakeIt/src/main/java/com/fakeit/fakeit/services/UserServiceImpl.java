package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.UserCreateDto;
import com.fakeit.fakeit.dtos.UserDto;
import com.fakeit.fakeit.dtos.UserUpdateDto;
import com.fakeit.fakeit.models.UserRank;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Firestore firestore = FirestoreClient.getFirestore();

    @Override
    public String createUser(UserCreateDto dto) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> docData = new HashMap<>();
        docData.put("administrador", false);
        docData.put("correo", dto.getCorreo());
        docData.put("fechaUnion", Timestamp.now());
        docData.put("fotoPerfil", dto.getFotoPerfil());
        docData.put("grupos", new ArrayList<>());
        docData.put("nombreUsuario", dto.getNombreUsuario());
        docData.put("puntosTotales", 0);
        docData.put("rango", UserRank.BRONZE);
        docData.put("ultimaSesion", null);
        docData.put("usuarioId", id);

        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("usuarios");
        try {
            ApiFuture<WriteResult> result = collectionReference.document(id).set(docData);
            result.get();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public Optional<UserDto> getUserById(String id) {
        DocumentReference docRef = firestore.collection("usuarios").document(String.valueOf(id));
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot doc = future.get();
            if (!doc.exists()) return Optional.empty();
            return Optional.of(doc.toObject(UserDto.class));
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserDto> searchUsersByUsername(String query) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("usuarios").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<UserDto> results = new ArrayList<>();
            for (DocumentSnapshot doc : documents) {
                String nombreUsuario = doc.getString("nombreUsuario");
                if (nombreUsuario != null && nombreUsuario.toLowerCase().contains(query.toLowerCase())) {
                    results.add(doc.toObject(UserDto.class));
                }
            }
            return results;
        } catch (InterruptedException | ExecutionException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("usuarios")
                    .whereEqualTo("correo", email).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            if (documents.isEmpty()) return Optional.empty();
            return Optional.of(documents.get(0).toObject(UserDto.class));
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }

    @Override
    public UserDto updateUser(String id, UserUpdateDto dto) {
        Map<String, Object> updates = new HashMap<>();
        if (dto.getNombreUsuario() != null) updates.put("nombreUsuario", dto.getNombreUsuario());
        if (dto.getFotoPerfil() != null) updates.put("fotoPerfil", dto.getFotoPerfil());
        firestore.collection("usuarios").document(id).update(updates);
        return getUserById(id).orElse(null);
    }

    @Override
    public List<UserDto> getAllUsers() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("usuarios").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<UserDto> results = new ArrayList<>();
            for (DocumentSnapshot doc : documents) {
                results.add(doc.toObject(UserDto.class));
            }
            return results;
        } catch (InterruptedException | ExecutionException e) {
            return Collections.emptyList();
        }
    }
}
