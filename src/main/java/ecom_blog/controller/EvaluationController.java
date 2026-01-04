package ecom_blog.controller;

import ecom_blog.model.Evaluation;
import ecom_blog.model.Reservation;
import ecom_blog.model.ServiceFournisseur;
import ecom_blog.model.User;
import ecom_blog.repository.EvaluationRepository;
import ecom_blog.repository.ServiceFournisseurRepository;
import ecom_blog.service.EvaluationService;
import ecom_blog.service.ReservationService;
import ecom_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ServiceFournisseurRepository serviceFournisseurRepository;

    @PostMapping("/submit")
    public String submitEvaluation(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long reservationId,
            @RequestParam Long serviceId,
            @RequestParam Integer note,
            @RequestParam String commentaire) {

        User client = userService.findByEmail(userDetails.getUsername());
        Reservation reservation = reservationService.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        ServiceFournisseur service = serviceFournisseurRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service non trouvé"));

        // Vérifier que c'est bien le client de la réservation
        if (!reservation.getClient().getId().equals(client.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à évaluer cette réservation");
        }

        // Vérifier si déjà évalué
        if (evaluationRepository.existsByReservationId(reservationId)) {
            return "redirect:/reservation/mes-reservations?error=already_eval";
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setClient(client);
        evaluation.setService(service);
        evaluation.setReservation(reservation);
        evaluation.setNote(note);
        evaluation.setCommentaire(commentaire);

        evaluationService.save(evaluation);

        return "redirect:/reservation/mes-reservations?success=eval";
    }
}
