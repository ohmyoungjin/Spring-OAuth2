package study.oauth2.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import study.oauth2.entity.UserInfo;
import study.oauth2.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class IAMAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("IAMAuthenticationProvider username : [{}] password : [{}]", username, password);
        //유저 검증 부분
        Optional<UserInfo> findUser = userService.findByUser(username);
        if(findUser.isEmpty()) {
            throw new BadCredentialsException("해당 하는 아이디가 없습니다!");
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        log.info("권한=[{}] [{}]", findUser.get().getUserRole(), new SimpleGrantedAuthority(findUser.get().getUserRole())) ;
        //권한 인자 값 넘겨주는 부분
        authorityList.add(new SimpleGrantedAuthority(findUser.get().getUserRole()));
        // UsernamePasswordAuthenticationToken param : this.principal = principal; this.credentials = credentials; super.setAuthenticated(true);
        return new UsernamePasswordAuthenticationToken(username, password, authorityList);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
