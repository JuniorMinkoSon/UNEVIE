package ecom_blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WaveService {

    @Value("${wave.api.key:wave_ci_prod_placeholder}")
    private String apiKey;

    @Value("${wave.base.url:https://api.wave.com/v1}")
    private String baseUrl;

    @Value("${app.base.url:http://localhost:8082}")
    private String appBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Crée une session de paiement Wave et retourne l'URL de paiement.
     */
    public String createCheckoutSession(double amount, Long commandeId) {
        String url = baseUrl + "/checkout/sessions";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("amount", String.valueOf((int) amount));
        body.put("currency", "XOF");
        body.put("error_url", appBaseUrl + "/projets?error=paiement");
        body.put("success_url", appBaseUrl + "/projets?success=commande");
        body.put("client_reference", "COMMANDE_" + commandeId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("wave_launch_url")) {
                    return (String) responseBody.get("wave_launch_url");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur creation session Wave: " + e.getMessage());
        }

        return null;
    }
}
