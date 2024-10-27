package pet.marshmallow.robota_refresher.api;


import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import pet.marshmallow.robota_refresher.excaption.InvalidHttpResponseException;

@Service
@RequiredArgsConstructor
public class RobotaApi {

  private final RestTemplate restTemplate;


  public void updateSeekerProfResumeSortDate(Integer resumeId) throws InvalidHttpResponseException {
    String body =
        "{\"operationName\":\"UpdateSeekerProfResumeSortDate\",\"variables\":{\"input\":{\"resumeId\":\""
            + resumeId
            + "\"}},\"query\":\"mutation UpdateSeekerProfResumeSortDate($input: UpdateSeekerProfResumeSortDateInput!) {\\n  updateSeekerProfResumeSortDate(input: $input) {\\n    ...UpdateSeekerProfResume\\n    __typename\\n  }\\n}\\n\\nfragment UpdateSeekerProfResume on UpdateSeekerProfResumeSortDateOutput {\\n  profResume {\\n    id\\n    __typename\\n  }\\n  errors {\\n    ... on ProfResumeDoesNotExist {\\n      message\\n      __typename\\n    }\\n    ... on ProfResumeDoesNotBelongToSeeker {\\n      message\\n      __typename\\n    }\\n    __typename\\n  }\\n  __typename\\n}\\n\"}";

    HttpEntity<String> entity = new HttpEntity<>(body);
    try {
      ResponseEntity<byte[]> response = restTemplate.exchange(
          "https://dracula.robota.ua/?q=UpdateSeekerProfResumeSortDate",
          HttpMethod.POST,
          entity,
          byte[].class);

      if (response.getStatusCode() == HttpStatus.OK) {
        System.out.println("Resume updated date is UPDATED");
      }
    } catch (HttpClientErrorException | HttpServerErrorException e) {
      String responseBody = new String(e.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);
      throw new InvalidHttpResponseException("Error to update resume date. Status: " + e.getStatusCode() + ". Body: " + responseBody);
    } catch (Exception e) {
      e.printStackTrace();
      throw new InvalidHttpResponseException("Error to update resume date. Message: " + e.getMessage());
    }
  }
}
