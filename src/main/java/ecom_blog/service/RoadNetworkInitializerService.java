package ecom_blog.service;

import ecom_blog.model.RoadNode;
import ecom_blog.model.RoadEdge;
import ecom_blog.repository.RoadNodeRepository;
import ecom_blog.repository.RoadEdgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Service pour initialiser le réseau routier d'Abidjan
 */
@Component
@Order(2) // S'exécute après DataInitializerService
@RequiredArgsConstructor
@Slf4j
public class RoadNetworkInitializerService implements CommandLineRunner {

    private final RoadNodeRepository nodeRepository;
    private final RoadEdgeRepository edgeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si le réseau existe déjà
        if (nodeRepository.count() > 0) {
            log.info("Le réseau routier existe déjà ({} nœuds)", nodeRepository.count());
            return;
        }

        log.info("🗺️ Initialisation du réseau routier d'Abidjan...");

        // =====================================================
        // CRÉER LES NŒUDS PRINCIPAUX D'ABIDJAN
        // =====================================================

        // Zone Cocody
        RoadNode riviera = createNode("Cocody Riviera", "Quartier résidentiel haut de gamme",
                5.3699, -3.9866, "quartier");
        RoadNode deuxPlateaux = createNode("Les Deux Plateaux", "Zone commerciale Cocody",
                5.3532, -4.0025, "quartier");
        RoadNode angre = createNode("Angré", "Quartier résidentiel",
                5.3847, -4.0154, "quartier");

        // Zone Plateau (Centre d'affaires)
        RoadNode plateauCentre = createNode("Plateau Centre", "Centre d'affaires",
                5.3267, -4.0122, "quartier");
        RoadNode portAutonome = createNode("Port Autonome", "Zone portuaire",
                5.2886, -4.0100, "landmark");

        // Zone Yopougon
        RoadNode yopSiporex = createNode("Yopougon Siporex", "Zone commerciale Yopougon",
                5.3433, -4.0587, "quartier");
        RoadNode yopKoumassi = createNode("Yopougon Koumassi", "Quartier Yopougon",
                5.3365, -4.0702, "quartier");

        // Zone Marcory
        RoadNode marcoryZone4 = createNode("Marcory Zone 4", "Quartier résidentiel",
                5.2964, -3.9777, "quartier");
        RoadNode marcoryRemblai = createNode("Marcory Remblai", "Zone commerciale",
                5.2881, -3.9654, "quartier");

        // Zone Treichville
        RoadNode treichvilleCentre = createNode("Treichville Centre", "Quartier commercial",
                5.2928, -4.0059, "quartier");

        // Axes majeurs
        RoadNode pontHKB = createNode("Pont Houphouët-Boigny", "Pont principal",
                5.3185, -4.0045, "intersection");
        RoadNode pontGaulle = createNode("Pont de Gaulle", "Pont secondaire",
                5.3020, -4.0015, "intersection");

        // Aéroport
        RoadNode aeroport = createNode("Aéroport FHB", "Aéroport international",
                5.2579, -3.9263, "landmark");

        log.info("✅ {} nœuds créés", nodeRepository.count());

        // =====================================================
        // CRÉER LES ARÊTES (ROUTES) ENTRE LES NŒUDS
        // =====================================================

        // Connexions Cocody
        createEdge(riviera, deuxPlateaux, 3.5, 8, "rue", true);
        createEdge(deuxPlateaux, angre, 4.2, 10, "boulevard", true);
        createEdge(riviera, plateauCentre, 8.5, 15, "autoroute", true);

        // Connexions Plateau
        createEdge(plateauCentre, pontHKB, 2.0, 5, "boulevard", true);
        createEdge(plateauCentre, portAutonome, 3.0, 8, "rue", true);
        createEdge(pontHKB, yopSiporex, 7.0, 18, "autoroute", true);
        createEdge(pontGaulle, plateauCentre, 1.5, 4, "boulevard", true);

        // Connexions Yopougon
        createEdge(yopSiporex, yopKoumassi, 5.0, 12, "boulevard", true);
        createEdge(yopSiporex, pontHKB, 7.0, 18, "autoroute", true);

        // Connexions Marcory
        createEdge(marcoryZone4, marcoryRemblai, 2.5, 6, "rue", true);
        createEdge(marcoryRemblai, pontGaulle, 3.5, 9, "boulevard", true);
        createEdge(marcoryZone4, aeroport, 6.0, 15, "autoroute", true);
        createEdge(riviera, marcoryZone4, 6.5, 12, "boulevard", true);

        // Connexions Treichville
        createEdge(treichvilleCentre, pontGaulle, 1.8, 5, "rue", true);
        createEdge(treichvilleCentre, marcoryRemblai, 3.0, 8, "boulevard", true);
        createEdge(plateauCentre, treichvilleCentre, 2.5, 7, "boulevard", true);

        // Connexions Aéroport
        createEdge(aeroport, marcoryRemblai, 5.5, 14, "autoroute", true);
        createEdge(aeroport, plateauCentre, 12.0, 25, "autoroute", true);

        // Routes supplémentaires pour créer un réseau connecté
        createEdge(angre, yopSiporex, 9.0, 22, "autoroute", true);
        createEdge(deuxPlateaux, marcoryZone4, 7.5, 16, "boulevard", true);
        createEdge(yopKoumassi, marcoryZone4, 8.5, 20, "boulevard", true);

        log.info("✅ {} arêtes créées", edgeRepository.count());
        log.info("🎉 Réseau routier d'Abidjan initialisé !");
        log.info("   - {} nœuds (quartiers, intersections, points d'intérêt)", nodeRepository.count());
        log.info("   - {} routes bidirectionnelles", edgeRepository.count());
    }

    private RoadNode createNode(String nom, String description, Double lat, Double lng, String type) {
        RoadNode node = new RoadNode();
        node.setNom(nom);
        node.setDescription(description);
        node.setLatitude(lat);
        node.setLongitude(lng);
        node.setTypeNode(type);
        return nodeRepository.save(node);
    }

    private RoadEdge createEdge(RoadNode depart, RoadNode arrivee, Double distanceKm,
            Integer dureeMin, String typeRoute, Boolean bidirectionnelle) {
        RoadEdge edge = new RoadEdge();
        edge.setNodeDepart(depart);
        edge.setNodeArrivee(arrivee);
        edge.setDistanceKm(distanceKm);
        edge.setDureeMinutes(dureeMin);
        edge.setTypeRoute(typeRoute);
        edge.setBidirectionnelle(bidirectionnelle);
        edge.setVitesseMoyenne(distanceKm / (dureeMin / 60.0)); // Calculer vitesse moyenne
        edge.calculatePoidsFromDistance(); // Calculer le poids

        RoadEdge saved = edgeRepository.save(edge);

        // Si bidirectionnelle, créer l'arête inverse
        if (bidirectionnelle) {
            RoadEdge reverse = new RoadEdge();
            reverse.setNodeDepart(arrivee);
            reverse.setNodeArrivee(depart);
            reverse.setDistanceKm(distanceKm);
            reverse.setDureeMinutes(dureeMin);
            reverse.setTypeRoute(typeRoute);
            reverse.setBidirectionnelle(true);
            reverse.setVitesseMoyenne(edge.getVitesseMoyenne());
            reverse.calculatePoidsFromDistance();
            edgeRepository.save(reverse);
        }

        return saved;
    }
}
