package study.oauth2.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.oauth2.entity.UserInfo;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final EntityManager em;

    @Transactional
    public Optional<UserInfo> findByUser(String name) {
        log.info("findByUser name=[{}]", name);
        return em.createQuery("select u from UserInfo u where u.name = :name", UserInfo.class)
                .setParameter("name", name)
                .getResultStream().findFirst();
    }
}
