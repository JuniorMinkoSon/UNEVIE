package ecom_blog.service;

import ecom_blog.model.MessageContact;
import ecom_blog.repository.MessageContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageContactService {

    private final MessageContactRepository repo;

    public void save(MessageContact msg) {
        repo.save(msg);
    }

    public long count() {
        return repo.count();
    }

    public List<MessageContact> getLast5() {
        return repo.findTop5ByOrderByDateEnvoiDesc();
    }

    public List<MessageContact> getAll() {
        return repo.findAllByOrderByDateEnvoiDesc();
    }

    public MessageContact findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {   // ✅ AJOUT OBLIGATOIRE
        repo.deleteById(id);
    }
}
