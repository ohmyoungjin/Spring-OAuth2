package iam.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@EnableResourceServer //권한 인증이 필요한 서버 어디에서 권한 인증을 받을지는 application.yml 기재돼있음.
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /* oauth Server 랑 동일한 key 값을 가지고 있어야 한다 !*/
    private final String jwtSignKey = "fn2jnbgfk2jbgfk2j";

    /* token store로 JWTTokenStore를 사용하겠다 */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /* JWT 디코딩 하기 위한 설정 */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(jwtSignKey);
        return jwtAccessTokenConverter;
    }

    /*자원 서버(@EnableResourceServer annotation 붙은) 접근 권한 설정*/
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v1/users").access("#oauth2.hasScope('read')") // security 검증 받을 uri(/v1/users) 권한은 read 일 때만 접근 가능 ! => 인가 기능
                .anyRequest().authenticated();
    }
}
