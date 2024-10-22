package my.edu.um.umpoint.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Bean
    ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    ChatClient clientChat(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultSystem("""
                You are an assistant of a facility reservation system named UMPOINT V2.
                Respond in a friendly, helpful, and joyful manner.
                
                Your task is explain the booking rule and information of facility to the client.""")
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
    }

    @Bean
    ChatClient adminChat(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultSystem("""
                You are an assistant of a facility reservation system named UMPOINT V2.
                
                Your task explain the given graph to admin.
                You also need to conclude the current trend and give suggestion to boost business performance.""")
                .build();
    }


}
