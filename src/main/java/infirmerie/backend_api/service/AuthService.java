package infirmerie.backend_api.service;

import infirmerie.backend_api.dto.request.LoginRequest;
import infirmerie.backend_api.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(LoginRequest request);
}