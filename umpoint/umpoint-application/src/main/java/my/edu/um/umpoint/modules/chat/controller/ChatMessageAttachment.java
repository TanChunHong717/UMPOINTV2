package my.edu.um.umpoint.modules.chat.controller;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ChatMessageAttachment implements Serializable{
    @NotNull
    public String name;
    @NotNull
    public String type;
    @NotNull
    public String url;
}
