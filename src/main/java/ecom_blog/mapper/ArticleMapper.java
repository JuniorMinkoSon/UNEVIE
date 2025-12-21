package ecom_blog.mapper;

import ecom_blog.dto.ArticleDto;
import ecom_blog.dto.CreateArticleDto;
import ecom_blog.dto.UpdateArticleDto;
import ecom_blog.model.Article;
import ecom_blog.model.Categorie;
import ecom_blog.service.CategorieService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ArticleMapper {

    private final CategorieService categorieService;

    public ArticleMapper(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    public Article toEntity(CreateArticleDto dto) {
        if (dto == null) {
            return null;
        }

        Article article = new Article();
        article.setTitre(dto.getTitre());
        article.setContenu(dto.getContenu());

        Categorie categorie = categorieService.findById(dto.getCategory());
        article.setCategory(categorie);

        return article;
    }


    public void updateEntity(UpdateArticleDto dto, Article article) {
        if (dto == null || article == null) {
            return;
        }

        article.setTitre(dto.getTitre());
        article.setContenu(dto.getContenu());

        Categorie categorie = categorieService.findById(dto.getCategory());
        article.setCategory(categorie);
    }

    public ArticleDto toDto(Article article) {
        if (article == null) {
            return null;
        }

        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setTitre(article.getTitre());
        dto.setContenu(article.getContenu());
        dto.setImageUrl(article.getImageUrl());
        dto.setCreated_at(article.getCreated_at());
        dto.setUpdated_at(article.getUpdated_at());

        // Catégorie simplifiée (pas l'objet complet)
        if (article.getCategory() != null) {
            dto.setCategoryId(article.getCategory().getId());
            dto.setCategoryName(article.getCategory().getLibelle());
        }

        return dto;
    }

    public List<ArticleDto> toDtoList(List<Article> articles) {
        if (articles == null) {
            return null;
        }

        return articles.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
