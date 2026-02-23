package br.com.casadocodigo.integracao.bookserver;

import br.com.casadocodigo.configuracao.seguranca.UsuarioLogado;
import br.com.casadocodigo.usuarios.AcessoBookserver;
import br.com.casadocodigo.usuarios.Usuario;
import br.com.casadocodigo.usuarios.UsuariosRepository;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

@Service
public class BookserverClientTokenServices implements ClientTokenServices{
    
    private static final Logger logger = LoggerFactory.getLogger(BookserverClientTokenServices.class);
    
    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails oaprd, Authentication a) {
        logger.info("### 3");
        UsuarioLogado usuarioLogado = (UsuarioLogado) a.getPrincipal();
        
        Usuario usuario = usuariosRepository.findById(usuarioLogado.getId());
        logger.info("### 3 usuario=" + usuario.getNome());
        
        String acessToken = usuario.getAcessoBookserver().getAcessoToken();
        Calendar dataDeExpiracao = usuario.getAcessoBookserver().getDataDeExpiracao();
        
        logger.info("### 3 acessToken=" + acessToken);
        if(acessToken == null){
            return null;
        }
        
        DefaultOAuth2AccessToken oauth2AcessToken =new DefaultOAuth2AccessToken(acessToken);
        oauth2AcessToken.setExpiration(dataDeExpiracao.getTime());
        
        return oauth2AcessToken;
    }

    @Override
    public void saveAccessToken(OAuth2ProtectedResourceDetails oaprd, Authentication a, OAuth2AccessToken oaat) {
        logger.info("### 4");
        AcessoBookserver acessoBookserver = new AcessoBookserver();
        acessoBookserver.setAcessoToken(oaat.getValue());
        
        Calendar dataDeExpiracao = Calendar.getInstance();
        dataDeExpiracao.setTime(oaat.getExpiration());
        
        acessoBookserver.setDataDeExpiracao(dataDeExpiracao);
        
        UsuarioLogado usuarioLogado = (UsuarioLogado) a.getPrincipal();
        Usuario usuario = usuariosRepository.findById(usuarioLogado.getId());
        usuario.setAcessoBookserver(acessoBookserver);
        
        logger.info("### 4 usuario=" + usuario.getNome());
        usuariosRepository.save(usuario);
    }

    @Override
    public void removeAccessToken(OAuth2ProtectedResourceDetails oaprd, Authentication a) {
        logger.info("### 5");
        UsuarioLogado usuarioLogado = (UsuarioLogado) a.getPrincipal();
        Usuario usuario = usuariosRepository.findById(usuarioLogado.getId());
        
        logger.info("### 5 usuario=" + usuario.getNome());
        usuario.setAcessoBookserver(null);
        usuariosRepository.save(usuario);
    }
    
}
