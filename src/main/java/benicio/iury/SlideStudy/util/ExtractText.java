package benicio.iury.SlideStudy.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ExtractText {

    public String extrairTextPDF(MultipartFile file) throws IOException {
        try (InputStream input = file.getInputStream();
             PDDocument document = PDDocument.load(input)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    public String extrairTextPPT(MultipartFile file) throws IOException{
    String textoFinal;

        InputStream input = file.getInputStream();

        try(XMLSlideShow PowerPoint = new XMLSlideShow(input);){
            StringBuilder texto = new StringBuilder();

            for (XSLFSlide slide : PowerPoint.getSlides()){
               for (XSLFShape shape : slide.getShapes()){
                   if(shape instanceof XSLFTextShape textShape){
                       texto.append(textShape.getText()).append("\n");
                   }
               }
           }

            textoFinal = texto.toString();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return textoFinal;
    }

}
