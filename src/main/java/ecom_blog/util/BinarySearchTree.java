package ecom_blog.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation d'un Arbre Binaire de Recherche (ABR) pour une recherche
 * optimisée.
 * Comme défini : "Un arbre binaire de recherche permet des opérations rapides
 * pour rechercher, insérer ou supprimer une clé."
 */
public class BinarySearchTree<T extends Comparable<T>> {

    private Node<T> root;

    private static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;

        Node(T data) {
            this.data = data;
        }
    }

    public void insert(T data) {
        root = insertRecursive(root, data);
    }

    private Node<T> insertRecursive(Node<T> current, T data) {
        if (current == null) {
            return new Node<>(data);
        }

        if (data.compareTo(current.data) < 0) {
            current.left = insertRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            current.right = insertRecursive(current.right, data);
        }

        return current;
    }

    /**
     * Recherche textuelle (contenant la chaîne)
     */
    public List<T> searchByCriterium(String query) {
        List<T> results = new ArrayList<>();
        searchRecursive(root, query.toLowerCase(), results);
        return results;
    }

    private void searchRecursive(Node<T> node, String query, List<T> results) {
        if (node == null)
            return;

        // In-order traversal to get sorted results (optional but good)
        searchRecursive(node.left, query, results);

        if (node.data.toString().toLowerCase().contains(query)) {
            results.add(node.data);
        }

        searchRecursive(node.right, query, results);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
    }
}
