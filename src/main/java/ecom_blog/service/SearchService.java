package ecom_blog.service;

import ecom_blog.model.Article;
import ecom_blog.model.ServiceFournisseur;
import ecom_blog.repository.ArticleRepository;
import ecom_blog.repository.ServiceFournisseurRepository;
import ecom_blog.util.BinarySearchTree;
import ecom_blog.util.SearchItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class SearchService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ServiceFournisseurRepository serviceFournisseurRepository;

    private BinarySearchTree<SearchItem> bst = new BinarySearchTree<>();

    @PostConstruct
    public void init() {
        refreshIndex();
    }

    public synchronized void refreshIndex() {
        bst.clear();

        // Indexer les articles
        List<Article> articles = articleRepository.findAll();
        log.info("🔍 Indexation de {} articles...", articles.size());
        for (Article a : articles) {
            log.info("  -> Article : {}", a.getTitre());
            bst.insert(new SearchItem(a.getId(), a.getTitre(), "ARTICLE", "/blog/article/" + a.getId()));
        }

        // Indexer les services
        List<ServiceFournisseur> services = serviceFournisseurRepository.findAll();
        log.info("🔍 Indexation de {} services...", services.size());
        for (ServiceFournisseur s : services) {
            log.info("  -> Service : {}", s.getNom());
            bst.insert(new SearchItem(s.getId(), s.getNom(), "SERVICE", "/reservation/service/" + s.getId()));
        }

        // ✅ Ajouter un élément par défaut pour tester le moteur
        bst.insert(new SearchItem(0L, "Unevie - Plateforme de Services", "SYSTEM", "/ propro"));
        log.info("✅ Indexation terminée. Racine de l'ABR : {}", bst.isEmpty() ? "VIDE" : "OK");
    }

    public List<SearchItem> search(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return bst.searchByCriterium(query);
    }
}
