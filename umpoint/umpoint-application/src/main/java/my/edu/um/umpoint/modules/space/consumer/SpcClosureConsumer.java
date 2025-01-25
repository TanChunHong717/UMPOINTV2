package my.edu.um.umpoint.modules.space.consumer;

import my.edu.um.umpoint.common.config.KafkaConfig;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.space.dto.SpcClosureDTO;
import my.edu.um.umpoint.modules.space.dto.SpcClosureKafkaMessageDTO;
import my.edu.um.umpoint.modules.space.service.SpcClosureService;
import my.edu.um.umpoint.modules.thirdparty.entity.EmailDetails;
import my.edu.um.umpoint.modules.thirdparty.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SpcClosureConsumer {
    @Autowired
    private SpcClosureService spcClosureService;

    @Autowired
    private EmailService emailService;

    private final Set<Long> processedSpaceIds = new HashSet<>();

    @KafkaListener(topics = KafkaConfig.BATCH_ADD_CLOSURE_TOPIC, groupId = "umpoint")
    public void onAddClosureMessageReceived(String message, Acknowledgment ack) {
        SpcClosureKafkaMessageDTO messageDTO = JsonUtils.parseObject(message, SpcClosureKafkaMessageDTO.class);
        if (messageDTO != null) {
            SpcClosureDTO closureDTO = messageDTO.getSpcClosureDTO();
            try {
                spcClosureService.save(closureDTO);
                processedSpaceIds.add(closureDTO.getSpaceId());

                if (messageDTO.getIsLast()) {
                    sendSuccessEmail(closureDTO, messageDTO.getUserDetail());
                    processedSpaceIds.clear();
                }
            } catch (Exception e) {
                sendFailureEmail(closureDTO, messageDTO.getUserDetail());
            }
        }
        ack.acknowledge();
    }

    private void sendSuccessEmail(SpcClosureDTO dto, UserDetail user) {
        String title = "Successfully Add Closure for Spaces";
        String spaceIdsList = "\n\t" + processedSpaceIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", \n\t"));

        String content = String.format("Dear %s,\n\n" +
                        "The booking closure for the following spaces has been successfully processed:\n\n" +
                        "- Space IDs: %s\n" +
                        "- Start Date: %s\n" +
                        "- End Date: %s\n" +
                        "- Start Time: %s\n" +
                        "- End Time: %s\n" +
                        "- Recur on Monday: %s\n" +
                        "- Recur on Tuesday: %s\n" +
                        "- Recur on Wednesday: %s\n" +
                        "- Recur on Thursday: %s\n" +
                        "- Recur on Friday: %s\n" +
                        "- Recur on Saturday: %s\n" +
                        "- Recur on Sunday: %s\n",
                user.getRealName(), spaceIdsList, dto.getStartDay(), dto.getEndDay(), dto.getStartTime(), dto.getEndTime(),
                dto.getRecurMonday() == 1 ? "Yes" : "No",
                dto.getRecurTuesday() == 1 ? "Yes" : "No",
                dto.getRecurWednesday() == 1 ? "Yes" : "No",
                dto.getRecurThursday() == 1 ? "Yes" : "No",
                dto.getRecurFriday() == 1 ? "Yes" : "No",
                dto.getRecurSaturday() == 1 ? "Yes" : "No",
                dto.getRecurSunday() == 1 ? "Yes" : "No");
        emailService.sendSimpleMail(new EmailDetails(user.getEmail(), content, title));
    }

    private void sendFailureEmail(SpcClosureDTO dto, UserDetail user) {
        String title = "Failed to Add Closure for Spaces";
        String content = String.format("Dear %s,\n\n" +
                        "We regret to inform you that there was an issue processing the booking closure for the following spaces:\n\n" +
                        "- Space IDs: %s\n" +
                        "- Start Date: %s\n" +
                        "- End Date: %s\n" +
                        "- Start Time: %s\n" +
                        "- End Time: %s\n" +
                        "- Recur on Monday: %s\n" +
                        "- Recur on Tuesday: %s\n" +
                        "- Recur on Wednesday: %s\n" +
                        "- Recur on Thursday: %s\n" +
                        "- Recur on Friday: %s\n" +
                        "- Recur on Saturday: %s\n" +
                        "- Recur on Sunday: %s\n",
                user.getRealName(), dto.getSpaceId(), dto.getStartDay(), dto.getEndDay(), dto.getStartTime(), dto.getEndTime(),
                dto.getRecurMonday() == 1 ? "Yes" : "No",
                dto.getRecurTuesday() == 1 ? "Yes" : "No",
                dto.getRecurWednesday() == 1 ? "Yes" : "No",
                dto.getRecurThursday() == 1 ? "Yes" : "No",
                dto.getRecurFriday() == 1 ? "Yes" : "No",
                dto.getRecurSaturday() == 1 ? "Yes" : "No",
                dto.getRecurSunday() == 1 ? "Yes" : "No");
        emailService.sendSimpleMail(new EmailDetails(user.getEmail(), content, title));
    }

}
