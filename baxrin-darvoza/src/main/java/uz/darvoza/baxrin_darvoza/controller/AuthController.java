package uz.darvoza.baxrin_darvoza.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.darvoza.baxrin_darvoza.dto.AdminDTO;
import uz.darvoza.baxrin_darvoza.dto.AppResponse;
import uz.darvoza.baxrin_darvoza.dto.LoginDTO;
import uz.darvoza.baxrin_darvoza.dto.RegistrationDTO;
import uz.darvoza.baxrin_darvoza.enums.AppLanguage;
import uz.darvoza.baxrin_darvoza.service.AuthService;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthController", description = "Controller for Authorization and authentication")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/api/v1/registration")
    public ResponseEntity<AppResponse<String>> registration(@RequestBody RegistrationDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language){
        return ResponseEntity.ok().body(authService.registration(dto,language));
    }

    @GetMapping("/api/v1/verification/{token}")
    public ResponseEntity<AppResponse<String>> verification(@PathVariable String token,
                                            @RequestParam (value = "language",defaultValue = "UZ")AppLanguage language){
        return ResponseEntity.ok().body(authService.verification(token,language));
    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<AdminDTO> login(@RequestBody LoginDTO dto,
                                          @RequestHeader(value = "Accept-Language" ,defaultValue = "UZ") AppLanguage language){
        return ResponseEntity.ok().body(authService.login(dto,language));
    }
}

