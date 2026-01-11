package ecom_blog.service;

import ecom_blog.model.Commande;
import ecom_blog.repository.CommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository repo;

    public long count() {
        return repo.count();
    }

    public List<Commande> getAll() {
        return repo.findAllByOrderByDateCommandeDesc();
    }

    public void save(Commande c) {
        repo.save(c);
    }

    public Commande findById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
