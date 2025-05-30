package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.NewPostDto;
import com.fakeit.fakeit.dtos.PostDto;
import com.fakeit.fakeit.dtos.PostEnhancedDto;
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
    public boolean addNewPost(NewPostDto dto) {
        Map<String, Object> data = new HashMap<>();
        data.put("title", dto.getTitle());
        data.put("format", dto.getFormat());
        data.put("urlImagen", dto.getUrl());
        data.put("real", dto.isReal());
        data.put("usuarioID", dto.getUserId());
        data.put("groupId", dto.getGrupoId());
        data.put("fechaPublicacion", Timestamp.now());
        data.put("fotoId", UUID.randomUUID().toString());
        data.put("votos", new ArrayList<Vote>());

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
                throw new RuntimeException("No such document!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PostDto> getPostsByGroup(String groupId) {
        List<PostDto> posts = new ArrayList<>();
        try {
            CollectionReference postsRef = db.collection("publicaciones");
            Query query = postsRef.whereEqualTo("grupoId", groupId);
            List<QueryDocumentSnapshot> docs = query.get().get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                PostDto post = doc.toObject(PostDto.class);
                post.setPublicacionId(doc.getId());
                posts.add(post);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public List<PostDto> getPostsHidden(String groupId) {
        return fetchPosts(groupId, true, false);
    }

    @Override
    public List<PostEnhancedDto> getPostsEnhanced(String groupId) {
        List<PostEnhancedDto> enhanced = new ArrayList<>();
        try {
            CollectionReference postsRef = db.collection("publicaciones");
            List<QueryDocumentSnapshot> docs = postsRef.whereEqualTo("groupoId", groupId).get().get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                PostDto p = doc.toObject(PostDto.class);
                String userId = p.getUsuarioId();

                DocumentSnapshot userDoc = db.collection("usuarios").document(userId).get().get();
                String nombre = userDoc.getString("nombreUsuario");
                String foto   = userDoc.getString("fotoPerfil");

                PostEnhancedDto dto = new PostEnhancedDto();
                dto.setPublicacionId(doc.getId());
                dto.setTitulo(p.getTitulo());
                dto.setUrlImagen(p.getUrlImagen());
                dto.setReal(null);                   // ocultamos
                dto.setUsuarioId(userId);
                dto.setNombreUsuario(nombre);
                dto.setFotoPerfil(foto);
                dto.setGrupoId(groupId);
                dto.setFechaPublicacion(p.getFechaPublicacion());

                enhanced.add(dto);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return enhanced;
    }

    @Override
    public PostDto getPostSafe(String id) {
        PostDto post = getPostById(id);
        if (post != null) post.setReal(false); // ocultar la respuesta
        return post;
    }

    private List<PostDto> fetchPosts(String groupId, boolean hideReal, boolean includeUser) {
        List<PostDto> posts = new ArrayList<>();
        try {
            CollectionReference postsRef = db.collection("publicaciones");
            List<QueryDocumentSnapshot> docs = postsRef.whereEqualTo("groupId", groupId).get().get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                PostDto post = doc.toObject(PostDto.class);
                post.setPublicacionId(doc.getId());
                if (hideReal) post.setReal(false);
                posts.add(post);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return posts;
    }
}

    /*public List<PostDto> get10Posts(int start) {
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

    }*/

