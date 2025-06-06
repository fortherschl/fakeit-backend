package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.NotificationDto;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final Firestore db = FirestoreClient.getFirestore();

    @Override
    public List<NotificationDto> getPendingNotifications(String userId) {
        List<NotificationDto> result = new ArrayList<>();

        try {
            QuerySnapshot snap = db.collection("notificaciones")
                    .whereEqualTo("destinatarioId", userId)
                    .whereEqualTo("estado", "pendiente")
                    .get().get();

            for (DocumentSnapshot doc : snap.getDocuments()) {
                NotificationDto dto = doc.toObject(NotificationDto.class);

                DocumentSnapshot userDoc = db.collection("usuarios")
                        .document(dto.getRemitenteId()).get().get();
                dto.setRemitenteUsername(userDoc.exists() ? userDoc.getString("nombreUsuario") : "Desconocido");

                DocumentSnapshot groupDoc = db.collection("grupos")
                        .document(dto.getGrupoId()).get().get();
                dto.setGrupoNombre(groupDoc.exists() ? groupDoc.getString("nombreGrupo") : "Desconocido");

                result.add(dto);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String respondToNotification(String notificationId, boolean accept) {
        try {
            DocumentReference notiRef = db.collection("notificaciones").document(notificationId);
            DocumentSnapshot notiDoc = notiRef.get().get();

            if (!notiDoc.exists()) return "Notificación no encontrada";

            Map<String, Object> data = notiDoc.getData();
            if (!Objects.equals(data.get("estado"), "pendiente")) {
                return "Ya respondida";
            }

            notiRef.update(
                    "estado", accept ? "aceptada" : "rechazada",
                    "respondidoEn", Timestamp.now()
            );

            if (!accept) return "Rechazada";

            // Lógica de aceptación: añadir a grupo y al usuario
            String tipo = (String) data.get("tipo");
            String grupoId = (String) data.get("grupoId");
            String idToAdd = tipo.equals("invitacion_grupo")
                    ? (String) data.get("destinatarioId")
                    : (String) data.get("remitenteId");

            db.collection("grupos").document(grupoId)
                    .update(
                            "usuarios", FieldValue.arrayUnion(idToAdd),
                            "cantidadUsuarios", FieldValue.increment(1)
                    );

            db.collection("usuarios").document(idToAdd)
                    .update("grupos", FieldValue.arrayUnion(grupoId));

            return "Aceptada";

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error interno";
        }
    }
}
