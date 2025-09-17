package uz.darvoza.baxrin_darvoza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uz.darvoza.baxrin_darvoza.dto.AdminDTO;
import uz.darvoza.baxrin_darvoza.dto.AppResponse;
import uz.darvoza.baxrin_darvoza.dto.LoginDTO;
import uz.darvoza.baxrin_darvoza.dto.RegistrationDTO;
import uz.darvoza.baxrin_darvoza.entity.AdminEntity;
import uz.darvoza.baxrin_darvoza.enums.AppLanguage;
import uz.darvoza.baxrin_darvoza.enums.GeneralStatus;
import uz.darvoza.baxrin_darvoza.enums.Roles;
import uz.darvoza.baxrin_darvoza.exps.AppBadException;
import uz.darvoza.baxrin_darvoza.repository.AdminRepository;
import uz.darvoza.baxrin_darvoza.util.JwtUtil;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ResourceBundleService bundleService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailSendingService emailSendingService;
    @Autowired
    private AdminService adminService;

    public AppResponse<String> registration(RegistrationDTO dto, AppLanguage language) {
        Optional<AdminEntity> optional=adminRepository.findByUsernameAndVisibleTrue(dto.getUsername());

        if(optional.isPresent()){
           // throw new AppBadException(bundleMessageSource.getMessage("email.phone.exist",language));
            AdminEntity adminEntity=optional.get();
            if (adminEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)){
                adminRepository.delete(adminEntity);
            }else{
                throw new AppBadException(bundleService.getMessage("email.phone.exist",language));
            }
        }

        AdminEntity adminEntity=new AdminEntity();
        adminEntity.setName(dto.getName());
        adminEntity.setUsername(dto.getUsername());

        adminEntity.setRole(Roles.USER);
        adminEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        adminEntity.setStatus(GeneralStatus.IN_REGISTRATION);
        adminEntity.setVisible(true);
        adminRepository.save(adminEntity);

        emailSendingService.sendRegistrationEmail(adminEntity.getUsername(),adminEntity.getId(),language);

        return new AppResponse<>(bundleService.getMessage("email.confirm.send",language));
    }

    public AppResponse<String> verification(String token,AppLanguage language){
        try {
            Integer adminId=JwtUtil.decodeRegVerToken(token);
            AdminEntity entity=adminService.getById(adminId);
            if(entity.getStatus().equals(GeneralStatus.IN_REGISTRATION)){
                adminRepository.changeStatus(adminId,GeneralStatus.ACTIVE);
                return new AppResponse<>(bundleService.getMessage("email.activated",language));
            }
        }catch (Exception e){

        }

        throw new AppBadException(bundleService.getMessage("email.failed",language));
    }
    public AdminDTO login(LoginDTO dto, AppLanguage language){
        Optional<AdminEntity> optional=adminRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty()){
            throw new AppBadException(bundleService.getMessage("email.wrong",language));
        }
        AdminEntity adminEntity=optional.get();
        if (!bCryptPasswordEncoder.matches(dto.getPassword(),adminEntity.getPassword())){
            throw new AppBadException(bundleService.getMessage("email.wrong",language));
        }
        if (!adminEntity.getStatus().equals(GeneralStatus.ACTIVE)){
            throw new AppBadException(bundleService.getMessage("email.wrong",language));
        }
        String jwt=JwtUtil.encode(adminEntity.getUsername(),adminEntity.getId(),List.of(adminEntity.getRole()));
        AdminDTO adminDTO=new AdminDTO();
        adminDTO.setName(adminEntity.getName());
        adminDTO.setUsername(adminEntity.getUsername());
        adminDTO.setRole(adminEntity.getRole());
        adminDTO.setStatus(adminEntity.getStatus());
        adminDTO.setJwt(jwt);
        return adminDTO;
    }
}
