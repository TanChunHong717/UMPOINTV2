package my.edu.um.umpoint.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.chat.dao.ChatMessageDao;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageEntity;
import my.edu.um.umpoint.modules.chat.service.ChatMessageService;
import my.edu.um.umpoint.modules.service.service.SvcServiceService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * Chat message
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Service
public class ChatMessageServiceImpl extends CrudServiceImpl<ChatMessageDao, ChatMessageEntity, ChatMessageDTO> implements ChatMessageService{

    @Autowired
    private SpcSpaceService spcSpaceService;

    @Autowired
    private SvcServiceService svcServiceService;

    @Autowired
    private AccAccommodationService accAccommodationService;

    @Autowired
    private ChatClient clientChat;

    @Override
    public QueryWrapper<ChatMessageEntity> getWrapper(Map<String, Object> params){
        String id = (String) params.get("id");

        QueryWrapper<ChatMessageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public List<ChatMessageEntity> getRoomMessages(Long roomId){
        return baseDao.getRoomMessages(roomId);
    }

    @Override
    public String generateAiChatResponse(ChatRoomDTO chatRoomDTO, ChatMessageDTO chatMessageDTO){

        String contextText = """
                Context information is below, surrounded by ---------------------
                
                ---------------------
                %s
                ---------------------
                
                Given the context and provided history information, reply to the user comment below.
                If the answer is not in the context, inform the user that you can't answer the question.
            """;

        String contextMessage;

        if (chatRoomDTO.getFacilityType() == ChatConstant.FacilityType.SPACE.getValue())
            contextMessage = String.format(contextText, spcSpaceService.get(chatRoomDTO.getFacilityId())) +
                             chatMessageDTO.getMessage();
        else if (chatRoomDTO.getFacilityType() == ChatConstant.FacilityType.SERVICE.getValue())
            contextMessage = String.format(contextText, svcServiceService.get(chatRoomDTO.getFacilityId())) +
                             chatMessageDTO.getMessage();
        else if (chatRoomDTO.getFacilityType() == ChatConstant.FacilityType.ACCOMMODATION.getValue())
            contextMessage = String.format(contextText, accAccommodationService.get(chatRoomDTO.getFacilityId())) +
                             chatMessageDTO.getMessage();
        else
            contextMessage = chatMessageDTO.getMessage();

        return this.clientChat.prompt()
                              .user(contextMessage)
                              .advisors(a -> a
                                  .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatRoomDTO.getId())
                                  .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                              .call().content();
    }

    @Override
    public List<String> generateAiChatResponseOptions(
        ChatRoomDTO chatRoomDTO,
        ChatMessageDTO chatMessageDTO,
        ChatMessageDTO botMessageDTO
    ){

        String contextText = """
                Context information is below, surrounded by ---------------------
                
                ---------------------
                %s
                ---------------------
                
                User comment is below, surrounded by =====================
                
                =====================
                %s
                =====================
                
                Given the context and provided user comment, suggest some questions that the user might ask
                towards an AI assistant that had provided the response below.
                Provide maximum of 5 options. Use bullet points for the options. Only output the options without any pre-text.
                The last option must be "Talk to human".
            """;

        String contextMessage;

        if (chatRoomDTO.getFacilityType() == ChatConstant.FacilityType.SPACE.getValue())
            contextMessage =
                String.format(contextText, spcSpaceService.get(chatRoomDTO.getFacilityId()),
                    chatMessageDTO.getMessage()
                ) +
                chatMessageDTO.getMessage();
        else if (chatRoomDTO.getFacilityType() == ChatConstant.FacilityType.SERVICE.getValue())
            contextMessage = String.format(contextText, svcServiceService.get(chatRoomDTO.getFacilityId()),
                chatMessageDTO.getMessage()
            ) + chatMessageDTO.getMessage();
        else if (chatRoomDTO.getFacilityType() == ChatConstant.FacilityType.ACCOMMODATION.getValue())
            contextMessage = String.format(contextText, accAccommodationService.get(chatRoomDTO.getFacilityId()),
                chatMessageDTO.getMessage()
            ) + chatMessageDTO.getMessage();
        else
            contextMessage = contextText + botMessageDTO.getMessage();

        return this.clientChat.prompt()
                              .user(contextMessage)
                              .advisors(a -> a
                                  .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatRoomDTO.getId())
                                  .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                              .call()
                              .entity(new ParameterizedTypeReference<ChatResponse>(){
                              })
                              .items; // Extract the list of items

    }

    static class ChatResponse{
        public String schema;
        public String type;
        public List<String> items;

        // Getters and setters
    }
}