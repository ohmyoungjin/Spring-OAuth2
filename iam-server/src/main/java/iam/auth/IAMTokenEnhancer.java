package iam.auth;

import iam.dto.Member;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * accessToken 과 함께 보낼 값 저장 하는 부분 해당 부분 사용하면 CustomJwtTokenConverter 사용 X
 * 해당 기능 사용하기 위해서 AuthConfig 에 class 추가 해줘야 함 ex) tokenEnhancer(tokenEnhancerChain)
 */
public class IAMTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(final OAuth2AccessToken accessToken,
                                     final OAuth2Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();

        Member member = (Member) authentication.getUserAuthentication().getDetails();

        userInfo.put("userName", member.getUserName());
        userInfo.put("userPassword", member.getUserPassword());
        userInfo.put("roles", member.getRole());
        //형 변환 후 token 과 같이 보낼 값 저장 ~
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(userInfo);
        return accessToken;
    }
}
