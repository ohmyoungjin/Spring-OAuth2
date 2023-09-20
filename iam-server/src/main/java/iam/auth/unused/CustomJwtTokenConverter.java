package iam.auth.unused;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
/* /oauth/token URI response에 토큰 발급과 함께 다른 정보들을 보낼 수 있게 함 */
public class CustomJwtTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        //authentication.getDetails() 하면 값을 못 가져온다. 상위
        Object details = authentication.getUserAuthentication().getDetails();
        log.info("enhance details={}", details);
        Map<String, Object> claims = new LinkedHashMap<String, Object>(
                accessToken.getAdditionalInformation());
        //토큰 값에 같이 보낼 정보 저장
        claims.put("userDetails", details);
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(claims);

        return super.enhance(customAccessToken, authentication);
    }

}
