package com.ll.tg.controller;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignupForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "ID는 필수로 작성해야 합니다.")
    private String username;

    @NotEmpty(message = "이메일은 필수로 설정해야 합니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8자리 이상 16자리 이하로 설정해야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", message = "비밀번호는 영문과 숫자를 포함해야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 입력값입니다.")
    private String passwordConfirm;
}