package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.NewPostDto;
import com.fakeit.fakeit.dtos.PostDto;
import com.fakeit.fakeit.models.Post;
import com.fakeit.fakeit.models.Vote;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class PostServiceImpl implements PostService {

    private final Firestore db = FirestoreClient.getFirestore();

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

        CollectionReference collectionReference = FirestoreClient.getFirestore().collection("publicaciones");
        try {
            ApiFuture<WriteResult> result = collectionReference.document().set(postData);
            result.get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PostDto getPostById(String id) {
        DocumentReference docRef = db.collection("publicaciones").document(String.valueOf(id));
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

    public List<PostDto> get10Posts(int start) {
            ApiFuture<QuerySnapshot> future = db.collection("publicaciones")
                    .orderBy("cantidadVotos", Query.Direction.DESCENDING)
                    .limit(10+start)
                    .get();

        try {
            return future.get().toObjects(PostDto.class);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}
