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
    // /oauth/token 호출 하게 되면 이 부분은 사용 하지 않음
    // http://localhost:8081/oauth/authorize?client_id=${client_id}&redirect_uri=${redirect_url}&response_type=code&scope=read
    // ex) http://localhost:8081/oauth/authorize?client_id=clientKey&redirect_uri=http://localhost:8081/oauth2/callback&response_type=code&scope=read
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