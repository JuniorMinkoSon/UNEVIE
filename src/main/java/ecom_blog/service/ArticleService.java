package ecom_blog.service;

import ecom_blog.dto.ArticleDto;
import ecom_blog.dto.UpdateArticleDto;
import ecom_blog.mapper.ArticleMapper;
import ecom_blog.model.Article;
import ecom_blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ArticleService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * Récupérer tous les articles sous forme de DTOs
     */
    public List<ArticleDto> getAll() {
        List<Article> articles = articleRepository.findAll();
        return articleMapper.toDtoList(articles);
    }

    /**
     * Récupérer un article par ID (retourne un DTO)
     */
    public ArticleDto findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'ID: " + id));
        return articleMapper.toDto(article);
    }

    /**
     * Récupérer tous les articles d'une catégorie (retourne des DTOs)
     */
    public List<ArticleDto> findByCategory(Long categoryId) {
        List<Article> articles = articleRepository.findByCategoryId(categoryId);
        return articleMapper.toDtoList(articles);
    }

    public long count() {
        return articleRepository.count();
    }

    /**
     * Créer un nouvel article
     */
    public void save(Article article, MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                // 📸 Nom unique du fichier
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                // 📂 Dossier de destination
                Path uploadPath = Paths.get(UPLOAD_DIR);

                // Créer le dossier s'il n'existe pas
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 💾 Copier le fichier dans /static/uploads/
                Files.copy(image.getInputStream(), uploadPath.resolve(fileName));

                // ✅ Ne pas inclure "uploads/" dans la base de données
                article.setImageUrl(fileName);
            }

            // Enregistrement de l'article en base
            articleRepository.save(article);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image", e);
        }
    }

    /**
     * Mettre à jour un article existant
     */
    public void update(Long id, UpdateArticleDto dto, MultipartFile image) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'ID: " + id));

        // Mettre à jour les champs de l'article
        articleMapper.updateEntity(dto, article);

        // Gérer l'image si fournie
        try {
            if (image != null && !image.isEmpty()) {
                // 📸 Nom unique du fichier
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                // 📂 Dossier de destination
                Path uploadPath = Paths.get(UPLOAD_DIR);

                // Créer le dossier s'il n'existe pas
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 💾 Copier le fichier dans /static/uploads/
                Files.copy(image.getInputStream(), uploadPath.resolve(fileName));

                // ✅ Mettre à jour l'URL de l'image
                article.setImageUrl(fileName);
            }

            // Enregistrement de l'article en base
            articleRepository.save(article);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image", e);
        }
    }

    /**
     * Supprimer un article
     */
    public void delete(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'ID: " + id));
        articleRepository.delete(article);
    }
}
