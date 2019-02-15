package apiincucumber.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    int id;
    @JsonProperty("Name")
    String name;
    @JsonProperty("Balance")
    BigDecimal balance;
    @JsonProperty("Currency")
    String currency;
    @JsonProperty("likes")
    String likes;
    @JsonProperty("High-Roller")
    Boolean highRoller;
}
