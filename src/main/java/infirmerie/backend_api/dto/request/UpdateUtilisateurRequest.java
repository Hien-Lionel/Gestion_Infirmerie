package infirmerie.backend_api.dto.request;


import infirmerie.backend_api.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUtilisateurRequest {

    private String nom;

    private String prenom;

    private String email;

    private Role role;

    private String poste;

    private String ville;

    private String site;

    private Boolean statut;

 //   private String telephone;

    @NotBlank(message = "Le mot de passe actuel est requis pour confirmer les modifications")
    private String actuelPassword;
}
