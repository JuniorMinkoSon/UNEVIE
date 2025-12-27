package ecom_blog.service;

import ecom_blog.model.Commande;
import org.springframework.stereotype.Service;

@Service
public class ItineraireService {

    public void calculerItineraire(Commande commande) {

        double distanceKm = 12.5;
        double vitesseMoyenneKmH = 35;

        long dureeEstimeeMs =
                (long) ((distanceKm / vitesseMoyenneKmH) * 3600 * 1000);

        commande.setDistanceEstimee(distanceKm);
        commande.setDureeEstimee(dureeEstimeeMs);
        commande.setDebutLivraison(System.currentTimeMillis());
    }
}
