package ecom_blog.repository;

import ecom_blog.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByServiceId(Long serviceId);

    List<Evaluation> findByClientId(Long clientId);

    boolean existsByReservationId(Long reservationId);
}
