package pet.marshmallow.robota_refresher.api.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "robota")
public class LogInEntity {

  private String username;
  private String password;

}
