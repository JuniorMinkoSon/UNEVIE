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

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private SearchService searchService;

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
    public void save(Article article, List<MultipartFile> images) {
        try {
            if (images != null && !images.isEmpty()) {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty()) {
                        // 📸 Nom unique du fichier
                        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                        // 💾 Copier le fichier dans /static/uploads/
                        Files.copy(image.getInputStream(), uploadPath.resolve(fileName));

                        // ✅ Ajouter à la liste des images
                        article.getImageUrls().add(fileName);

                        // ✅ Mettre à jour l'image principale si c'est la première
                        if (article.getImageUrl() == null) {
                            article.setImageUrl(fileName);
                        }
                    }
                }
            }
            // Enregistrement de l'article en base
            articleRepository.save(article);
            searchService.refreshIndex();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement des images", e);
        }
    }

    /**
     * Mettre à jour un article existant
     */
    public void update(Long id, UpdateArticleDto dto, java.util.List<MultipartFile> images) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'ID: " + id));

        // Mettre à jour les champs de l'article
        articleMapper.updateEntity(dto, article);

        // Gérer les images si fournies
        try {
            if (images != null && !images.isEmpty()) {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty()) {
                        // 📸 Nom unique du fichier
                        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                        // 💾 Copier le fichier dans /static/uploads/
                        Files.copy(image.getInputStream(), uploadPath.resolve(fileName));

                        // ✅ Ajouter à la liste des images
                        article.getImageUrls().add(fileName);

                        // ✅ Mettre à jour l'image principale si elle n'existe pas
                        if (article.getImageUrl() == null) {
                            article.setImageUrl(fileName);
                        }
                    }
                }
            }
            // Enregistrement de l'article en base
            articleRepository.save(article);
            searchService.refreshIndex();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement des images", e);
        }
    }

    /**
     * Supprimer un article
     */
    public void delete(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'ID: " + id));
        articleRepository.delete(article);
        searchService.refreshIndex();
    }
}
