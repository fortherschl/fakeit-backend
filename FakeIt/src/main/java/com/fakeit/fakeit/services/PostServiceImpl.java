package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.*;
import com.fakeit.fakeit.models.Vote;
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
public class PostServiceImpl implements PostService {

    private final Firestore db = FirestoreClient.getFirestore();

    @Override
    public boolean addNewPost(NewPostDto dto) {
        Map<String, Object> data = new HashMap<>();
        data.put("titulo", dto.getTitulo());
        data.put("urlImagen", dto.getUrlImagen());
        data.put("real", dto.isReal());
        data.put("usuarioId", dto.getUsuarioId());
        data.put("grupoId", dto.getGrupoId());
        data.put("fechaPublicacion", Timestamp.now());
        data.put("fotoId", UUID.randomUUID().toString());
        data.put("votos", new ArrayList<Vote>());

        try {
            db.collection("publicaciones").document().set(data).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PostDto getPostById(String id) {
        try {
            DocumentSnapshot doc = db.collection("publicaciones").document(id).get().get();
            if (!doc.exists()) throw new RuntimeException("No such document!");
            PostDto post = doc.toObject(PostDto.class);
            post.setPublicacionId(doc.getId());
            return post;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PostHiddenDto getPostSafe(String id) {
        try {
            DocumentSnapshot doc = db.collection("publicaciones").document(id).get().get();
            if (!doc.exists()) return null;

            PostHiddenDto h = new PostHiddenDto();
            h.setPublicacionId(doc.getId());
            h.setTitulo(doc.getString("titulo"));
            h.setUrlImagen(doc.getString("urlImagen"));
            h.setUsuarioId(doc.getString("usuarioId"));
            h.setGrupoId(doc.getString("grupoId"));
            h.setFechaPublicacion(doc.getTimestamp("fechaPublicacion"));
            return h;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PostDto> getPostsByGroup(String grupoId) {
        List<PostDto> posts = new ArrayList<>();
        try {
            CollectionReference postsRef = db.collection("publicaciones");
            List<QueryDocumentSnapshot> docs = postsRef.whereEqualTo("grupoId", grupoId).get().get().getDocuments();

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
    public List<PostHiddenDto> getPostsHidden(String grupoId) {
        List<PostHiddenDto> hiddenPosts = new ArrayList<>();
        try {
            List<QueryDocumentSnapshot> docs = db.collection("publicaciones")
                    .whereEqualTo("grupoId", grupoId).get().get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                PostHiddenDto h = new PostHiddenDto();
                h.setPublicacionId(doc.getId());
                h.setTitulo(doc.getString("titulo"));
                h.setUrlImagen(doc.getString("urlImagen"));
                h.setUsuarioId(doc.getString("usuarioId"));
                h.setGrupoId(doc.getString("grupoId"));
                h.setFechaPublicacion(doc.getTimestamp("fechaPublicacion"));
                hiddenPosts.add(h);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return hiddenPosts;
    }

    @Override
    public List<PostEnhancedDto> getPostsEnhanced(String grupoId) {
        List<PostEnhancedDto> enhanced = new ArrayList<>();
        try {
            List<QueryDocumentSnapshot> docs = db.collection("publicaciones")
                    .whereEqualTo("grupoId", grupoId).get().get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                PostDto p = doc.toObject(PostDto.class);
                if (p == null) continue;

                String usuarioId = p.getUsuarioId();
                DocumentSnapshot userDoc = db.collection("usuarios").document(usuarioId).get().get();

                PostEnhancedDto dto = new PostEnhancedDto();
                dto.setPublicacionId(doc.getId());
                dto.setTitulo(p.getTitulo());
                dto.setUrlImagen(p.getUrlImagen());
                dto.setUsuarioId(usuarioId);
                dto.setGrupoId(grupoId);
                dto.setFechaPublicacion(p.getFechaPublicacion());

                dto.setNombreUsuario(userDoc.getString("nombreUsuario"));
                dto.setFotoPerfil(userDoc.getString("fotoPerfil"));

                enhanced.add(dto);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return enhanced;
    }
}
