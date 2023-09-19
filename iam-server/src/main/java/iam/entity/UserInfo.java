package iam.entity;

import iam.auth.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


import javax.persistence.*;


@Entity
@Getter
@ToString
@Table(name="user_info")
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "user_role")
    private String userRole;

    public User toUser() {
        return User.builder()
                .id(id)
                .name(name)
                .password(password)
                .authorities(userRole)
                .build();

    }
}
