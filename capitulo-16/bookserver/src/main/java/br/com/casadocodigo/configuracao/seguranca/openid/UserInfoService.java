package br.com.casadocodigo.configuracao.seguranca.openid;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@Service
public class UserInfoService {

    @Autowired
    private DiscoveryDocument discovery;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<String, String> getUserInfoFor(OAuth2AccessToken accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<MultiValueMap<String,String>> requestEntity = new RequestEntity<>(
                getHeader(accessToken.getValue()),
                HttpMethod.GET,
                URI.create(discovery.getUserInfoEndpoint())
        );

        ResponseEntity<Map> result = restTemplate.exchange(requestEntity, Map.class);
        
        if(result.getStatusCode().is2xxSuccessful()){
            return result.getBody();
        }

        throw new RuntimeException("Não foi possível obtem dados do usuário");
    }

    private MultiValueMap getHeader(String accessToken) {
        MultiValueMap<String, String> httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer " + accessToken);

        return httpHeaders;
    }

}
