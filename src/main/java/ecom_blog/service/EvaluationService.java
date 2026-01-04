package ecom_blog.service;

import ecom_blog.model.Evaluation;
import ecom_blog.model.ServiceFournisseur;
import ecom_blog.repository.EvaluationRepository;
import ecom_blog.repository.ServiceFournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ServiceFournisseurRepository serviceFournisseurRepository;

    @Transactional
    public Evaluation save(Evaluation evaluation) {
        // Enregistrer l'évaluation
        Evaluation saved = evaluationRepository.save(evaluation);

        // Mettre à jour les statistiques du service
        updateServiceMetrics(evaluation.getService().getId());

        return saved;
    }

    private void updateServiceMetrics(Long serviceId) {
        ServiceFournisseur service = serviceFournisseurRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service non trouvé"));

        List<Evaluation> evaluations = evaluationRepository.findByServiceId(serviceId);

        int count = evaluations.size();
        double sum = evaluations.stream().mapToDouble(Evaluation::getNote).sum();

        service.setNombreAvis(count);
        if (count > 0) {
            service.setNoteMoyenne(sum / count);
        } else {
            service.setNoteMoyenne(0.0);
        }

        serviceFournisseurRepository.save(service);
    }
}
