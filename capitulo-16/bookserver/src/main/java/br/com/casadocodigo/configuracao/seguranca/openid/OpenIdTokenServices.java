package br.com.casadocodigo.configuracao.seguranca.openid;

import br.com.casadocodigo.usuarios.AutenticacaoOpenid;
import br.com.casadocodigo.usuarios.IdentificadorDeAutorizacao;
import br.com.casadocodigo.usuarios.RepositorioDeUsuarios;
import br.com.casadocodigo.usuarios.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OpenIdTokenServices {
    
    @Autowired
    private RepositorioDeUsuarios repositorioDeUsuarios;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserInfoService userInfoService;
    
    private Date obterDatetime(long timestamp){
        return new Date(timestamp * 1000);
    }
    
    public void saveAccessToken(OAuth2AccessToken accessToken){
        TokenIdClaims claims = TokenIdClaims.extrairClaims(objectMapper, accessToken);
        
        Optional<Usuario> usuarioAutenticado = repositorioDeUsuarios.buscarUsuarioAutenticado(new IdentificadorDeAutorizacao(claims.getSubjectIdentifier()));
        
        Usuario usuario = usuarioAutenticado.orElseGet(()->{
            Usuario novoUsuario = new Usuario(claims.getEmail(), claims.getEmail());
            
            new AutenticacaoOpenid(
                    novoUsuario, 
                    new IdentificadorDeAutorizacao(claims.getSubjectIdentifier()), 
                    claims.getIssuerIdentifier(), 
                    obterDatetime(claims.getExpirationTime()));
            
            return novoUsuario;
        });
        
        if(usuario.getAutenticacaoOpenid().expirou()){
            AutenticacaoOpenid a = usuario.getAutenticacaoOpenid();
            a.setValidade(obterDatetime(claims.getExpirationTime()));
        }
        
        Map<String,String> userInfo =  userInfoService.getUserInfoFor(accessToken);
        String nomeUsuario = userInfo.get("name");
        
        usuario.alterarNome(nomeUsuario);
        
        repositorioDeUsuarios.registrar(usuario);
    }
}
