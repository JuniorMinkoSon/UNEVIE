package ecom_blog.service;

import ecom_blog.model.RoadEdge;
import ecom_blog.model.RoadNode;
import ecom_blog.repository.RoadEdgeRepository;
import ecom_blog.repository.RoadNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service implémentant l'algorithme de Dijkstra pour trouver le chemin le plus
 * court
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DijkstraRoutingService {

    private final RoadNodeRepository nodeRepository;
    private final RoadEdgeRepository edgeRepository;

    /**
     * Calcule le chemin le plus court entre deux nœuds en utilisant l'algorithme de
     * Dijkstra
     * 
     * @param startNodeId ID du nœud de départ
     * @param endNodeId   ID du nœud d'arrivée
     * @return Liste des nœuds formant le chemin optimal (null si aucun chemin)
     */
    public List<RoadNode> calculateShortestPath(Long startNodeId, Long endNodeId) {
        log.info("Calcul du chemin optimal de {} vers {}", startNodeId, endNodeId);

        // 1. Vérifier que les nœuds existent
        RoadNode startNode = nodeRepository.findById(startNodeId)
                .orElseThrow(() -> new IllegalArgumentException("Nœud de départ introuvable: " + startNodeId));
        RoadNode endNode = nodeRepository.findById(endNodeId)
                .orElseThrow(() -> new IllegalArgumentException("Nœud d'arrivée introuvable: " + endNodeId));

        // 2. Initialisation des structures de données
        Map<Long, Double> distances = new HashMap<>();
        Map<Long, Long> predecessors = new HashMap<>();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>();
        Set<Long> visited = new HashSet<>();

        // Initialiser toutes les distances à l'infini sauf le départ
        List<RoadNode> allNodes = nodeRepository.findAll();
        for (RoadNode node : allNodes) {
            distances.put(node.getId(), Double.MAX_VALUE);
        }
        distances.put(startNodeId, 0.0);
        queue.add(new NodeDistance(startNodeId, 0.0));

        log.debug("Démarrage Dijkstra avec {} nœuds dans le graphe", allNodes.size());

        // 3. Algorithme de Dijkstra
        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            Long currentNodeId = current.nodeId;

            // Si déjà visité, ignorer
            if (visited.contains(currentNodeId)) {
                continue;
            }
            visited.add(currentNodeId);

            // Si on a atteint la destination, on peut arrêter
            if (currentNodeId.equals(endNodeId)) {
                log.info("Destination atteinte ! Distance totale: {} km", current.distance);
                break;
            }

            // Explorer tous les voisins (arêtes sortantes)
            List<RoadEdge> edges = edgeRepository.findByNodeDepart_Id(currentNodeId);

            for (RoadEdge edge : edges) {
                Long neighborId = edge.getNodeArrivee().getId();

                // Si déjà visité, ignorer
                if (visited.contains(neighborId)) {
                    continue;
                }

                // Calculer la nouvelle distance
                Double currentDistance = distances.get(currentNodeId);
                Double edgeWeight = edge.getPoids();
                Double newDistance = currentDistance + edgeWeight;

                // Si c'est un meilleur chemin, mettre à jour
                if (newDistance < distances.get(neighborId)) {
                    distances.put(neighborId, newDistance);
                    predecessors.put(neighborId, currentNodeId);
                    queue.add(new NodeDistance(neighborId, newDistance));

                    log.debug("Nouveau chemin vers nœud {}: {}km via {}",
                            neighborId, newDistance, currentNodeId);
                }
            }
        }

        // 4. Vérifier si un chemin existe
        if (!predecessors.containsKey(endNodeId) && !startNodeId.equals(endNodeId)) {
            log.warn("Aucun chemin trouvé de {} vers {}", startNodeId, endNodeId);
            return null;
        }

        // 5. Reconstruire le chemin
        List<RoadNode> path = reconstructPath(predecessors, startNodeId, endNodeId);
        log.info("Chemin trouvé avec {} nœuds", path.size());

        return path;
    }

    /**
     * Trouve le nœud le plus proche d'une coordonnée GPS
     */
    public RoadNode findNearestNode(Double latitude, Double longitude) {
        return nodeRepository.findNearestNode(latitude, longitude)
                .orElseThrow(() -> new IllegalStateException("Aucun nœud trouvé dans le réseau routier"));
    }

    /**
     * Calcule le chemin optimal entre deux coordonnées GPS
     */
    public List<RoadNode> calculatePathFromCoordinates(Double latDepart, Double lngDepart,
            Double latArrivee, Double lngArrivee) {
        log.info("Calcul du chemin de ({}, {}) vers ({}, {})",
                latDepart, lngDepart, latArrivee, lngArrivee);

        // Trouver les nœuds les plus proches
        RoadNode startNode = findNearestNode(latDepart, lngDepart);
        RoadNode endNode = findNearestNode(latArrivee, lngArrivee);

        log.info("Nœuds sélectionnés: {} -> {}", startNode.getNom(), endNode.getNom());

        return calculateShortestPath(startNode.getId(), endNode.getId());
    }

    /**
     * Calcule la distance totale d'un chemin
     */
    public double calculateTotalDistance(List<RoadNode> path) {
        if (path == null || path.size() < 2) {
            return 0.0;
        }

        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            RoadNode current = path.get(i);
            RoadNode next = path.get(i + 1);

            // Trouver l'arête entre les deux nœuds
            List<RoadEdge> edges = edgeRepository.findEdgeBetweenNodes(current.getId(), next.getId());
            if (!edges.isEmpty()) {
                totalDistance += edges.get(0).getDistanceKm();
            }
        }

        return totalDistance;
    }

    /**
     * Calcule la durée totale estimée d'un chemin
     */
    public int calculateTotalDuration(List<RoadNode> path) {
        if (path == null || path.size() < 2) {
            return 0;
        }

        int totalDuration = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            RoadNode current = path.get(i);
            RoadNode next = path.get(i + 1);

            List<RoadEdge> edges = edgeRepository.findEdgeBetweenNodes(current.getId(), next.getId());
            if (!edges.isEmpty() && edges.get(0).getDureeMinutes() != null) {
                totalDuration += edges.get(0).getDureeMinutes();
            }
        }

        return totalDuration;
    }

    /**
     * Convertit un chemin de nœuds en liste de coordonnées GPS
     */
    public List<Map<String, Double>> convertPathToCoordinates(List<RoadNode> path) {
        List<Map<String, Double>> coordinates = new ArrayList<>();

        if (path != null) {
            for (RoadNode node : path) {
                Map<String, Double> coord = new HashMap<>();
                coord.put("lat", node.getLatitude());
                coord.put("lng", node.getLongitude());
                coordinates.add(coord);
            }
        }

        return coordinates;
    }

    /**
     * Reconstruit le chemin à partir des prédécesseurs
     */
    private List<RoadNode> reconstructPath(Map<Long, Long> predecessors, Long startId, Long endId) {
        List<RoadNode> path = new ArrayList<>();
        Long current = endId;

        // Remonter depuis la fin jusqu'au début
        while (current != null) {
            final Long nodeId = current;
            RoadNode node = nodeRepository.findById(nodeId)
                    .orElseThrow(() -> new IllegalStateException("Nœud " + nodeId + " introuvable"));
            path.add(0, node); // Ajouter au début

            if (current.equals(startId)) {
                break; // Arrivé au départ
            }

            current = predecessors.get(current);
        }

        return path;
    }

    /**
     * Classe helper pour la PriorityQueue
     */
    private static class NodeDistance implements Comparable<NodeDistance> {
        final Long nodeId;
        final Double distance;

        NodeDistance(Long nodeId, Double distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }
}
