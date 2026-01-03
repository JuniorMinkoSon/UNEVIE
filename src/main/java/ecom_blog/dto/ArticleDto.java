package ecom_blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Long id;
    private String titre;
    private String contenu;
    private String imageUrl;
    private java.util.List<String> imageUrls = new java.util.ArrayList<>();

    private Long categoryId;
    private String categoryName;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
