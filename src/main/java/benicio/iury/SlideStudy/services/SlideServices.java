package benicio.iury.SlideStudy.services;

import benicio.iury.SlideStudy.util.ExtractText;
import benicio.iury.SlideStudy.util.PromptUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SlideServices {

    @Autowired
    private ExtractText extractText;

    @Autowired
    private PromptUtil promptUtil;

    @Value("${OPENAI_KEY}")
    private String apiKey;

    @Value("${OPENAI_URL}")
    private String apiUrl;


    public String ResponseIa(MultipartFile arq) throws IOException {
        String textoFinal = "";
        String nomeArquivo = arq.getOriginalFilename();

        //Verifica extensão do arquivo e pega o texto
        if(nomeArquivo != null && nomeArquivo.toLowerCase().endsWith(".pdf")){
            textoFinal = extractText.extrairTextPDF(arq);
        } else if (nomeArquivo != null && nomeArquivo.toLowerCase().endsWith(".pttx")) {
            textoFinal = extractText.extrairTextPPT(arq);
        }

        // FAZ A REQUISIÇÃO PARA A OPEN AI E CAPTURA A RESPOSTA DA IA
        
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(apiKey);

        String promptString;

        try{
            promptString = promptUtil.loadPrompt("prompt");
        }catch(IOException e){
            throw new IOException("Tivemos problemas com o prompt");
        }

        Map<String, Object> message = Map.of(
                "role", "user", "content", promptString +"\n\n" + textoFinal
                );

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", List.of(message),
                "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, header);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> body = mapper.readValue(response.getBody(), Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
            String resposta = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
            return resposta;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Erro ao chamar a OpenAI: " + e.getMessage();
        }

    }

}
