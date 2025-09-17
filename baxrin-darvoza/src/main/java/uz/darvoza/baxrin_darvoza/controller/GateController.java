package uz.darvoza.baxrin_darvoza.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.darvoza.baxrin_darvoza.dto.AppResponse;
import uz.darvoza.baxrin_darvoza.dto.post.GateCreateDTO;
import uz.darvoza.baxrin_darvoza.dto.post.GateDTO;
import uz.darvoza.baxrin_darvoza.entity.GateEntity;
import uz.darvoza.baxrin_darvoza.service.AttachService;
import uz.darvoza.baxrin_darvoza.service.GateService;

import java.util.List;

@RestController
@RequestMapping("/gate")
public class GateController {
    @Autowired
    private GateService gateService;

    @PostMapping("/api/v1/create")
    public ResponseEntity<GateDTO>  create(@Valid @RequestPart(name = "post") GateCreateDTO dto,
                                           @RequestPart(name = "image") List<MultipartFile> file) {
        return ResponseEntity.ok().body(gateService.create(dto,file));
    }
    @GetMapping("/api/v1/get-id/{id}")
    public ResponseEntity<GateDTO>  getById(@PathVariable(value = "id") String id){
        return ResponseEntity.ok().body(gateService.getId(id));
    }

    @GetMapping("/api/v1/get-admin-all-post")
    public ResponseEntity<Page<GateDTO>>  getAdminAllPost(@RequestParam(value = "page",defaultValue ="1" ) int page,
                                                          @RequestParam(value = "size",defaultValue = "12") int size) {
        return ResponseEntity.ok().body(gateService.getAdminAllPost(page-1,size));
    }

    @GetMapping("/api/v1/get-all")
    public ResponseEntity<Page<GateDTO>> getAll(@RequestParam(value = "page",defaultValue ="1" ) int page,
                                                @RequestParam(value = "size",defaultValue = "12")  int size){
        return ResponseEntity.ok().body(gateService.getAll(page-1,size));
    }

    @PutMapping("/api/v1/update/{id}")
    public ResponseEntity<GateDTO> update(@Valid @PathVariable(value = "id") String id,
                                          @RequestBody GateCreateDTO dto) {
        return ResponseEntity.ok().body(gateService.update(id,dto));
    }

    @DeleteMapping("/api/v1/delete")
    public ResponseEntity<AppResponse<String>> delete(@RequestParam(value = "id") String id) {
        return ResponseEntity.ok().body(gateService.delete(id));
    }
}
