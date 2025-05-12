package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.NewPostDto;
import com.fakeit.fakeit.dtos.PostDto;
import com.fakeit.fakeit.models.Vote;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Override
    public boolean addNewPost(NewPostDto newPostDto) {
        Map<String, Object> postData = new HashMap<>();

        postData.put("title", newPostDto.getTitle());
        postData.put("fechaPublicacion", Timestamp.now());
        postData.put("format", newPostDto.getFormat());
        postData.put("fotoId", UUID.randomUUID().toString());
        postData.put("real", newPostDto.isReal());
        postData.put("titulo", newPostDto.getTitle());
        postData.put("urlImagen", newPostDto.getUrl());
        postData.put("usuarioID", newPostDto.getUserId());
        postData.put("votos", new ArrayList<Vote>());

        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("posts");
        try {
            ApiFuture<WriteResult> result = collectionReference.document().set(postData);
            result.get(); // Espera a que se complete
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PostDto getPostById(String id) {
        Firestore db = FirestoreClient.getFirestore();

        // Firestore document IDs are strings, so convert the ID
        DocumentReference docRef = db.collection("posts").document(String.valueOf(id));
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(PostDto.class);
            } else {
                System.out.println("No such document!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
