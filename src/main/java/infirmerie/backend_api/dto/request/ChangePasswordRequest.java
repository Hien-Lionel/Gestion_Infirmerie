package infirmerie.backend_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "L'ancien mot de passe est obligatoire")
    private String ancienpassword;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(max = 8, message = "Le nouveau mot de passe doit contenir au moins 8 caractères")
    private String nouveaupassword;

    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    private String confirmationPassword;
}
