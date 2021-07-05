package com.github.xcapdevila.creditcard.utils;

import com.github.curiousoddman.rgxgen.RgxGen;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.platform.commons.util.StringUtils;

/**
 * @author Xavier Capdevila Estevez on 28/5/21.
 */
@Slf4j
public class CreditCardGenerator {

  private static final Pattern PAN_REGEX = Pattern.compile("\\$\\{pan}");
  private static final Pattern CVV_REGEX = Pattern.compile("\\$\\{cvv}");
  private static final Pattern EXP_DATE_REGEX = Pattern.compile("\\$\\{expDate}");
  private static final Pattern ISSUER_NAME_REGEX = Pattern.compile("\\$\\{issuerName}");

  private final List<CreditCardIssuer> creditCardIssuers;
  private final String outputPattern;

  public CreditCardGenerator(final List<CreditCardIssuer> creditCardIssuers, final String outputPattern) {
    if (Objects.isNull(creditCardIssuers) || creditCardIssuers.isEmpty()) {
      throw new IllegalArgumentException("'creditCardIssuers' is empty");
    }
    if (StringUtils.isBlank(outputPattern)) {
      throw new IllegalArgumentException("'outputPattern' is blank");
    }
    this.creditCardIssuers = new ArrayList<>(creditCardIssuers);
    this.outputPattern = outputPattern;
  }

  public Set<String> generateRandomCards() {
    val cards = new HashSet<String>();
    log.info("Card generator process starting...");
    for (CreditCardIssuer creditCardIssuer : creditCardIssuers) {
      log.info("Generating {} {} cards...", creditCardIssuer.getCards(), creditCardIssuer.getName());

      val panGenerator = new RgxGen(creditCardIssuer.getPanRegex());
      val cvvGenerator = new RgxGen(creditCardIssuer.getCvvRegex());
      val expDateGenerator = new RgxGen(creditCardIssuer.getExpDateRegex());
      for (int i = 0; i < creditCardIssuer.getCards(); ++i) {
        boolean isValid = false;
        String pan;
        while (!isValid) {
          pan = panGenerator.generate();
          isValid = creditCardIssuer.isLuhnCompliant(pan);
          if (isValid) {
            String cardInfo = outputPattern;
            cardInfo = PAN_REGEX.matcher(cardInfo).replaceAll(pan);
            cardInfo = CVV_REGEX.matcher(cardInfo).replaceAll(cvvGenerator.generate());
            cardInfo = EXP_DATE_REGEX.matcher(cardInfo).replaceAll(expDateGenerator.generate());
            cardInfo = ISSUER_NAME_REGEX.matcher(cardInfo).replaceAll(creditCardIssuer.getName());

            isValid = cards.add(cardInfo);
            if (isValid) {
              log.debug("Generated card: {}", cardInfo);
            }
          }
        }
      }
      log.info("{} {} cards generated.", creditCardIssuer.getCards(), creditCardIssuer.getName());
    }
    log.info("Card generator process finished.");
    log.info("Total generated cards: {}", cards.size());

    return cards;
  }

}
