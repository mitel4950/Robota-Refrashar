package pet.marshmallow.robota_refresher.job;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pet.marshmallow.robota_refresher.api.RobotaApi;
import pet.marshmallow.robota_refresher.excaption.InvalidHttpResponseException;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class Jobs {

  private final RobotaApi robotaApi;
  @Value("${robota.resumeId}")
  private Integer resumeId;

  @Scheduled(cron = "0 8 * * * ?")
  public void scheduleFixedDelayTask() {
    try {
      robotaApi.updateSeekerProfResumeSortDate(resumeId);
    } catch (InvalidHttpResponseException exception){
      System.out.println(exception.getMessage());
    }
  }

}
