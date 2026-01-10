package ecom_blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivreurDto {
    private Long id;
    private String nom;
    private Double latitude;
    private Double longitude;
}
