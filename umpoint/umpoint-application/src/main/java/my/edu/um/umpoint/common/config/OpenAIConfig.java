package my.edu.um.umpoint.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                You are an assistant of a facility reservation system named UMPOINT V2 in University of Malaya, Kuala Lumpur, Malaysia.
                Respond in a friendly, helpful, and joyful manner.
                
                Your task is explain the booking rule and information of facility to the client.
                All prices are using the monetary unit of RM (Ringgit Malaysia).
                All times are at Malaysian Standard Time (UTC +8).
                The current time is """ + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
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
