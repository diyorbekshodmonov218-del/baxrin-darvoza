package uz.darvoza.baxrin_darvoza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.darvoza.baxrin_darvoza.dto.AdminDTO;
import uz.darvoza.baxrin_darvoza.dto.AppResponse;
import uz.darvoza.baxrin_darvoza.enums.Roles;
import uz.darvoza.baxrin_darvoza.repository.AdminRepository;
import uz.darvoza.baxrin_darvoza.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/api/v1/get-all-admin")
    public ResponseEntity<List<AdminDTO>> getAllAdminsByRole(){
        return ResponseEntity.ok().body(adminService.getAllAdmins());
    }
    @GetMapping("/api/v1/get-admin")
    public ResponseEntity<AdminDTO> getAdminById(@RequestParam(value = "id") Integer adminId){
        return ResponseEntity.ok().body(adminService.getAdminId(adminId));
    }
    @DeleteMapping("/api/v1/delete")
    public ResponseEntity<AppResponse<String>> delete(@RequestParam(value = "id") Integer id){
        return ResponseEntity.ok().body(adminService.deleteAdmin(id));
    }
}
