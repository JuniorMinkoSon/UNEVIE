package ecom_blog.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchItem implements Comparable<SearchItem> {
    private Long id;
    private String label;
    private String type; // ARTICLE or SERVICE
    private String url;

    @Override
    public int compareTo(SearchItem other) {
        int cmp = this.label.toLowerCase().compareTo(other.label.toLowerCase());
        if (cmp == 0) {
            return this.id.compareTo(other.id);
        }
        return cmp;
    }

    @Override
    public String toString() {
        return label;
    }
}
