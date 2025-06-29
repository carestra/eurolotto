package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import java.util.List;

record EuroBallNumbers(List<Integer> numbers) {
  EuroBallNumbers {
    if (numbers == null || numbers.isEmpty()) {
      throw new NullPointerException("number cannot be null nor empty.");
    }

    if (numbers.size() != 2) {
      throw new NullPointerException("Numbers must contain exactly 2 values.");
    }

    if (numbers.stream().anyMatch(number -> number < 1 || number > 12)) {
      throw new IllegalArgumentException("Invalid numbers [" + numbers + "], values must be between 1 and 12.");
    }
  }
}
