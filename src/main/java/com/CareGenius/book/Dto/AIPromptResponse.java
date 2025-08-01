package com.CareGenius.book.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class AIPromptResponse {

    private List<Choice> choices;

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Choice {
        private Message message;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Message {
        private String content;
    }

}
