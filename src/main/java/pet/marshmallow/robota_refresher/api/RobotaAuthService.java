package pet.marshmallow.robota_refresher.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.brotli.dec.BrotliInputStream;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pet.marshmallow.robota_refresher.api.entity.LogInEntity;
import pet.marshmallow.robota_refresher.config.RestTemplateConfig;

@Service
public class RobotaAuthService {

  private final RestTemplate restTemplate;
  private final LogInEntity logInEntity;
  private static String bearerToken = null;


  public RobotaAuthService(RestTemplateBuilder builder, LogInEntity logInEntity) {
    this.restTemplate = builder
        .additionalInterceptors(RestTemplateConfig.createHeadersInterceptor())
        .additionalInterceptors(createAuthHeadersInterceptor())
        .setConnectTimeout(Duration.ofSeconds(10))
        .setReadTimeout(Duration.ofSeconds(10))
        .build();

    this.logInEntity = logInEntity;
  }

  // get bearer token
  private String logIn() throws IOException {
    ResponseEntity<byte[]> response = restTemplate.exchange("https://auth-api.robota.ua/Login",
                                                            HttpMethod.POST,
                                                            new HttpEntity<>(logInEntity),
                                                            byte[].class);
    return decompressBrotli(response.getBody());
  }

  public String getBearerToken() throws IOException {
    return bearerToken == null ? getNewBearerToken() : bearerToken;
  }

  public String getNewBearerToken() throws IOException {
    bearerToken = logIn();
    System.out.println("Bearer Token is UPDATED");
    return bearerToken;
  }


  private static ClientHttpRequestInterceptor createAuthHeadersInterceptor() {
    return (request, body, execution) -> {
      HttpHeaders headers = request.getHeaders();
      headers.add("Host", "auth-api.robota.ua");
      headers.add("Referer", "https://robota.ua/");
      headers.add("Cookie",
                  "_fbp=fb.1.1729976267508.533419293522155553; _ga_WS6TVT9PSM=GS1.1.1729976267.1.1.1729976292.35.0.0; _gcl_aw=GCL.1729976269.EAIaIQobChMIg-nw4fesiQMVxwqiAx1BQAQhEAAYASAAEgKQifD_BwE; _gcl_gs=2.1.k1$i1729976266$u111548935; _hjSessionUser_3376361=eyJpZCI6ImE2ZTgzOTEwLThiZmQtNWNhYy04NTZiLWRiYWRkYWVmYmZlMiIsImNyZWF0ZWQiOjE3Mjk5NzYyNjk2OTIsImV4aXN0aW5nIjpmYWxzZX0=; _hjSession_3376361=eyJpZCI6IjY4ZTYxMjcxLWRlOGUtNGIyZS1hOGE2LTlhYzNlOWMxZjQyNCIsImMiOjE3Mjk5NzYyNjk2OTMsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjoxLCJzcCI6MH0=; _ga=GA1.1.476298760.1729976267; _gcl_au=1.1.1894065099.1729976268; searchEventAction=no_suggest");

      return execution.execute(request, body);
    };
  }

  private static String decompressBrotli(byte[] compressedData) throws IOException {
    try (InputStream brotliInputStream = new BrotliInputStream(
        new ByteArrayInputStream(compressedData))) {
      StringBuilder outStr = new StringBuilder();
      byte[] buffer = new byte[1024];
      int bytesRead;

      while ((bytesRead = brotliInputStream.read(buffer)) != -1) {
        outStr.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
      }

      return outStr.toString().replaceAll("\"", "");
    }
  }
}
