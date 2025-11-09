package ecom_blog.service;

import ecom_blog.model.Commande;
import ecom_blog.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    public Commande save(Commande commande) {
        return commandeRepository.save(commande);
    }

    public List<Commande> getAll() {
        return commandeRepository.findAll();
    }

    public Commande getById(Long id) {
        return commandeRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean delete(Long id) {
        if (commandeRepository.existsById(id)) {
            commandeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Ajoute cette méthode :
    public long count() {
        return commandeRepository.count();
    }

}
