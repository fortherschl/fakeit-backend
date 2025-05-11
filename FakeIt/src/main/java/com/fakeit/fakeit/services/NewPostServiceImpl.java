package com.fakeit.fakeit.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NewPostServiceImpl implements NewPostService {

    @Override
    public boolean addNewPost(String description, String title, String imageUrl, boolean real, String userId) {
        Map<String, Object> postData = new HashMap<>();

        postData.put("descripcion", descripion);
        postData.put("fechaPublicacion", Timestamp.now());
        postData.put("formato", formato);
        postData.put("fotoId", UUID.randomUUID().toString());
        postData.put("real", real);
        postData.put("titulo", titulo);
        postData.put("urlImagen", urlImagen);
        postData.put("usuarioID", usuarioID);
        postData.put("votos", new HashMap<String, Boolean>());

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
}
