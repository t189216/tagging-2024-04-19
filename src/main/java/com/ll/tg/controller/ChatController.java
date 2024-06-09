package com.ll.tg.controller;

import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import com.ll.tg.component.RsData;
import com.ll.tg.domain.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatController {
    private List<ChatMessage> chatMessages = new ArrayList<>();

    private Translator translator;

    public ChatController(@Value("${translator.authKey}") String authKey) throws Exception {
        this.translator = new Translator(authKey);
    }

    public record WriteMessageRequest(String author, String content) {
    }

    public record WriteMessageResponse(long id) {
    }

    @GetMapping("/room")
    public String showRoom() {
        return "domain/chat/room";
    }

    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<WriteMessageResponse> writeMessage(@RequestBody WriteMessageRequest req) {
        ChatMessage message = new ChatMessage(req.author(), req.content());

        chatMessages.add(message);

        return new RsData<>(
                "S-1",
                "메세지가 작성되었습니다.",
                new WriteMessageResponse(message.getId())
        );
    }

    public record MessagesRequest(Long fromId) {
    }

    public record MessagesResponse(List<ChatMessage> messages, long count) {
    }

    @GetMapping("/messages")
    @ResponseBody
    public RsData<MessagesResponse> messages(@RequestParam(required = false) Long fromId) {
        List<ChatMessage> messages = chatMessages;

        if (fromId != null) {
            int index = IntStream.range(0, messages.size())
                    .filter(i -> chatMessages.get(i).getId() == fromId)
                    .findFirst()
                    .orElse(-1);

            if (index != -1) {
                messages = messages.subList(index + 1, messages.size());
            }
        }

        return new RsData<>(
                "S-1",
                "성공",
                new MessagesResponse(messages, messages.size())
        );
    }

    public record TranslateRequest(String text) {
    }

    public record TranslateResponse(String translatedText) {
    }

    @PostMapping("/translate")
    @ResponseBody
    public RsData<TranslateResponse> translate(@RequestBody TranslateRequest req) {
        try {
            // translator가 null인지 확인하고 초기화되었는지 확인
            if (translator == null) {
                throw new IllegalStateException("Translator is not initialized properly");
            }
            // 번역할 텍스트가 null이 아니고 비어 있지 않은 경우에만 번역 요청 보내기
            if (req.text() != null && !req.text().isEmpty()) {
                TextResult result = translator.translateText(req.text(), null, "en-US");
                return new RsData<>(
                        "S-1",
                        "번역 성공",
                        new TranslateResponse(result.getText())
                );
            } else {
                // 번역할 텍스트가 비어 있을 경우에는 번역 실패 처리
                return new RsData<>(
                        "F-1",
                        "번역할 텍스트가 없습니다.",
                        null
                );
            }
        } catch (Exception e) {
            log.error("번역 중 오류 발생", e);
            return new RsData<>(
                    "F-1",
                    "번역 실패",
                    null
            );
        }
    }
}