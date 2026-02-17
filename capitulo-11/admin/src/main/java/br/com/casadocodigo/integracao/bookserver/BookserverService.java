package br.com.casadocodigo.integracao.bookserver;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BookserverService {

    @Autowired
    private ClientCredentialsTokenService clientCredentialsTokenService;

    public long getQuantidadeDeLivrosCadastrados() {
        String token = clientCredentialsTokenService.getToken().getAccessToken();
        
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + token);
        
        String endPoint = "http://localhost:8080/api/v2/admin/total_livros";
        
        RequestEntity<Object> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(endPoint));
        
        return sendRequest(request);
    }

    private long sendRequest(RequestEntity<Object> request) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Long> resposta = restTemplate.exchange(request, Long.class);
            if (resposta.getStatusCode().is2xxSuccessful()) {
                return resposta.getBody();
            } else {
                throw new RuntimeException("sem sucesso");
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("não foi possível obter os livros do usuário");
        }

    }

}
