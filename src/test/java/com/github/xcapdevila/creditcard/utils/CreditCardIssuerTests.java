package com.github.xcapdevila.creditcard.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreditCardIssuerTests {

  private static Stream<Arguments> invalidConstructorArgs() {
    return Stream.of(
        Arguments.of(null, null, null, null, null, null),
        Arguments.of("name", null, null, null, null, null),
        Arguments.of("name", 0, null, null, null, null),
        Arguments.of("name", 1, null, null, null, null),
        Arguments.of("name", 1, null, null, null, null),
        Arguments.of("name", 1, "", null, null, null),
        Arguments.of("name", 1, " ", null, null, null),
        Arguments.of("name", 1, "panRegex", null, null, null),
        Arguments.of("name", 1, "panRegex", "", null, null),
        Arguments.of("name", 1, "panRegex", " ", null, null),
        Arguments.of("name", 1, "panRegex", "cvvRegex", null, null),
        Arguments.of("name", 1, "panRegex", "cvvRegex", "", null),
        Arguments.of("name", 1, "panRegex", "cvvRegex", " ", null),
        Arguments.of("name", 1, "panRegex", "cvvRegex", "expDateRegex", null)
        );
  }

  @ParameterizedTest
  @MethodSource("invalidConstructorArgs")
  void invalidConstructorArgsThrowException(final String name, final Integer cards, final String panRegex, final String cvvRegex, final String expDateRegex,
      final LuhnCompliance luhnCompliant) {
    assertThrows(RuntimeException.class, () -> CreditCardIssuer
        .create()
        .name(name)
        .cards(cards)
        .panRegex(panRegex)
        .cvvRegex(cvvRegex)
        .expDateRegex(expDateRegex)
        .luhnCompliant(luhnCompliant)
        .build());
  }

}
