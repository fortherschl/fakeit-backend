package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.*;
import com.fakeit.fakeit.services.GroupService;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final Firestore db = FirestoreClient.getFirestore();

    @Override
    public GroupDto createGroup(GroupCreateDto dto, String creatorId) {
        String grupoId = UUID.randomUUID().toString();
        Timestamp now  = Timestamp.now();

        Map<String, Object> data = new HashMap<>();
        data.put("grupoId", grupoId);
        data.put("nombreGrupo", dto.getNombreGrupo());
        data.put("descripcion", dto.getDescripcion() == null ? "" : dto.getDescripcion());
        data.put("publico", dto.isPublico());
        data.put("maximoUsuarios", dto.getMaximoUsuarios());
        data.put("fechaCreacion", now);
        data.put("fechaUltimoMensaje", null);
        data.put("fotoPerfil", dto.getFotoPerfil() == null ?
                "/uploads/group-pictures/default.jpg" : dto.getFotoPerfil());
        data.put("administradores", List.of(creatorId));
        data.put("usuarios", List.of(creatorId));
        data.put("cantidadUsuarios", 1);
        data.put("rondaActual", 1);

        db.collection("grupos").document(grupoId).set(data);

        // añadir grupo al usuario
        db.collection("usuarios").document(creatorId)
                .update("grupos", FieldValue.arrayUnion(grupoId));

        GroupDto g = new GroupDto();
        g.setGrupoId(grupoId);
        g.setNombreGrupo(dto.getNombreGrupo());
        g.setDescripcion(dto.getDescripcion());
        g.setPublico(dto.isPublico());
        g.setMaximoUsuarios(dto.getMaximoUsuarios());
        g.setFechaCreacion(now);
        g.setFotoPerfil((String) data.get("fotoPerfil"));
        g.setAdministradores(List.of(creatorId));
        g.setUsuarios(List.of(creatorId));
        g.setCantidadUsuarios(1);
        g.setRondaActual(1);
        return g;
    }

    @Override
    public List<GroupDto> groupsOfUser(String userId) {
        return queryGroups(q -> q.whereArrayContains("usuarios", userId));
    }

    @Override
    public List<GroupDto> groupsUserIsNotIn(String userId) {
        List<GroupDto> out = new ArrayList<>();
        try {
            QuerySnapshot snap = db.collection("grupos")
                    .whereEqualTo("publico", true)
                    .get().get();

            for (DocumentSnapshot doc : snap.getDocuments()) {
                List<String> usuarios = (List<String>) doc.get("usuarios");
                int max = doc.getLong("maximoUsuarios").intValue();
                int actuales = doc.getLong("cantidadUsuarios").intValue();

                if ((usuarios == null || !usuarios.contains(userId)) && actuales < max) {
                    out.add(doc.toObject(GroupDto.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return out;
    }

    private List<GroupDto> queryGroups(java.util.function.Function<CollectionReference, Query> fn) {
        List<GroupDto> out = new ArrayList<>();
        try {
            CollectionReference ref = db.collection("grupos");
            Query q = fn.apply(ref);
            for (QueryDocumentSnapshot d : q.get().get().getDocuments()) {
                out.add(d.toObject(GroupDto.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return out;
    }

    // ---------- Get ----------
    @Override
    public GroupDto getGroup(String grupoId) {
        try {
            DocumentSnapshot d = db.collection("grupos").document(grupoId).get().get();
            return d.exists() ? d.toObject(GroupDto.class) : null;
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    // ---------- Detalles ----------
    @Override
    public GroupDetailsDto getGroupDetails(String grupoId, String requesterId) {
        try {
            DocumentSnapshot gDoc = db.collection("grupos").document(grupoId).get().get();
            if (!gDoc.exists()) return null;

            GroupDto g = gDoc.toObject(GroupDto.class);
            List<String> usuarioIds = (List<String>) gDoc.get("usuarios");

            // cargar usuarios básicos
            List<SimpleUserDto> miembros = new ArrayList<>();
            for (String uid : usuarioIds) {
                DocumentSnapshot udoc = db.collection("usuarios").document(uid).get().get();
                if (udoc.exists()) {
                    SimpleUserDto su = new SimpleUserDto();
                    su.setUsuarioId(uid);
                    su.setNombreUsuario(udoc.getString("nombreUsuario"));
                    su.setFotoPerfil(udoc.getString("fotoPerfil"));
                    miembros.add(su);
                }
            }

            GroupDetailsDto details = new GroupDetailsDto();
            details.setGrupo(g);
            details.setMiembros(miembros);
            details.setEsAdmin(g.getAdministradores().contains(requesterId));
            return details;
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public GroupDto joinPublicGroup(String grupoId, String userId) {
        try {
            DocumentReference ref = db.collection("grupos").document(grupoId);
            DocumentSnapshot doc = ref.get().get();
            if (!doc.exists()) throw new RuntimeException("Grupo no encontrado");

            Boolean publico = doc.getBoolean("publico");
            List<String> usuarios = (List<String>) doc.get("usuarios");
            int max = doc.getLong("maximoUsuarios").intValue();

            if (!publico) throw new RuntimeException("Grupo privado");
            if (usuarios.contains(userId)) throw new RuntimeException("Ya eres miembro");
            if (usuarios.size() >= max) throw new RuntimeException("Grupo lleno");

            ref.update("usuarios", FieldValue.arrayUnion(userId),
                    "cantidadUsuarios", usuarios.size() + 1);

            db.collection("usuarios").document(userId)
                    .update("grupos", FieldValue.arrayUnion(grupoId));

            return getGroup(grupoId);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void leaveGroup(String grupoId, String userId) {
        db.collection("grupos").document(grupoId)
                .update(Map.of(
                        "usuarios", FieldValue.arrayRemove(userId),
                        "administradores", FieldValue.arrayRemove(userId),
                        "cantidadUsuarios", FieldValue.increment(-1)
                ));
        db.collection("usuarios").document(userId)
                .update("grupos", FieldValue.arrayRemove(grupoId));
    }

    @Override
    public void inviteUser(String grupoId, String adminId, String invitedUserId) {
        DocumentSnapshot g = getSnapshot("grupos", grupoId);
        if (!((List<String>) g.get("administradores")).contains(adminId))
            throw new RuntimeException("No eres admin");

        createNotification("invitacion_grupo", grupoId, adminId, invitedUserId);
    }

    @Override
    public void requestJoinPrivate(String grupoId, String requesterId) {
        DocumentSnapshot g = getSnapshot("grupos", grupoId);
        if ((Boolean) g.get("publico"))
            throw new RuntimeException("Grupo público, únete directo");

        String adminId = ((List<String>) g.get("administradores")).get(0);
        createNotification("solicitud_ingreso", grupoId, requesterId, adminId);
    }

    @Override
    public void removeUser(String grupoId, String adminId, String usuarioId) {
        DocumentSnapshot g = getSnapshot("grupos", grupoId);
        if (!((List<String>) g.get("administradores")).contains(adminId))
            throw new RuntimeException("No eres admin");

        leaveGroup(grupoId, usuarioId);
    }

    @Override
    public void deleteGroup(String grupoId, String adminId) {
        DocumentSnapshot g = getSnapshot("grupos", grupoId);
        if (!((List<String>) g.get("administradores")).contains(adminId))
            throw new RuntimeException("No eres admin");

        List<String> usuarios = (List<String>) g.get("usuarios");
        for (String uid : usuarios) {
            db.collection("usuarios").document(uid)
                    .update("grupos", FieldValue.arrayRemove(grupoId));
        }
        db.collection("grupos").document(grupoId).delete();
    }

    private DocumentSnapshot getSnapshot(String col, String id) {
        try { return db.collection(col).document(id).get().get(); }
        catch (InterruptedException | ExecutionException e) { throw new RuntimeException(e); }
    }

    private void createNotification(String tipo, String grupoId, String remitente, String destinatario) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> n = Map.of(
                "notificacionId", id,
                "tipo", tipo,
                "grupoId", grupoId,
                "remitenteId", remitente,
                "destinatarioId", destinatario,
                "estado", "pendiente",
                "creadoEn", Timestamp.now()
        );
        db.collection("notificaciones").document(id).set(n);
    }
}