package iam.auth;

import iam.entity.UserInfo;
import iam.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class IAMUserDetailService implements UserDetailsService {

    private final UserService userService;

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> user = userService.findByUser(username);
        log.info("loadUserByUsername user={}", user);
        if(user == null)
            throw new UsernameNotFoundException("유저 정보 찾을 수 없음");
        return user.get().toUser();
    }
}
