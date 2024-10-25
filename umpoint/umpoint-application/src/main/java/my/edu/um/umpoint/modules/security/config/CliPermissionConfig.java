package my.edu.um.umpoint.modules.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "umpoint.user.permission")
public class CliPermissionConfig {

    private List<String> common;

    private List<String> space;

    private List<String> service;

    private List<String> accommodation;

}
