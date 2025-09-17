package uz.darvoza.baxrin_darvoza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.darvoza.baxrin_darvoza.entity.EmailHistroyEntity;
import uz.darvoza.baxrin_darvoza.enums.AppLanguage;
import uz.darvoza.baxrin_darvoza.enums.EmailType;
import uz.darvoza.baxrin_darvoza.exps.AppBadException;
import uz.darvoza.baxrin_darvoza.repository.EmailHistroyRepository;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistroyRepository emailHistroyRepository;
    @Autowired
    private ResourceBundleService bundleService;
    public void create(String email, String code, EmailType type){
        EmailHistroyEntity emailHistroyEntity = new EmailHistroyEntity();
        emailHistroyEntity.setEmail(email);
        emailHistroyEntity.setCode(code);
        emailHistroyEntity.setEmailType(type);
        emailHistroyEntity.setAttemptCount(0);
        emailHistroyEntity.setCreatedDate(LocalDateTime.now());
        emailHistroyRepository.save(emailHistroyEntity);
    }
    public Long getEmailCount(String email){
        LocalDateTime now = LocalDateTime.now();
        return emailHistroyRepository.countByEmailAndCreatedDateBetween(email,now.minusMinutes(1),now);
    }
    public void check(String email, String code, AppLanguage language){
        Optional<EmailHistroyEntity> optional=emailHistroyRepository.findTop1ByEmailOrderByCreatedDateDesc(email);
        if (optional.isEmpty()){
            throw new AppBadException(bundleService.getMessage("profile.not.found",language));
        }
        EmailHistroyEntity emailHistroyEntity=optional.get();
        if (emailHistroyEntity.getAttemptCount() >= 3){
            throw new AppBadException(bundleService.getMessage("profile.not.found",language));
        }
        if (!emailHistroyEntity.getCode().equals(code)){
            emailHistroyRepository.updateAttemptCount(emailHistroyEntity.getId());
            throw new AppBadException(bundleService.getMessage("profile.not.found",language));
        }
        LocalDateTime expDate=emailHistroyEntity.getCreatedDate().plusMinutes(2);
        if (LocalDateTime.now().isAfter(expDate)){
            throw new AppBadException(bundleService.getMessage("profile.expired",language));
        }
    }
}
