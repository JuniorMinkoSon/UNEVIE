package ecom_blog.controller;

import ecom_blog.dto.EquipeFiestaDto;
import ecom_blog.service.EquipeFiestaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fiesta/equipes")
@RequiredArgsConstructor
public class EquipeFiestaController {

    private final EquipeFiestaService equipeFiestaService;

    @GetMapping("/mes-equipes")
    public ResponseEntity<List<EquipeFiestaDto>> getMesEquipes(Authentication authentication) {
        // On utilisera l'ID de l'utilisateur authentifié
        // Pour l'instant, on peut utiliser un ID par défaut ou récupérer depuis le
        // contexte de sécurité
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(equipeFiestaService.getEquipesByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeFiestaDto> getEquipe(@PathVariable Long id) {
        return ResponseEntity.ok(equipeFiestaService.getEquipeById(id));
    }

    @PostMapping
    public ResponseEntity<EquipeFiestaDto> createEquipe(
            @Valid @RequestBody EquipeFiestaDto dto,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        EquipeFiestaDto created = equipeFiestaService.createEquipe(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeFiestaDto> updateEquipe(
            @PathVariable Long id,
            @Valid @RequestBody EquipeFiestaDto dto) {
        return ResponseEntity.ok(equipeFiestaService.updateEquipe(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipe(@PathVariable Long id) {
        equipeFiestaService.deleteEquipe(id);
        return ResponseEntity.noContent().build();
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        // TODO: Implémenter la récupération de l'ID utilisateur depuis le contexte de
        // sécurité
        // Pour l'instant, retourner 1L comme valeur par défaut
        if (authentication != null
                && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            // Logique pour récupérer l'ID utilisateur
            return 1L;
        }
        return 1L;
    }
}
