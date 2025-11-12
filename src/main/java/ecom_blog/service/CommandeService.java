package ecom_blog.service;

import ecom_blog.model.Commande;
import ecom_blog.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    public List<Commande> getAll() {
        return commandeRepository.findAll();
    }

    public Commande getById(Long id) {
        return commandeRepository.findById(id).orElse(null);
    }

    public void save(Commande commande) {
        commandeRepository.save(commande);
    }

    public long count() {
        return commandeRepository.count();
    }

    public long countByStatut(String statut) {
        return commandeRepository.countByStatut(statut);
    }

    public List<Commande> findLast5() {
        return commandeRepository.findTop5ByOrderByDateCommandeDesc();
    }
}
