package ecom_blog.service;

import ecom_blog.model.*;
import ecom_blog.repository.FournisseurRepository;
import ecom_blog.repository.ReservationRepository;
import ecom_blog.repository.ServiceFournisseurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private ServiceFournisseurRepository serviceFournisseurRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreerReservation() {
        User client = new User();
        ServiceFournisseur service = new ServiceFournisseur();
        service.setId(1L);
        service.setPrix(10000.0);
        Fournisseur fournisseur = new Fournisseur();
        service.setFournisseur(fournisseur);

        when(serviceFournisseurRepository.findById(1L)).thenReturn(Optional.of(service));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        Reservation res = reservationService.creerReservation(client, 1L, "Client Test", "0123456789", "test@test.com",
                LocalDate.now(), null, 2, null, false, "Note test");

        assertNotNull(res);
        assertEquals(StatutReservation.EN_COURS, res.getStatut());
        assertEquals(10000.0, res.getMontant());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testAccepterReservationCalculeCommission() {
        Reservation res = new Reservation();
        res.setId(1L);
        res.setMontant(10000.0);
        res.setStatut(StatutReservation.EN_COURS);

        Fournisseur f = new Fournisseur();
        f.setNombreReservations(0);
        f.setChiffreAffaires(0.0);
        f.setTotalCommissions(0.0);
        res.setFournisseur(f);

        ServiceFournisseur s = new ServiceFournisseur();
        s.setNombreReservations(0);
        res.setService(s);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(res));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        Reservation resAccepte = reservationService.accepterReservation(1L);

        assertEquals(StatutReservation.ACCEPTE, resAccepte.getStatut());
        assertEquals(500.0, resAccepte.getCommission()); // 5% de 10000
        assertEquals(9500.0, resAccepte.getMontantNet());

        assertEquals(1, f.getNombreReservations());
        assertEquals(10000.0, f.getChiffreAffaires());
        assertEquals(500.0, f.getTotalCommissions());
    }
}
