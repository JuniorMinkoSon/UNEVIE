package ecom_blog.service;

import ecom_blog.model.Visit;
import ecom_blog.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

    public void logVisit() {
        visitRepository.save(new Visit(LocalDateTime.now()));
    }

    public Map<String, Long> getMonthlyVisits() {
        int currentYear = LocalDateTime.now().getYear();
        List<Object[]> results = visitRepository.countVisitsByMonth(currentYear);

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
