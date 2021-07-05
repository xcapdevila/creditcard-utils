package com.github.xcapdevila.creditcard.utils;

import java.util.regex.Pattern;

/**
 * @author Xavier Capdevila Estevez on 27/5/21.
 */
public class LuhnAlgorithmValidator {

  private static final int LUHN_LAST_DIGIT = 9;
  private static final int LUHN_MULTIPLIER = 2;
  private static final long LUHN_MODULE = 10L;
  private static final Pattern ONLY_DIGITS = Pattern.compile("\\d+");

  public boolean isValid(final String pan) {
    if (!ONLY_DIGITS.matcher(pan).matches()) {
      throw new IllegalArgumentException("Luhn requires an only digits pan to be validated");
    }
    int digits = pan.length();
    int oddOrEven = digits & 1;
    long sum = 0L;

    for (int count = 0; count < digits; ++count) {
      int digit = Integer.parseInt(String.valueOf(pan.charAt(count)));

      if (((count & 1) ^ oddOrEven) == 0) {
        digit *= LUHN_MULTIPLIER;
        if (digit > LUHN_LAST_DIGIT) {
          digit -= LUHN_LAST_DIGIT;
        }
      }

      sum += digit;
    }

    return sum != 0L && sum % LUHN_MODULE == 0L;
  }

}
