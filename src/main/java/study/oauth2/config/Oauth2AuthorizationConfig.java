package study.oauth2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import study.oauth2.auth.IAMUserDetailService;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@EnableAuthorizationServer    // OAuth2 인증 처리 권한 서버 선언 여러 endpoints 자동으로 생성 됨 =>
// [ /oauth/authorize, /oauth/token, /oauth/confirm_access, /oauth/error, /oauth/check_token, /oauth/token_key ]
public class Oauth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DataSource dataSource;
    private final IAMUserDetailService iamUserDetailService;

    //endPoint 추가를 위한 설정
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //.tokenStore(tokenStore())
                .authenticationManager(authenticationManager);
                //.accessTokenConverter(jwtAccessTokenConverter())
                //.userDetailsService(iamUserDetailService);

    }

    /* token store로 JWTTokenStore를 사용하겠다 */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /* JWT 디코딩 하기 위한 설정 */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("jwtKey");
        return jwtAccessTokenConverter;
    }

    /* 클라이언트 대한 정보를 설정하는 부분 */
    /* clientId, clientKey 체크 */
    /* jdbc(DataBase)를 이용하는 방식 */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
    }

//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("clientKey") // 클라이언트 아이디
//                .secret(passwordEncoder.encode("clientPassword")) // 클라이언트 시크릿
//                .redirectUris("http://localhost:8081/oauth2/callback")
//                .authorizedGrantTypes("authorization_code", "password") // grant Type
//                .scopes("read", "write")    // 해당 클라이언트의 접근 범위
//                .accessTokenValiditySeconds(60 * 60 * 4)            // access token 유효 기간 (초 단위)
//                .refreshTokenValiditySeconds(60 * 60 * 24 * 120)   // refresh token 유효 기간 (초 단위)
//                .autoApprove(true);
//    }


}
