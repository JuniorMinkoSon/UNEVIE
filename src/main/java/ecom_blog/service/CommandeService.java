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

    public List<Commande> getAll() {
        return commandeRepository.findAll();
    }

    public Commande getById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
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

    public List<Commande> getByUser(User user) {
        return commandeRepository.findByUserOrderByDateCommandeDesc(user);
    }

    // 📊 Statistiques mensuelles
    public Map<String, Long> getMonthlyOrders() {
        int currentYear = LocalDateTime.now().getYear();
        List<Object[]> results = commandeRepository.countOrdersByMonth(currentYear);

        Map<String, Long> stats = new LinkedHashMap<>();

        String[] months = {
                "Jan", "Fév", "Mar", "Avr", "Mai", "Juin",
                "Juil", "Août", "Sep", "Oct", "Nov", "Déc"
        };

        // initialisation
        for (String m : months) {
            stats.put(m, 0L);
        }

        // remplissage depuis la requête
        for (Object[] row : results) {
            int monthIndex = ((Number) row[0]).intValue() - 1;
            long count = ((Number) row[1]).longValue();

            if (monthIndex >= 0 && monthIndex < 12) {
                stats.put(months[monthIndex], count);
            }
        }

        return stats;

    }

    public List<Commande> getCommandesEnCoursLivraison() {
        return commandeRepository.findCommandesEnCoursLivraison();
    }

    public Commande findById(Long id) {
        return commandeRepository.findById(id).orElse(null);
    }
}
