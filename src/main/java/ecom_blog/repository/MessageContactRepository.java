package ecom_blog.repository;

import ecom_blog.model.MessageContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageContactRepository extends JpaRepository<MessageContact, Long> {
    List<MessageContact> findTop5ByOrderByDateEnvoiDesc();
    List<MessageContact> findAllByOrderByDateEnvoiDesc();
}
