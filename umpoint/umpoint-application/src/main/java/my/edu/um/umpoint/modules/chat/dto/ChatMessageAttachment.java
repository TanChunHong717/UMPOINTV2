package my.edu.um.umpoint.modules.chat.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

public class ChatMessageAttachment implements Serializable{
    @Serial
    private static final long serialVersionUID = -4835029904844331279L;

    @NotNull
    public String name;

    @NotNull
    public String type;

    @NotNull
    public String url;
}
