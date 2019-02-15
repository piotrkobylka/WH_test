package apiincucumber.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Game {
 int id;
 @JsonProperty("Name")
  String name;
  @JsonProperty("Type")
  String type;
  @JsonProperty("Currency")
  String currency;
 @JsonProperty("Stake")
  BigDecimal[] stake;
  @JsonProperty("StakesThisWeek")
  BigDecimal stakesThisWeek;
}
