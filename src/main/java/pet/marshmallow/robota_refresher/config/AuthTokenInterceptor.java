package pet.marshmallow.robota_refresher.config;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import pet.marshmallow.robota_refresher.api.RobotaAuthService;

@Component
@RequiredArgsConstructor
public class AuthTokenInterceptor implements ClientHttpRequestInterceptor {

  private final RobotaAuthService tokenService;

  @Override
  public ClientHttpResponse intercept(HttpRequest request,
                                      byte[] body,
                                      ClientHttpRequestExecution execution) throws IOException {
    String token = tokenService.getBearerToken();
    request.getHeaders().add("Authorization", "Bearer " + token);

    ClientHttpResponse response = execution.execute(request, body);

    if (HttpStatus.UNAUTHORIZED == response.getStatusCode()) {
      System.out.println("Authorization error. Status: " + response.getStatusCode());
      token = tokenService.getNewBearerToken();
      request.getHeaders().set("Authorization", "Bearer " + token);
      response = execution.execute(request, body);
    }

    return response;
  }

}
