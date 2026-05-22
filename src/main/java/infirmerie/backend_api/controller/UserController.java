package infirmerie.backend_api.controller;

import infirmerie.backend_api.dto.request.ChangePasswordRequest;
import infirmerie.backend_api.dto.request.CreateUtilisateurRequest;
import infirmerie.backend_api.dto.request.UpdateUtilisateurRequest;
import infirmerie.backend_api.model.Utilisateur;
import infirmerie.backend_api.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UtilisateurService utilisateurService;



    /**
     * Seul un Administrateur peut créer un nouvel utilisateur (infirmier, médecin, etc.).
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Utilisateur> create(@Valid @RequestBody CreateUtilisateurRequest request) {
        Utilisateur nouvelUtilisateur = utilisateurService.createUtilisateur(request);
        return new ResponseEntity<>(nouvelUtilisateur, HttpStatus.CREATED);
    }

    /**
     * Met à jour le profil d'un utilisateur.
     * La sécurité s'adapte automatiquement si l'appelant est ADMIN ou le propriétaire du compte.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUtilisateurRequest request,
            Authentication authentication) {

        // Vérifie si l'utilisateur connecté possède le rôle ADMINISTRATEUR
        boolean estAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        utilisateurService.updateUtilisateur(id, request, estAdmin);
        return ResponseEntity.ok("Profil mis à jour avec succès.");
    }

    /**
     * Permet à un utilisateur connecté de modifier son propre mot de passe.
     */
    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        utilisateurService.changePassword(id, request);
        return ResponseEntity.ok("Mot de passe modifié avec succès.");
    }
}
