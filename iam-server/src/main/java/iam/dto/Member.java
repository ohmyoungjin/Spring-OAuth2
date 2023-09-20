package iam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Member {

    private Long userId;
    private String userName;
    private String userPassword;
    private String role;

    @Builder
    public Member(Long userId, String userName, String userPassword, String role) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.role = role;
    }
}
