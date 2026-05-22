package infirmerie.backend_api.service.impl;

import infirmerie.backend_api.dto.request.LoginRequest;
import infirmerie.backend_api.dto.response.AuthResponse;
import infirmerie.backend_api.dto.response.UtilisateurResponse;
import infirmerie.backend_api.model.Utilisateur;
import infirmerie.backend_api.repository.UtilisateurRepository;
import infirmerie.backend_api.security.JwtUtil;
import infirmerie.backend_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse authenticate(LoginRequest request) {

        // 1. Vérifier email + mot de passe (BCrypt) → lève une exception si invalide
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Charger l'utilisateur depuis la BDD
        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur non trouvé"
                ));

        // 3. Mettre à jour la dernière connexion
        utilisateur.setDerniereConnexion(LocalDateTime.now());
        utilisateurRepository.save(utilisateur);

        // 4. Générer le token JWT
        String token = jwtUtil.generateToken(utilisateur);

        // 5. Retourner token + infos utilisateur
        return AuthResponse.builder()
                .token(token)
                .utilisateur(UtilisateurResponse.builder()
                       // .id(utilisateur.getId())
                        /*.nom(utilisateur.getNom())
                        .prenom(utilisateur.getPrenom())*/
                        .email(utilisateur.getEmail())
                        /*.role(utilisateur.getRole())
                        .site(utilisateur.getSite())*/
                        .build())
                .build();
    }
}