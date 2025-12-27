package ecom_blog.service;

import ecom_blog.model.Commande;
import ecom_blog.model.Role;
import ecom_blog.model.User;
import ecom_blog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AssignationService {

    private final UserRepository userRepository;

    public AssignationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User choisirLivreurOptimal(Commande commande) {

        List<User> livreursDisponibles =
                userRepository.findByRoleAndDisponible(Role.ROLE_LIVREUR, true);

        if (livreursDisponibles.isEmpty()) {
            return null;
        }

        return livreursDisponibles.stream()
                .min(Comparator.comparingDouble(this::calculerCoutLivreur))
                .orElse(null);
    }

    private double calculerCoutLivreur(User livreur) {
        double congestion = livreur.getCongestionScore() != null
                ? livreur.getCongestionScore() : 0;

        double consommation = livreur.getConsommationEstimee() != null
                ? livreur.getConsommationEstimee() : 0;

        return congestion + consommation;
    }
}
