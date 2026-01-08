package ecom_blog.util.dijkstra;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Node implements Comparable<Node> {

    private String name;
    private List<Node> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;

    // Nœuds adjacents avec leur distance (poids)
    private Map<Node, Integer> adjacentNodes = new HashMap<>();

    public Node(String name) {
        this.name = name;
    }

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.distance, node.distance);
    }

    @Override
    public String toString() {
        return name;
    }
}
