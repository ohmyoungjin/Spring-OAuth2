package iam.auth;

import iam.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class IAMAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    private final IAMUserDetailService iamUserDetailService;

    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("IAMAuthenticationProvider username : [{}] password : [{}]", username, password);
        // 권한 부분 담아줄 List 선언
        List<GrantedAuthority> authorityList = new ArrayList<>();
        //유저 검증 부분
        //Optional<UserInfo> findUser = userService.findByUser(username);
        //        if(findUser.isEmpty()) {
        //    throw new BadCredentialsException("해당 하는 아이디가 없습니다!");
        //}
        //권한 인자 값 넘겨주는 부분
        //authorityList.add(new SimpleGrantedAuthority(findUser.get().getUserRole()));
        // UsernamePasswordAuthenticationToken param : this.principal = principal; this.credentials = credentials; super.setAuthenticated(true);
        UserDetails userDetails = iamUserDetailService.loadUserByUsername(username);
        log.info("userDetails password : [{}]", userDetails.getPassword());
        //비밀번호 검증 부분
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않음");
        }

        authorityList.add(new SimpleGrantedAuthority(userDetails.getAuthorities().toString()));

        return new UsernamePasswordAuthenticationToken(username, password, authorityList);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
