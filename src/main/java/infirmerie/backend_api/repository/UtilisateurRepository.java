package infirmerie.backend_api.repository;

import infirmerie.backend_api.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

   Optional<Utilisateur> findByEmail(String email);

   boolean existsByEmail(String email);

}
