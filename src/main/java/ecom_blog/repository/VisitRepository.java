package ecom_blog.repository;

import ecom_blog.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query(value = "SELECT EXTRACT(MONTH FROM v.timestamp) as month, COUNT(*) as count " +
            "FROM visits v " +
            "WHERE EXTRACT(YEAR FROM v.timestamp) = :year " +
            "GROUP BY month " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> countVisitsByMonth(@Param("year") int year);
}
