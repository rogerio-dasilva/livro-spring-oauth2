package br.com.casadocodigo.integracao.bookserver;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.casadocodigo.integracao.model.Livro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

@Service
public class BookserverService {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguracaoResource.class);
    
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public List<Livro> livrosFromCurrentUser() throws UsuarioSemAutorizacaoException {

        logger.info("### 8");
        String endpoint = "http://localhost:8081/api/v2/livros";

        try {
            Livro[] livros = oAuth2RestTemplate.getForObject(endpoint, Livro[].class);
            return listaFromArray(livros);

        } catch (HttpClientErrorException e) {
            System.out.println("erro" + e.getMessage());
            throw new UsuarioSemAutorizacaoException("não foi possível obter os livros do usuário");
        }

    }

    private List<Livro> listaFromArray(Livro[] livros) {
        List<Livro> lista = new ArrayList<>();

        for (Livro livro : livros) {
            lista.add(livro);
        }

        return lista;
    }

}
