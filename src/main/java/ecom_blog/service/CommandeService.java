package ecom_blog.service;

import ecom_blog.model.Commande;
import ecom_blog.model.User;
import ecom_blog.repository.CommandeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    // 👤 Commandes d'un utilisateur
    public List<Commande> getByUser(User user) {
        return commandeRepository.findByUserOrderByDateCommandeDesc(user);
    }

    public Map<String, Long> getMonthlyOrders() {
        int currentYear = LocalDateTime.now().getYear();
        List<Object[]> results = commandeRepository.countOrdersByMonth(currentYear);

        Map<String, Long> stats = new LinkedHashMap<>();
        String[] months = { "Jan", "Fév", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc" };

        // Initialize with 0 for all months
        for (String month : months) {
            stats.put(month, 0L);
        }

        for (Object[] row : results) {
            int monthIndex = ((Number) row[0]).intValue() - 1;
            long count = ((Number) row[1]).longValue();
            if (monthIndex >= 0 && monthIndex < 12) {
                stats.put(months[monthIndex], count);
            }
        }

        return stats;
    }
}
