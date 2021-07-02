package com.github.xcapdevila.creditcard.utils;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.junit.platform.commons.util.StringUtils;

/**
 * @author Xavier Capdevila Estevez on 20/6/21.
 */
@Builder(builderMethodName = "create", builderClassName = "Builder")
public final class CreditCardIssuer {

  @Getter
  private final String name;
  @Getter
  private final Integer cards;
  @Getter
  private final String panRegex;
  @Getter
  private final String cvvRegex;
  @Getter
  private final String expDateRegex;

  private final LuhnCompliance luhnCompliance;

  private CreditCardIssuer(String name, Integer cards, String panRegex, String cvvRegex, String expDateRegex,
      LuhnCompliance luhnCompliance) {

    if (StringUtils.isBlank(name)) {
      throw new RuntimeException("'name' is blank");
    }
    if (Objects.isNull(cards) || cards < 1) {
      throw new RuntimeException("'cards' is lower than 1");
    }
    if (StringUtils.isBlank(panRegex)) {
      throw new RuntimeException("'panRegex' is blank");
    }
    if (StringUtils.isBlank(cvvRegex)) {
      throw new RuntimeException("'cvvRegex' is blank");
    }
    if (StringUtils.isBlank(expDateRegex)) {
      throw new RuntimeException("'expDateRegex' is blank");
    }
    if (Objects.isNull(luhnCompliance)) {
      throw new RuntimeException("'luhnCompliance' is null");
    }

    this.name = name;
    this.cards = cards;
    this.panRegex = panRegex;
    this.cvvRegex = cvvRegex;
    this.expDateRegex = expDateRegex;
    this.luhnCompliance = luhnCompliance;
  }

  public boolean isLuhnCompliant(final String pan) {
    return luhnCompliance.validate(pan);
  }

}
