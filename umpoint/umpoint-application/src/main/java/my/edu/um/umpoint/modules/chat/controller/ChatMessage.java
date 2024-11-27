package my.edu.um.umpoint.modules.chat.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class ChatMessage implements Serializable{
    public String message;
    public Long replyMessageId;
    public List<ChatMessageAttachment> attachments;
    public String returnMessage;
}
