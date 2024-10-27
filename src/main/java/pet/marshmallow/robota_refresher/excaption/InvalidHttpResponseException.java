package pet.marshmallow.robota_refresher.excaption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InvalidHttpResponseException extends Exception {

  @Getter
  private String message;

}
