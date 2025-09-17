package uz.darvoza.baxrin_darvoza.confige;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.darvoza.baxrin_darvoza.entity.AdminEntity;
import uz.darvoza.baxrin_darvoza.repository.AdminRepository;

import java.util.List;
import java.util.Optional;
@Service
public class CustomUserDetailsServise implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AdminEntity> optional=adminRepository.findByUsernameAndVisibleTrue(username);
        if (optional.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        AdminEntity adminEntity=optional.get();
        CustomUserDetails userDetails=new CustomUserDetails(adminEntity);
        return userDetails;
    }
}
