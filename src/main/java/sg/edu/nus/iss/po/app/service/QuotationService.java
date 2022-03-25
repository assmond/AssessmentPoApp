package sg.edu.nus.iss.po.app.service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sg.edu.nus.iss.po.app.model.Quotation;
import sg.edu.nus.iss.po.app.util.QuotationUtil;

import java.util.List;
import java.util.Optional;

@Service
public class QuotationService {

    private static final Logger logger = LoggerFactory.getLogger(QuotationService.class);

    private static final String QUOTATION_SERVER = "https://quotation.chuklee.com";

    public Optional<Quotation> getQuotations(List<String> items) {
        String endpoint = UriComponentsBuilder.fromUriString(QUOTATION_SERVER + "/quotation").toUriString();
        JsonArrayBuilder itemsJsonBuilder = Json.createArrayBuilder();
        items.forEach(v -> itemsJsonBuilder.add(v));

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<String> req = RequestEntity.post(endpoint)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(itemsJsonBuilder.build().toString());

        try {
            ResponseEntity<String> response = restTemplate.exchange(req, String.class);
            logger.info(">>> QUOTATION SERVER RETURNED: " + response.getBody());
            if (response.getStatusCodeValue() == 200) {
                return Optional.ofNullable(QuotationUtil.create(response.getBody()));
            } else {
                logger.info(">>> getQuotations: DID NOT RECEIVE STATUS 200, INSTEAD GOT " + response.getStatusCodeValue());
            }
        } catch (RestClientException e) {
            logger.error(">>> RestClientException @ getQuotations: " + e.getMessage());
        } catch (JsonException e) {
            logger.error(">>> JsonException @ getQuotations: " + e.getMessage());
        }
        
        return Optional.empty();
    }
   
}
