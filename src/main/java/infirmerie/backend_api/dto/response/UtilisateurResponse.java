package infirmerie.backend_api.dto.response;

import infirmerie.backend_api.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtilisateurResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private String site;
}