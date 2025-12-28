package ecom_blog.controller;

import ecom_blog.dto.EntrepriseDto;
import ecom_blog.service.EntrepriseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/entreprises")
@RequiredArgsConstructor
public class AdminEntrepriseController {

    private final EntrepriseService entrepriseService;

    @GetMapping
    public ResponseEntity<List<EntrepriseDto>> getAllEntreprises() {
        return ResponseEntity.ok(entrepriseService.getAllEntreprises());
    }

    @PostMapping
    public ResponseEntity<EntrepriseDto> createEntreprise(@Valid @RequestBody EntrepriseDto dto) {
        EntrepriseDto created = entrepriseService.createEntreprise(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrepriseDto> updateEntreprise(
            @PathVariable Long id,
            @Valid @RequestBody EntrepriseDto dto) {
        return ResponseEntity.ok(entrepriseService.updateEntreprise(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntreprise(@PathVariable Long id) {
        entrepriseService.deleteEntreprise(id);
        return ResponseEntity.noContent().build();
    }
}
