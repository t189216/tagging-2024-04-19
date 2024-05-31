package com.ll.tg.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {

    private Long id;

    @NotEmpty(message = "제목은 필수로 작성해야 합니다.")
    private String title;

    @NotEmpty(message = "내용은 필수로 작성해야 합니다.")
    private String content;
}