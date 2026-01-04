package ecom_blog.service;

import java.util.Map;
import java.util.List;

public interface StatistiquesService {

    /**
     * Retourne les indicateurs de performance globaux (Produits + Services)
     */
    Map<String, Object> getGlobalKpis();

    /**
     * Retourne les revenus mensuels combinés pour un graphique
     */
    Map<String, List<Double>> getCombinedMonthlyRevenue(int year);

    /**
     * Résumé financier pour l'admin
     */
    Map<String, Double> getFinancialSummary();
}
