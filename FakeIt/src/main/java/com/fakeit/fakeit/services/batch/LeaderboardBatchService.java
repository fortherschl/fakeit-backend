package com.fakeit.fakeit.services.batch;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class LeaderboardBatchService {

    private final Firestore db = FirestoreClient.getFirestore();

    @Scheduled(cron = "0 0 14 * * *", zone = "Europe/Madrid")
    public void processLeaderboard() {
        log.info("⏰ Ejecutando cron de Leaderboard…");
        try {
            doProcess();
            log.info("✅ Leaderboard procesado");
        } catch (Exception e) {
            log.error("❌ Error procesando Leaderboard", e);
        }
    }

    public void doProcess() throws ExecutionException, InterruptedException {

        List<QueryDocumentSnapshot> publicaciones =
                db.collection("publicaciones").get().get().getDocuments();
        List<QueryDocumentSnapshot> votos =
                db.collection("votos").get().get().getDocuments();
        Map<String, DocumentSnapshot> gruposMap = docMap("grupos");
        Map<String, DocumentSnapshot> usuariosMap = docMap("usuarios");

        List<Map<String, Object>> leaderboardEntries = new ArrayList<>();
        Map<String, Integer> puntosPorUsuario = new HashMap<>();

        for (DocumentSnapshot pub : publicaciones) {
            String publicacionId = pub.getId();
            String grupoId       = pub.getString("grupoId");
            String usuarioId     = pub.getString("usuarioId");
            boolean real         = Boolean.TRUE.equals(pub.getBoolean("real"));
            String titulo        = pub.getString("titulo");
            String urlImagen     = pub.getString("urlImagen");
            Timestamp fechaPub   = pub.getTimestamp("fechaPublicacion");

            DocumentSnapshot g  = gruposMap.get(grupoId);
            DocumentSnapshot u  = usuariosMap.get(usuarioId);
            int tamanioGrupo    = g == null ? 1 :
                    g.getLong("cantidadUsuarios").intValue();

            int enganados = 0;
            for (QueryDocumentSnapshot v : votos) {
                if (publicacionId.equals(v.getString("publicacionId"))) {
                    boolean votoReal = Boolean.TRUE.equals(v.getBoolean("real"));
                    if (votoReal != real) enganados++;
                }
            }
            int puntos = Math.round(enganados * (tamanioGrupo / 50.0f) * 10);

            // --- construir entrada leaderboard ---
            Map<String, Object> entry = new HashMap<>();
            entry.put("publicacionId", publicacionId);
            entry.put("grupoId", grupoId);
            entry.put("usuarioId", usuarioId);
            entry.put("nombreUsuario", u == null ? "Usuario" : u.getString("nombreUsuario"));
            entry.put("fotoPerfil",   u == null ? ""        : u.getString("fotoPerfil"));
            entry.put("titulo", titulo);
            entry.put("real", real);
            entry.put("urlImagen", urlImagen);
            entry.put("enganadosCount", enganados);
            entry.put("tamanioGrupo", tamanioGrupo);
            entry.put("puntosRecibidos", puntos);
            entry.put("processedAt", Timestamp.now());
            leaderboardEntries.add(entry);
            puntosPorUsuario.merge(usuarioId, puntos, Integer::sum);
        }

        WriteBatch batch = db.batch();
        CollectionReference lref = db.collection("leaderboards");

        for (QueryDocumentSnapshot d : lref.get().get().getDocuments()) {
            batch.delete(d.getReference());
        }
        for (Map<String, Object> e : leaderboardEntries) {
            batch.set(lref.document(), e);
        }

        for (Map.Entry<String, Integer> e : puntosPorUsuario.entrySet()) {
            DocumentReference uref = db.collection("usuarios").document(e.getKey());
            batch.update(uref, "puntosTotales", FieldValue.increment(e.getValue()));
        }

        batch.commit().get();
    }

    private Map<String, DocumentSnapshot> docMap(String col) throws ExecutionException, InterruptedException {
        Map<String, DocumentSnapshot> map = new HashMap<>();
        for (DocumentSnapshot d : db.collection(col).get().get().getDocuments()) {
            map.put(d.getId(), d);
        }
        return map;
    }
}
