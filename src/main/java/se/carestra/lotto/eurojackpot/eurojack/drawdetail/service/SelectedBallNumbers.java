package se.carestra.lotto.eurojackpot.eurojack.drawdetail.service;

import java.util.List;

public record SelectedBallNumbers(List<Integer> numbers) {
  public SelectedBallNumbers {
    if (numbers == null || numbers.isEmpty()) {
      throw new NullPointerException("number cannot be null nor empty.");
    }

    if (numbers.size() != 5) {
      throw new NullPointerException("Numbers must contain exactly 5 values.");
    }

    if (numbers.stream().anyMatch(number -> number < 1 || number > 50)) {
      throw new IllegalArgumentException("Invalid numbers [" + numbers + "], values must be between 1 and 50.");
    }
  }
}
