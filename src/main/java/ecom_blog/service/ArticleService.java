package ecom_blog.service;

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

    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    public long count() {
        return articleRepository.count();
    }

    public void save(Article article, MultipartFile image) {
        try {
            if (!image.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, image.getBytes());
                article.setImageUrl("/uploads/" + fileName);
            }
            articleRepository.save(article);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l’enregistrement de l’image", e);
        }
    }
}
