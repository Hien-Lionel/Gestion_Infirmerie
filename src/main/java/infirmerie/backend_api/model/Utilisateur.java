package infirmerie.backend_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import infirmerie.backend_api.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true)
    private String nom;

    @Column(length = 100, nullable = true)
    private String prenom;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)  // stocke "ADMIN" et non 0, 1, 2...
    @Column(nullable = true)
    private Role role;

    @Column(length = 20, nullable = true)
    private String poste;

    @Column(length = 20,  nullable = true)
    private String ville;

    @Column(length = 50, nullable = true)
    private String site;

    @Column(nullable = false)
    private Boolean statut; // true = actif

    //private String telephone;

    private LocalDateTime dateCreation;


    private LocalDateTime derniereConnexion;

    // --- UserDetails (Spring Security) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())); // ex: "ROLE_ADMIN"
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // email utilisé comme identifiant unique
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return statut; // compte désactivé = ne peut plus se connecter
    }

    @PrePersist
    public void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null)
            this.statut=true;
    }
}