package benicio.iury.SlideStudy.controller;

import benicio.iury.SlideStudy.DTOS.SlideDTO;
import benicio.iury.SlideStudy.services.SlideServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SlideController {

    @Autowired
    private SlideServices slideServices;


    @PostMapping("/upload-slide")
    public ResponseEntity<String> uploadSlide(@RequestParam("slideFile") SlideDTO slide) throws IOException {
        if(slide.isEmpty()){
            return ResponseEntity.badRequest().body("nenhum arquivo enviado");
        }

        String Response = slideServices.ResponseIa(slide.getSlide().get());

        return ResponseEntity.ok(Response);

    }

}
