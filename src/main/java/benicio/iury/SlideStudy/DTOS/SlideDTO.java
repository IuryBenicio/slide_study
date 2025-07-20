package benicio.iury.SlideStudy.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlideDTO {
    private Optional <MultipartFile> slide;

    public boolean isEmpty() {
        if(this.slide.isEmpty()){
        return true;
        }else {
        return false;
        }
    }
}
