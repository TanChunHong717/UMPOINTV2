package my.edu.um.umpoint.modules.chat.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class ChatMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 4688943558580002457L;

    public String message;

    public Long replyMessageId;

    public List<ChatMessageAttachment> attachments;

    public String returnMessage;
}
