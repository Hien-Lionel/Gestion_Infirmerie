package infirmerie.backend_api.service;

import infirmerie.backend_api.dto.request.ChangePasswordRequest;
import infirmerie.backend_api.dto.request.CreateUtilisateurRequest;
import infirmerie.backend_api.dto.request.UpdateUtilisateurRequest;
import infirmerie.backend_api.exception.ResourceNotFoundException;
import infirmerie.backend_api.model.Utilisateur;
import infirmerie.backend_api.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilisateurService { // Changé en class

    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Permet de créer un nouvel utilisateur avec un mot de passe haché.
     */
    @Transactional
    public Utilisateur createUtilisateur(CreateUtilisateurRequest request) {
        // 1. Vérifier si l'email est déjà utilisé
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cette adresse email est déjà utilisée par un autre compte");
        }

        // 2. Instancier l'entité Utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        //utilisateur.setTelephone(request.getTelephone());

        // 3. Hacher le mot de passe initial avec BCrypt
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        utilisateur.setPassword(hashedPassword);

        // 4. Sauvegarder en base de données
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Permet à un utilisateur de modifier son mot de passe de manière sécurisée.
     */
    @Transactional
    public void changePassword(Long utilisateurId, ChangePasswordRequest request) {
        // 1. Récupérer l'utilisateur en base de données
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        // 2. Vérifier si l'ancien mot de passe saisi correspond au hash stocké en BDD
        if (!passwordEncoder.matches(request.getAncienpassword(), utilisateur.getPassword())) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect");
        }

        // 3. Vérifier si le nouveau mot de passe et la confirmation sont identiques
        if (!request.getNouveaupassword().equals(request.getConfirmationPassword())) {
            throw new IllegalArgumentException("Le nouveau mot de passe et sa confirmation ne correspondent pas");
        }

        // 4. Hacher le nouveau mot de passe et sauvegarder
        String hashedNewPassword = passwordEncoder.encode(request.getNouveaupassword());
        utilisateur.setPassword(hashedNewPassword);

        utilisateurRepository.save(utilisateur);
    }

    /**
     * Permet de mettre à jour les informations du profil après validation du mot de passe actuel.
     */
    @Transactional
    public void updateUtilisateur(Long utilisateurId, UpdateUtilisateurRequest request, boolean estAdmin) {
        // 1. Récupérer l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        // 2. Sécurité : Si ce n'est pas un admin, on vérifie le mot de passe actuel
        if (!estAdmin) {
            if (!passwordEncoder.matches(request.getActuelPassword(), utilisateur.getPassword())) {
                throw new IllegalArgumentException("Mot de passe de confirmation incorrect. Modifications annulées.");
            }
        }
        // 3. Vérifier si le nouvel email n'est pas déjà pris par un autre compte
        if (!utilisateur.getEmail().equals(request.getEmail()) &&
                utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cette adresse email est déjà utilisée par un autre compte");
        }

        // 4. Mettre à jour les champs autorisés
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        //utilisateur.setTelephone(request.getTelephone());

        utilisateurRepository.save(utilisateur);
    }
}
