package ecom_blog.service;

import ecom_blog.model.Commande;
import ecom_blog.repository.CommandeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;

    public CommandeService(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    // 📦 Toutes les commandes
    public List<Commande> getAll() {
        return commandeRepository.findAll();
    }

    // 🔎 Récupération sûre par ID (OBLIGATOIRE)
    public Commande getById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }

    // 💾 Sauvegarde
    public void save(Commande commande) {
        commandeRepository.save(commande);
    }

    // 📊 Statistiques
    public long count() {
        return commandeRepository.count();
    }

    public long countByStatut(String statut) {
        return commandeRepository.countByStatut(statut);
    }

    // 🕒 Dernières commandes
    public List<Commande> findLast5() {
        return commandeRepository.findTop5ByOrderByDateCommandeDesc();
    }
}
