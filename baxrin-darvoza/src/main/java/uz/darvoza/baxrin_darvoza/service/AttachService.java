package uz.darvoza.baxrin_darvoza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.darvoza.baxrin_darvoza.dto.post.AttachDTO;
import uz.darvoza.baxrin_darvoza.entity.AttachEntity;
import uz.darvoza.baxrin_darvoza.entity.GateEntity;
import uz.darvoza.baxrin_darvoza.exps.AppBadException;
import uz.darvoza.baxrin_darvoza.repository.AttachRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachService {

    @Value("${attach.upload.folder}")
    private String folderName;
    @Value("${attach.upload.url}")
    private String attachUrl;

    @Autowired
    private AttachRepository attachRepository;

    public ResponseEntity<Resource> open(String id){
        AttachEntity attachEntity = getEntity(id);
            Path filePath = Paths.get(getPath(attachEntity)).normalize();
            Resource resource =null;
            try {
                resource=new UrlResource(filePath.toUri());
                if(resource.exists()){
                    throw new AppBadException("file not found"+ id);
                }
                String contentType = Files.probeContentType(filePath);
                System.out.println("contentType1 "+contentType);
                if (contentType == null) {
                    System.out.println("contentType2 "+contentType);
                    contentType = "application/octet-stream"; // Fallback content type
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
    }

    public AttachDTO upload(MultipartFile file, GateEntity gateEntity) {
        if (file.isEmpty()){
            throw new AppBadException("file not found");
        }
        try {
            String pathFolder=getYmDString();
            String key= UUID.randomUUID().toString();
            String extension=getExtension(file.getOriginalFilename());
            System.out.println(extension);

            File folder = new File(folderName + "/" +  pathFolder);
            if (!folder.exists()){
                boolean t =folder.mkdirs();
            }

            byte[] bytes = file.getBytes();
            System.out.println(bytes.length);
            System.out.println(bytes);
            Path path= Paths.get(folderName+"/"+ pathFolder + "/" +key+"."+extension);
            Files.write(path, bytes);

            AttachEntity attachEntity = new AttachEntity();
            attachEntity.setId(key+ "."+ extension);
            attachEntity.setPath(pathFolder);
            attachEntity.setSize(file.getSize());
            attachEntity.setExtension(extension);
            attachEntity.setOrigenName(file.getOriginalFilename());
            attachEntity.setVisible(true);
            attachEntity.setGate(gateEntity);
            attachRepository.save(attachEntity);

            return toDTO(attachEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getPath(AttachEntity entity) {
        return folderName + "/" + entity.getPath() + "/" + entity.getId();
    }
    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getId());
        attachDTO.setOriginName(entity.getOrigenName());
        attachDTO.setSize(entity.getSize());
        attachDTO.setExtension(entity.getExtension());
        attachDTO.setCreatedData(entity.getCreatedDate());
        attachDTO.setUrl(openURL(entity.getId()));
        return attachDTO;
    }
    private String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }
    public String openURL(String fileName) {
        return attachUrl + "/open/" + fileName;
    }
    public AttachEntity getEntity(String id) {
        Optional<AttachEntity> optional = attachRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadException("File not found");
        }
        return optional.get();
    }
    private String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1 || lastIndex == fileName.length() - 1) {
            return null; // kengaytma yo‘q yoki oxiri '.' bilan tugagan
        }
        return fileName.substring(lastIndex + 1);
    }
}
