package ecom_blog.service;

import ecom_blog.model.Article;
import ecom_blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired private ArticleRepository articleRepo;

    public List<Article> getAll() { return articleRepo.findAll(); }

    public Article save(Article a) { return articleRepo.save(a); }

    public void delete(Long id) { articleRepo.deleteById(id); }

    public Article findById(Long id) { return articleRepo.findById(id).orElse(null); }
}
