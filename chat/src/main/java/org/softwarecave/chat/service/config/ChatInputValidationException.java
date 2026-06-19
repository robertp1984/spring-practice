package org.softwarecave.chat.service.config;

public class ChatInputValidationException extends RuntimeException {
    public ChatInputValidationException(String message) {
        super(message);
    }

    public ChatInputValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
