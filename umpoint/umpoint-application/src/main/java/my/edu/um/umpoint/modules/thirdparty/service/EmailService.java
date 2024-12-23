package my.edu.um.umpoint.modules.thirdparty.service;

import my.edu.um.umpoint.modules.thirdparty.entity.EmailDetails;

public interface EmailService {
    void sendSimpleMail(EmailDetails details);
}
