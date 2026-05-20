package infirmerie.backend_api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private UtilisateurResponse utilisateur;
}