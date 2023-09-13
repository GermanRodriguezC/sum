package com.sum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PercentageResponseDto implements Serializable {

  @JsonProperty("percentage")
  int percentage;
}
