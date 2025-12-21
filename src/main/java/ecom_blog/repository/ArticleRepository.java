package ecom_blog.repository;

import ecom_blog.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * Trouver tous les articles d'une catégorie spécifique
     * 
     * @param categoryId ID de la catégorie
     * @return Liste des articles de cette catégorie
     */
    List<Article> findByCategoryId(Long categoryId);

    /**
     * Trouver tous les articles triés par date de création (plus récents d'abord)
     * 
     * @return Liste des articles triés
     */
    @Query("SELECT a FROM Article a ORDER BY a.created_at DESC")
    List<Article> findAllByOrderByCreatedAtDesc();
}
