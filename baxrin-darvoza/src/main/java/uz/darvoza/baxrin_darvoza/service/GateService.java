package uz.darvoza.baxrin_darvoza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import uz.darvoza.baxrin_darvoza.dto.AppResponse;
import uz.darvoza.baxrin_darvoza.dto.post.AttachDTO;
import uz.darvoza.baxrin_darvoza.dto.post.GateCreateDTO;
import uz.darvoza.baxrin_darvoza.dto.post.GateDTO;
import uz.darvoza.baxrin_darvoza.entity.AttachEntity;
import uz.darvoza.baxrin_darvoza.entity.GateEntity;
import uz.darvoza.baxrin_darvoza.enums.AppLanguage;
import uz.darvoza.baxrin_darvoza.enums.GeneralStatus;
import uz.darvoza.baxrin_darvoza.enums.Roles;
import uz.darvoza.baxrin_darvoza.exps.AppBadException;
import uz.darvoza.baxrin_darvoza.repository.AttachRepository;
import uz.darvoza.baxrin_darvoza.repository.GateRepository;
import uz.darvoza.baxrin_darvoza.util.SpringSecurityUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GateService {
    @Autowired
    private GateRepository gateRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private AttachRepository attachRepository;


    public GateDTO create(GateCreateDTO gateCreateDTO, List<MultipartFile> file) {
        Integer adminId=SpringSecurityUtil.getCurrentUserId();
        GateEntity  gateEntity = new GateEntity();
        gateEntity.setName(gateCreateDTO.getName());
        gateEntity.setDescription(gateCreateDTO.getDescription());
        gateEntity.setStatus(GeneralStatus.ACTIVE);
        gateEntity.setCreatedDate(LocalDateTime.now());
        gateEntity.setAdminId(adminId);
        gateEntity.setVisible(true);
        gateRepository.save(gateEntity);

        List<AttachDTO> attachDTOList=file.stream().map(d -> toAttach(d,gateEntity)).toList();

        return toDTO(gateEntity,attachDTOList);
    }

    public GateDTO getId(String id) {
        Optional<GateEntity > optional = gateRepository.findById(id);
        if(optional.isEmpty()){
            throw new RuntimeException("post not found");
        }
        GateEntity  gateEntity = optional.get();
        return toDTO(gateEntity);
    }

    public AttachDTO toAttach(MultipartFile file,GateEntity gateEntity) {
        return attachService.upload(file,gateEntity);
    }
    public Page<GateDTO> getAdminAllPost(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Integer adminId= SpringSecurityUtil.getCurrentUserId();
        Page<String> pageId=gateRepository.findPageIds(adminId,pageable);
        if (pageId.isEmpty()){
            return Page.empty(pageable);
        }
        List<GateEntity> gateEntityList=gateRepository.findAllWithAttachByIds(pageId.getContent());
        List<GateDTO> gateDTOS=gateEntityList.stream().map(this::toDTO).collect(Collectors.toList());

        return new PageImpl<>(gateDTOS,pageable,pageId.getTotalElements());
    }

    public Page<GateDTO> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GateEntity> entityPage=gateRepository.findAll(pageable);
        Page<GateDTO> gateDTOS=entityPage.map(this::toDTO);

        return gateDTOS;
    }

    public GateDTO update(String id,GateCreateDTO dto) {
        GateEntity gateEntity = gateRepository.findById(id).get();
        Integer adminId=SpringSecurityUtil.getCurrentUserId();
        if (SpringSecurityUtil.hasRole(Roles.USER) && !gateEntity.getAdminId().equals(adminId)){
            throw new AppBadException("You are not allowed to update this post");
        }

        gateEntity.setName(dto.getName());
        gateEntity.setDescription(dto.getDescription());
        gateRepository.save(gateEntity);

        return toDTO(gateEntity);
    }
    public AppResponse<String> delete(String id) {
        GateEntity gateEntity = gateRepository.findById(id).get();
        if (!gateEntity.getAdminId().equals(SpringSecurityUtil.getCurrentUserId())) {
            throw new AppBadException("You are not allowed to delete this post");
        }
        gateRepository.delete(gateEntity.getId());
        return new AppResponse<>("delete success");
    }

    public GateDTO toDTO(GateEntity gateEntity) {
        GateDTO gateDTO = new GateDTO();
        gateDTO.setId(gateEntity.getId());
        gateDTO.setName(gateEntity.getName());
        gateDTO.setDescription(gateEntity.getDescription());
        gateDTO.setStatus(gateEntity.getStatus());
        gateDTO.setCreatedDate(LocalDateTime.now());
        gateDTO.setAttachs(gateEntity.getAttachs()
                .stream().map(g -> attachService.toDTO(g))
                .collect(Collectors.toList()));

        return gateDTO;
    }

    public GateDTO toDTO(GateEntity gateEntity,List<AttachDTO> attachDTO) {
        GateDTO gateDTO = new GateDTO();
        gateDTO.setId(gateEntity.getId());
        gateDTO.setName(gateEntity.getName());
        gateDTO.setDescription(gateEntity.getDescription());
        gateDTO.setStatus(gateEntity.getStatus());
        gateDTO.setCreatedDate(LocalDateTime.now());
        gateDTO.setAttachs(attachDTO);

        return gateDTO;
    }
}
