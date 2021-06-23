package com.github.xcapdevila.creditcard.utils;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreditCardGeneratorTests {

  private static final String PAN_PLACEHOLDER = "${pan}";
  private static final String CVV_PLACEHOLDER = "${cvv}";
  private static final String EXP_DATE_PLACEHOLDER = "${expDate}";
  private static final String ISSUER_NAME_PLACEHOLDER = "${issuerName}";
  private static final String DELIMITER = ",";
  private static final Random RANDOM = new Random();

  private LuhnAlgorithmValidator luhnAlgorithmValidator;
  private List<CreditCardIssuer> creditCardIssuers;

  @BeforeEach
  void setup() {
    luhnAlgorithmValidator = new LuhnAlgorithmValidator();

    creditCardIssuers = new ArrayList<>();
    val creditCardIssuerLuhn = CreditCardIssuer
        .create()
        .cards(getNextPositiveRandom(100))
        .name("issuer_1_luhn")
        .panRegex("^4[0-9]{15}$")
        .cvvRegex("^[0-9]{3}$")
        .expDateRegex("^(0[1-9]|1[0-2])(2[2-7])$")
        .luhnCompliant((pan) -> luhnAlgorithmValidator.isValid(pan))
        .build();
    creditCardIssuers.add(creditCardIssuerLuhn);
    val creditCardIssuerLuhn2 = CreditCardIssuer
        .create()
        .cards(getNextPositiveRandom(100))
        .name("issuer_2_luhn")
        .panRegex("^5[1-5][0-9]{14}$")
        .cvvRegex("^[0-9]{3}$")
        .expDateRegex("^(0[1-9]|1[0-2])(2[2-5])$")
        .luhnCompliant((pan) -> luhnAlgorithmValidator.isValid(pan))
        .build();
    creditCardIssuers.add(creditCardIssuerLuhn2);
    val creditCardIssuerNoLuhn = CreditCardIssuer
        .create()
        .cards(getNextPositiveRandom(100))
        .name("issuer_1_no_luhn")
        .panRegex("^3[47][0-9]{13}$")
        .cvvRegex("^[0-9]{4}$")
        .expDateRegex("^(0[1-9]|1[0-2])(2[3-8])$")
        .luhnCompliant((pan) -> true)
        .build();
    creditCardIssuers.add(creditCardIssuerNoLuhn);
  }

  private Integer getNextPositiveRandom(int bound) {
    int nextInt;
    do {
      nextInt = RANDOM.nextInt(bound);
    } while (nextInt < 1);
    return nextInt;
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "${pan}" + DELIMITER + "${cvv}" + DELIMITER + "${expDate}" + DELIMITER + "${issuerName}",
      "${expDate}" + DELIMITER + "${pan}" + DELIMITER + "${issuerName}",
      "${issuerName}" + DELIMITER + "${pan}",
      "${pan}" + DELIMITER + "${expDate}",
      "${pan}",
  })
  void givenAValidConfigCardsAreGenerated(final String outputPattern) {
    val creditCardGenerator = new CreditCardGenerator(creditCardIssuers, outputPattern);

    assertFalse(creditCardIssuers.isEmpty());
    assertFalse(StringUtils.isBlank(outputPattern));

    val cards = creditCardGenerator.generateRandomCards();
    val expectedCards = creditCardIssuers
        .stream()
        .map(CreditCardIssuer::getCards)
        .reduce(Integer::sum)
        .orElseThrow(RuntimeException::new);

    val outputPatternKeywords = asList(outputPattern.split(DELIMITER));

    assertAll(
        () -> assertEquals(expectedCards.intValue(), cards.size()),
        () -> {
          if (outputPattern.contains(ISSUER_NAME_PLACEHOLDER)) {
            for (CreditCardIssuer creditCardIssuer : creditCardIssuers) {
              val issuerCards = cards
                  .stream()
                  .filter(card -> !outputPattern.contains(ISSUER_NAME_PLACEHOLDER) || card.contains(creditCardIssuer.getName()))
                  .count();

              val issuerCardValues = cards
                  .stream()
                  .filter(card -> !outputPattern.contains(ISSUER_NAME_PLACEHOLDER) || card.contains(creditCardIssuer.getName()))
                  .findAny()
                  .orElseThrow(RuntimeException::new)
                  .split(DELIMITER);

              val panIdx = outputPatternKeywords.indexOf(PAN_PLACEHOLDER);
              val cvvIdx = outputPatternKeywords.indexOf(CVV_PLACEHOLDER);
              val expDateIdx = outputPatternKeywords.indexOf(EXP_DATE_PLACEHOLDER);
              assertAll(
                  () -> assertTrue(!outputPattern.contains(ISSUER_NAME_PLACEHOLDER) || creditCardIssuer.getCards() == issuerCards),
                  () -> assertTrue(!outputPattern.contains(PAN_PLACEHOLDER) || Pattern.matches(creditCardIssuer.getPanRegex(), issuerCardValues[panIdx])),
                  () -> assertTrue(!outputPattern.contains(CVV_PLACEHOLDER) || Pattern.matches(creditCardIssuer.getCvvRegex(), issuerCardValues[cvvIdx])),
                  () -> assertTrue(
                      !outputPattern.contains(EXP_DATE_PLACEHOLDER) || Pattern.matches(creditCardIssuer.getExpDateRegex(), issuerCardValues[expDateIdx])),
                  () -> assertTrue(!outputPattern.contains(PAN_PLACEHOLDER) || creditCardIssuer.isLuhnCompliant(issuerCardValues[panIdx]))
              );
            }
          }
        }
    );

  }

}
