# CreditCard Utils

CreditCard Toolbox.

[![Build Status](https://app.travis-ci.com/xcapdevila/creditcard-utils.svg?branch=main)](https://app.travis-ci.com/xcapdevila/creditcard-utils)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=xcapdevila_creditcard-utils&metric=alert_status)](https://sonarcloud.io/dashboard?id=xcapdevila_creditcard-utils)
[![Coverage Status](https://coveralls.io/repos/github/xcapdevila/creditcard-utils/badge.svg)](https://coveralls.io/github/xcapdevila/creditcard-utils)
[![Known Vulnerabilities](https://snyk.io/test/github/xcapdevila/creditcard-utils/badge.svg)](https://snyk.io/test/github/xcapdevila/creditcard-utils)

<br>

### Credit Card Generator

####Utility class to help you generate random *(dummy but technically valid)* credit cards for testing purposes.

Credit card issuers list to be generated including:
- **Name:** issuer name
- **Cards:** number of cards to be generated
- **PAN Regex:** regular expression to generate the PAN value
- **CVV Regex:** regular expression to generate the CVV value
- **Expiration Date Regex:** regular expression to generate the expiration date value
- **Luhn Compliant:** a [LuhnCompliance](src/main/java/io/github/xcapdevila/creditcard/utils/LuhnCompliance.java) functional interface instance (See also [LuhnAlgorithmValidator](src/main/java/io/github/xcapdevila/creditcard/utils/LuhnAlgorithmValidator.java))
```
creditCardIssuers[0].name=VISA
creditCardIssuers[0].cards=100
creditCardIssuers[0].panRegex=^4[0-9]{15}$
creditCardIssuers[0].cvvRegex=^[0-9]{3}$
creditCardIssuers[0].expDateRegex=^(0[1-9]|1[0-2])(2[2-7])$
creditCardIssuers[0].luhnCompliant=(pan) -> luhnAlgorithmValidator.validate(pan)
```

Output pattern. It may include the following placeholders:
- **${pan}** - it will be replaced by the generated pan
- **${cvv}** - it will be replaced by the generated cvv
- **${expDate}** - it will be replaced by the generated expiration date
- **${issuerName}** - it will be replaced by the issuer name
```
outputPattern=${pan},${cvv},${expDate},${issuerName}
```

<br>

### Luhn Algorithm Validator

####Utility class to help you validate PAN's luhn algorithm compliance.

No configuration required. (See also [Luhn Algorithm](https://en.wikipedia.org/wiki/Luhn_algorithm)).

<br>

### Reference docs
[Payment Card Number](https://en.wikipedia.org/wiki/Payment_card_number)

[Luhn Algorithm](https://en.wikipedia.org/wiki/Luhn_algorithm)

<br>

### Disclaimer

Use this piece of software under your own responsibility.
