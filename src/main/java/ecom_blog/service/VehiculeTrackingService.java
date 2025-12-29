package ecom_blog.service;

import ecom_blog.model.VehiculePosition;
import ecom_blog.model.RoadNode;
import ecom_blog.model.enums.StatutVehicule;
import ecom_blog.repository.VehiculePositionRepository;
import ecom_blog.repository.RoadNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service de tracking en temps réel des véhicules
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculeTrackingService {

    private final VehiculePositionRepository positionRepository;
    private final RoadNodeRepository nodeRepository;
    private final SimpMessagingTemplate messagingTemplate; // Pour WebSocket

    /**
     * Met à jour la position d'un véhicule et diffuse via WebSocket
     */
    @Transactional
    public VehiculePosition updatePosition(Long vehiculeId, Double latitude, Double longitude,
            StatutVehicule statut, Double vitesse, Double direction) {
        log.debug("Mise à jour position véhicule {}: ({}, {})", vehiculeId, latitude, longitude);

        VehiculePosition position = new VehiculePosition();
        position.setVehiculeId(vehiculeId);
        position.setLatitude(latitude);
        position.setLongitude(longitude);
        position.setTimestamp(LocalDateTime.now());
        position.setStatut(statut);
        position.setVitesse(vitesse);
        position.setDirection(direction);

        // Trouver le nœud routier le plus proche
        Optional<RoadNode> nearestNode = nodeRepository.findNearestNode(latitude, longitude);
        nearestNode.ifPresent(position::setNoeudProche);

        // Sauvegarder
        VehiculePosition saved = positionRepository.save(position);

        // Diffuser via WebSocket pour mise à jour temps réel
        messagingTemplate.convertAndSend("/topic/vehiclePositions", saved);

        log.info("Position véhicule {} mise à jour: statut={}, vitesse={} km/h",
                vehiculeId, statut, vitesse);

        return saved;
    }

    /**
     * Récupère la dernière position d'un véhicule
     */
    public Optional<VehiculePosition> getLatestPosition(Long vehiculeId) {
        return positionRepository.findFirstByVehiculeIdOrderByTimestampDesc(vehiculeId);
    }

    /**
     * Récupère toutes les dernières positions de tous les véhicules
     */
    public List<VehiculePosition> getAllLatestPositions() {
        return positionRepository.findLatestPositionsForAllVehicles();
    }

    /**
     * Récupère les véhicules actifs (en course)
     */
    public List<VehiculePosition> getActiveVehicles() {
        return positionRepository.findLatestPositionsByStatut(StatutVehicule.EN_COURSE);
    }

    /**
     * Récupère les véhicules disponibles
     */
    public List<VehiculePosition> getAvailableVehicles() {
        return positionRepository.findLatestPositionsByStatut(StatutVehicule.DISPONIBLE);
    }

    /**
     * Trouve les véhicules à proximité d'un point
     */
    public List<VehiculePosition> findVehiclesNearby(Double latitude, Double longitude, Double radiusKm) {
        log.info("Recherche véhicules dans un rayon de {} km autour de ({}, {})",
                radiusKm, latitude, longitude);
        return positionRepository.findVehiclesNearby(latitude, longitude, radiusKm);
    }

    /**
     * Récupère l'historique des positions d'un véhicule
     */
    public List<VehiculePosition> getPositionHistory(Long vehiculeId, LocalDateTime debut, LocalDateTime fin) {
        return positionRepository.findByVehiculeIdAndTimestampBetweenOrderByTimestampAsc(
                vehiculeId, debut, fin);
    }

    /**
     * Change le statut d'un véhicule
     */
    @Transactional
    public VehiculePosition updateStatus(Long vehiculeId, StatutVehicule nouveauStatut) {
        Optional<VehiculePosition> latestOpt = getLatestPosition(vehiculeId);

        if (latestOpt.isPresent()) {
            VehiculePosition latest = latestOpt.get();
            return updatePosition(
                    vehiculeId,
                    latest.getLatitude(),
                    latest.getLongitude(),
                    nouveauStatut,
                    latest.getVitesse(),
                    latest.getDirection());
        } else {
            throw new IllegalStateException("Aucune position trouvée pour le véhicule " + vehiculeId);
        }
    }

    /**
     * Nettoyage des anciennes positions (à exécuter périodiquement)
     */
    @Transactional
    public void cleanOldPositions(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        log.info("Nettoyage des positions avant {}", cutoffDate);
        positionRepository.deleteByTimestampBefore(cutoffDate);
    }
}
