package uz.darvoza.baxrin_darvoza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.darvoza.baxrin_darvoza.dto.post.AttachDTO;
import uz.darvoza.baxrin_darvoza.entity.AttachEntity;
import uz.darvoza.baxrin_darvoza.service.AttachService;

@RestController
@RequestMapping("/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

//    @PostMapping("/api/v1/upload")
//    public ResponseEntity<AttachDTO> create(@RequestParam("file") MultipartFile file) {
//        return ResponseEntity.ok().body(attachService.upload(file));
//    }

    @GetMapping("/api/v1/open/{fileName}")
    public ResponseEntity<Resource> open(@PathVariable("fileName") String fileName) {
        return attachService.open(fileName);
    }
}
