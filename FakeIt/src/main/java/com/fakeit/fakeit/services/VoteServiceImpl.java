package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.VoteDto;
import com.fakeit.fakeit.dtos.VoteRequestDto;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final Firestore db = FirestoreClient.getFirestore();

    @Override
    public VoteDto addVote(VoteRequestDto dto, String usuarioId) {

        if (dto.getReal() == null)
            throw new IllegalArgumentException("\"real\" debe ser booleano");

        DocumentSnapshot pubDoc = getDoc("publicaciones", dto.getPublicacionId());
        if (!pubDoc.exists()) throw new RuntimeException("Publicación no encontrada");

        String autorPub = pubDoc.getString("usuarioId");
        if (usuarioId.equals(autorPub))
            throw new RuntimeException("No puedes votar tu propia publicación");

        String grupoId = pubDoc.getString("grupoId");

        DocumentSnapshot grupoDoc = getDoc("grupos", grupoId);
        if (!grupoDoc.exists()) throw new RuntimeException("Grupo no encontrado");

        List<?> miembros = (List<?>) grupoDoc.get("usuarios");
        boolean esMiembro = false;
        if (miembros != null) {
            for (Object obj : miembros) {
                if (usuarioId.equals(String.valueOf(obj))) {
                    esMiembro = true;
                    break;
                }
            }
        }
        if (!esMiembro)
            throw new RuntimeException("No eres miembro de este grupo");

        try {
            QuerySnapshot dup = db.collection("votos")
                    .whereEqualTo("publicacionId", dto.getPublicacionId())
                    .whereEqualTo("usuarioId", usuarioId)
                    .get().get();

            if (!dup.isEmpty()) throw new RuntimeException("Ya votaste esta publicación");

            // Crear voto
            String votoId = UUID.randomUUID().toString();
            Timestamp now = Timestamp.now();

            Map<String, Object> voteData = new HashMap<>();
            voteData.put("votoId", votoId);
            voteData.put("publicacionId", dto.getPublicacionId());
            voteData.put("grupoId", grupoId);
            voteData.put("usuarioId", usuarioId);
            voteData.put("real", dto.getReal());
            voteData.put("fechaHora", now);

            // 1. Guardar el voto en la colección "votos"
            db.collection("votos").document(votoId).set(voteData);

            // 2. Agregar el voto al array de la publicación
            db.collection("publicaciones")
                    .document(dto.getPublicacionId())
                    .update("votos", FieldValue.arrayUnion(voteData));

            // 3. Devolver el voto como DTO
            VoteDto v = new VoteDto();
            v.setVotoId(votoId);
            v.setPublicacionId(dto.getPublicacionId());
            v.setGrupoId(grupoId);
            v.setUsuarioId(usuarioId);
            v.setReal(dto.getReal());
            v.setFechaHora(now);
            return v;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<VoteDto> getVotesByUserAndGroup(String usuarioId, String grupoId) {
        List<VoteDto> list = new ArrayList<>();
        try {
            QuerySnapshot snap = db.collection("votos")
                    .whereEqualTo("usuarioId", usuarioId)
                    .whereEqualTo("grupoId", grupoId)
                    .get().get();

            for (DocumentSnapshot d : snap.getDocuments()) {
                list.add(d.toObject(VoteDto.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private DocumentSnapshot getDoc(String col, String id) {
        try { return db.collection(col).document(id).get().get(); }
        catch (InterruptedException | ExecutionException e) { throw new RuntimeException(e); }
    }
}
