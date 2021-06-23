package com.github.xcapdevila.creditcard.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LuhnAlgorithmValidatorTests {

  private static final String VALID_PAN = "340184587300287";
  private static final String INVALID_PAN = "340184587300283";
  private static final String MALFORMED_PAN = "34018A5873E0283";
  private LuhnAlgorithmValidator luhnAlgorithmValidator;

  @BeforeEach
  public void setup() {
    luhnAlgorithmValidator = new LuhnAlgorithmValidator();
  }

  @Test
  void givenAValidCreditCardLuhnIsValid() {
    assertTrue(luhnAlgorithmValidator.isValid(VALID_PAN));
  }

  @Test
  void givenAnInvalidCreditCardLuhnIsInvalid() {
    assertFalse(luhnAlgorithmValidator.isValid(INVALID_PAN));
  }

  @Test
  void givenAMalformedCreditCardLuhnThrowsException() {
    assertThrows(RuntimeException.class, () -> luhnAlgorithmValidator.isValid(MALFORMED_PAN));
  }

}
