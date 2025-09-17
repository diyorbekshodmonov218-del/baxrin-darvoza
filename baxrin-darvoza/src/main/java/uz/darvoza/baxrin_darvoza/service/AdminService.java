package uz.darvoza.baxrin_darvoza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.darvoza.baxrin_darvoza.dto.AdminDTO;
import uz.darvoza.baxrin_darvoza.dto.AppResponse;
import uz.darvoza.baxrin_darvoza.entity.AdminEntity;
import uz.darvoza.baxrin_darvoza.enums.Roles;
import uz.darvoza.baxrin_darvoza.exps.AppBadException;
import uz.darvoza.baxrin_darvoza.repository.AdminRepository;
import uz.darvoza.baxrin_darvoza.util.SpringSecurityUtil;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public List<AdminDTO> getAllAdmins() {

        if (!SpringSecurityUtil.hasRole(Roles.SUPER_ADMIN)) {
            throw new AppBadException("Sizda SUPER_ADMIN huquqi yo'q");
        }
        List<AdminEntity> adminEntityList = adminRepository.findAllByRoleAndVisibleTrue(Roles.ADMIN);
        if(adminEntityList.isEmpty()){
            return Collections.emptyList();
        }
        return adminEntityList.stream().map(this::toDTO).collect(Collectors.toList());
    }
    public AdminDTO getAdminId(Integer adminId) {
        AdminEntity adminEntity=adminRepository.findById(adminId).get();
        if (!SpringSecurityUtil.hasRole(Roles.SUPER_ADMIN)) {
            throw new AppBadException("Sizda SUPER_ADMIN huquqi yo'q");
        }

        return toDTO(adminEntity);
    }
    public AppResponse<String> deleteAdmin(Integer adminId) {
        adminRepository.deleteById(adminId,false);
        return new AppResponse("Admin Deleted Successfully");
    }

    public AdminDTO toDTO(AdminEntity adminEntity) {
        AdminDTO adminDTO=new AdminDTO();
        adminDTO.setName(adminEntity.getName());
        adminDTO.setId(adminEntity.getId());
        adminDTO.setUsername(adminEntity.getUsername());
        adminDTO.setRole(adminEntity.getRole());
        adminDTO.setStatus(adminEntity.getStatus());
        return adminDTO;
    }

    public AdminEntity getById(Integer adminId){
        return adminRepository.findByIdAndVisibleTrue(adminId).orElseThrow(() ->{
            throw new AppBadException("profile not found");
        });
    }
}
