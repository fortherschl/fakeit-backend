package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.LeaderboardEntryDto;
import com.fakeit.fakeit.dtos.LeaderboardSummaryDto;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {

    private final Firestore db = FirestoreClient.getFirestore();
    private static final ZoneId ZONE = ZoneId.of("Europe/Madrid");

    @Override
    public List<LeaderboardEntryDto> getGlobalLeaderboard() {
        List<LeaderboardEntryDto> out = new ArrayList<>();
        try {
            QuerySnapshot snap = db.collection("leaderboards")
                    .orderBy("enganadosCount", Query.Direction.DESCENDING)
                    .get().get();
            for (DocumentSnapshot d : snap.getDocuments()) {
                out.add(d.toObject(LeaderboardEntryDto.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public List<LeaderboardEntryDto> getDailyLeaderboard(String grupoId) {
        List<LeaderboardEntryDto> list = new ArrayList<>();
        LocalDate today = LocalDate.now(ZONE);
        try {
            QuerySnapshot snap = db.collection("leaderboards")
                    .whereEqualTo("grupoId", grupoId)
                    .get().get();

            for (DocumentSnapshot d : snap.getDocuments()) {
                Timestamp ts = d.getTimestamp("processedAt");
                if (ts == null) continue;

                LocalDate tsDate = ts.toDate().toInstant()
                        .atZone(ZONE)
                        .toLocalDate();

                if (tsDate.isEqual(today)) {
                    list.add(d.toObject(LeaderboardEntryDto.class));
                }
            }
            list.sort(Comparator.comparingInt(LeaderboardEntryDto::getEnganadosCount)
                    .reversed());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<LeaderboardSummaryDto> getSummary(String grupoId) {
        Map<String, LeaderboardSummaryDto> map = new HashMap<>();
        try {
            QuerySnapshot snap = db.collection("leaderboards")
                    .whereEqualTo("grupoId", grupoId)
                    .get().get();

            // 1) acumular puntos
            for (DocumentSnapshot d : snap.getDocuments()) {
                String uid = d.getString("usuarioId");
                int pts = d.getLong("puntosRecibidos").intValue();

                map.computeIfAbsent(uid, k -> {
                    LeaderboardSummaryDto s = new LeaderboardSummaryDto();
                    s.setUsuarioId(uid);
                    // nombre y rango se rellenarán después
                    s.setPuntosTotales(0);
                    return s;
                });
                map.get(uid).setPuntosTotales(
                        map.get(uid).getPuntosTotales() + pts);
            }

            // 2) enriquecer con datos de usuario (nombre, rango)
            for (LeaderboardSummaryDto s : map.values()) {
                DocumentSnapshot userDoc =
                        db.collection("usuarios").document(s.getUsuarioId()).get().get();
                s.setNombreUsuario(userDoc.getString("nombreUsuario"));
                s.setRango(userDoc.getString("rango"));
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 3) ordenar
        List<LeaderboardSummaryDto> res = new ArrayList<>(map.values());
        res.sort(Comparator.comparingInt(LeaderboardSummaryDto::getPuntosTotales)
                .reversed());
        return res;
    }
}