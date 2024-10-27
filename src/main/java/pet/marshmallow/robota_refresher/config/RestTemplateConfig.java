package pet.marshmallow.robota_refresher.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder,
                                   AuthTokenInterceptor authTokenInterceptor) {
    return builder
        .additionalInterceptors(createHeadersInterceptor())
        .additionalInterceptors(createDraculaHeadersInterceptor())
        .additionalInterceptors(authTokenInterceptor)
        .setConnectTimeout(Duration.ofSeconds(10))
        .setReadTimeout(Duration.ofSeconds(10))
        .build();
  }


  private ClientHttpRequestInterceptor createDraculaHeadersInterceptor() {
    return (request, body, execution) -> {
      HttpHeaders headers = request.getHeaders();
      headers.add("Host", "dracula.robota.ua");
      headers.add("Referer", "https://robota.ua/");
      headers.add("apollographql-client-version", "50221e3");
      headers.add("cid", "476298760.1729976267");
      headers.add("apollographql-client-name", "web-alliance-desktop");
      return execution.execute(request, body);
    };
  }


  public static ClientHttpRequestInterceptor createHeadersInterceptor() {
    return (request, body, execution) -> {
      HttpHeaders headers = request.getHeaders();
      headers.remove("Accept");
      headers.remove("Content-Type");
      headers.add("Content-Type", "application/json");
      headers.add("Accept", "application/json, text/plain, */*");
      headers.add("Sec-Fetch-Site", "same-site");
      headers.add("Accept-Language", "uk");
      headers.add("Accept-Encoding", "gzip, deflate, br");
      headers.add("Sec-Fetch-Mode", "cors");
      headers.add("Origin", "https://robota.ua");
      headers.add("User-Agent",
                  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15");
      headers.add("Connection", "keep-alive");
      headers.add("Sec-Fetch-Dest", "empty");
      return execution.execute(request, body);
    };
  }
}
