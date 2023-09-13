package com.sum.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PercentageDto {

  int percentage;

  public int getPercentage() {
    return percentage;
  }

  @JsonSetter("percentage")
  public void setPercentage(String percentage) {
    this.percentage = Integer.parseInt(percentage);
  }
}
