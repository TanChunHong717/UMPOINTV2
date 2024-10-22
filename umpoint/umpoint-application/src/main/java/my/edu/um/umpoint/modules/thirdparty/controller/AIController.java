package my.edu.um.umpoint.modules.thirdparty.controller;

import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.service.service.SvcServiceService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@RestController
public class AIController {

    private static final String contextText = """
        Context information is below, surrounded by ---------------------
    
        ---------------------
        %s
        ---------------------
    
        Given the context and provided history information and not prior knowledge,
        reply to the user comment below. If the answer is not in the context, inform
        the user that you can't answer the question.
    """;

    @Autowired
    private SpcSpaceService spcSpaceService;

    @Autowired
    private SvcServiceService svcServiceService;

    @Autowired
    private AccAccommodationService accAccommodationService;

    private final ChatClient chatClient;

    public AIController(@Qualifier("clientChat") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/client/chat")
    @RequiresAuthentication
    public Flux<String> generation(
            String chatId,
            String message,
            @RequestParam(required = false) Long spaceId,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Long accommodationId
    ) {
        String contextMessage;
        if (spaceId != null)
            contextMessage = String.format(contextText, spcSpaceService.get(spaceId)) + message;
        else if (serviceId != null)
            contextMessage = String.format(contextText, svcServiceService.get(serviceId)) + message;
        else if (accommodationId != null)
            contextMessage = String.format(contextText, accAccommodationService.get(accommodationId)) + message;
        else
            contextMessage = message;

        return this.chatClient.prompt()
                .user(contextMessage)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .stream().content();
    }
}
