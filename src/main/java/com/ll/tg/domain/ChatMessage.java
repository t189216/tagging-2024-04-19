package com.ll.tg.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ChatMessage {
    private long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;
    private String author;
    private String content;

    public ChatMessage(String author, String content) {
        this(ChatMessageIdGenerator.genNextId(), LocalDateTime.now(), author, content);
    }
}

class ChatMessageIdGenerator {
    private static long id = 0;

    public static long genNextId() {
        return ++id;
    }
}