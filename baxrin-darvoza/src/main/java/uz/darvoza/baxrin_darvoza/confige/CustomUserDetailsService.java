package uz.darvoza.baxrin_darvoza.confige;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.darvoza.baxrin_darvoza.entity.AdminEntity;
import uz.darvoza.baxrin_darvoza.repository.AdminRepository;

@Service
@Primary // Added @Primary to resolve duplicate UserDetailsService bean conflict
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminEntity admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return new CustomUserDetails(admin);
    }
}
