package ecom_blog.service;

import ecom_blog.repository.CommandeRepository;
import ecom_blog.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatistiquesServiceImpl implements StatistiquesService {

    private final CommandeRepository commandeRepository;
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final ProduitService produitService;
    private final FournisseurService fournisseurService;

    public StatistiquesServiceImpl(CommandeRepository commandeRepository,
            ReservationRepository reservationRepository,
            UserService userService,
            ProduitService produitService,
            FournisseurService fournisseurService) {
        this.commandeRepository = commandeRepository;
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.produitService = produitService;
        this.fournisseurService = fournisseurService;
    }

    @Override
    public Map<String, Object> getGlobalKpis() {
        Map<String, Object> kpis = new LinkedHashMap<>();
        kpis.put("👥 Utilisateurs", userService.count());
        kpis.put("🛍️ Produits", produitService.count());
        kpis.put("🏢 Fournisseurs", fournisseurService.countActifs());
        kpis.put("📦 Commandes", commandeRepository.count());
        kpis.put("📅 Réservations", reservationRepository.count());
        return kpis;
    }

    @Override
    public Map<String, List<Double>> getCombinedMonthlyRevenue(int year) {
        Map<String, List<Double>> results = new HashMap<>();

        List<Double> productRevenue = new ArrayList<>(Collections.nCopies(12, 0.0));
        List<Double> serviceRevenue = new ArrayList<>(Collections.nCopies(12, 0.0));
        List<Double> commissions = new ArrayList<>(Collections.nCopies(12, 0.0));

        // Produits
        List<Object[]> prodStats = commandeRepository.sumRevenueByMonth(year);
        for (Object[] row : prodStats) {
            int month = ((Number) row[0]).intValue() - 1;
            Double total = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            if (month >= 0 && month < 12)
                productRevenue.set(month, total);
        }

        // Services & Commissions
        // Note: Using reservationRepository.getStatistiquesMensuelles logic which
        // returns [month, count, total, commissions, net]
        // Since getStatistiquesMensuelles is in ReservationService usually, I might
        // call it if I had access,
        // but I'll replicate the core logic here or call reservationRepository if
        // methods exist.
        // Actually, ReservationService.getStatistiquesMensuelles is better.
        // For simplicity and decoupling, I'll calculate here or expose from
        // ReservationRepository if needed.

        // I will assume the logic from ReservationService is accessible or I can use
        // the repository directly.
        // Re-implementing a simple version here for the combined view:
        List<Object[]> resStats = reservationRepository.getStatistiquesMensuelles(year);
        for (Object[] row : resStats) {
            int month = ((Number) row[0]).intValue() - 1;
            Double total = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            Double comm = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;
            if (month >= 0 && month < 12) {
                serviceRevenue.set(month, total);
                commissions.set(month, comm);
            }
        }

        results.put("produits", productRevenue);
        results.put("services", serviceRevenue);
        results.put("commissions", commissions);

        return results;
    }

    @Override
    public Map<String, Double> getFinancialSummary() {
        Map<String, Double> summary = new HashMap<>();

        // Commande total (e-commerce)
        Double totalProd = commandeRepository.findAll().stream()
                .mapToDouble(c -> c.getTotal() != null ? c.getTotal() : 0.0)
                .sum();

        // Reservation total & commissions
        Double totalServRaw = reservationRepository.sumTotalMontant();
        Double totalCommRaw = reservationRepository.sumTotalCommission();

        Double totalServ = totalServRaw != null ? totalServRaw : 0.0;
        Double totalComm = totalCommRaw != null ? totalCommRaw : 0.0;

        summary.put("revenueProduits", totalProd);
        summary.put("revenueServices", totalServ);
        summary.put("totalCommissions", totalComm);
        summary.put("gmvTotal", totalProd + totalServ);

        return summary;
    }
}
