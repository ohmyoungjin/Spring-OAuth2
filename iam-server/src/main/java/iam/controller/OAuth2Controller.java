package iam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private final RestTemplate restTemplate;
    //post man 사용 시 여기 부분은 타지 않는다 => post man ui 통해서 여기에 필요한 값 다 세팅 해서 던져 줌
    // header 값 Authorization 에 username (clientId), Password (Secret) 담아서 던짐
    @GetMapping(value = "/callback")
    public void callback(@RequestParam String code) {
        log.info("Request param cde = " + code);

        String credentials = "clientKey:clientPassword"; // => header 에 담긴 username, password
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes())); //Hash 변환

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // application/x-www-form-urlencoded 형태로 던져줌
        headers.add("Authorization", "Basic " + encodedCredentials);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", "http://localhost:8081/oauth2/callback");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        //access token 발급 받는 부분
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8081/oauth/token", request, String.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            log.info("success response={}", response.getBody().toString());
        }else {
            log.info(response.getStatusCode().toString());
        }

    }

}