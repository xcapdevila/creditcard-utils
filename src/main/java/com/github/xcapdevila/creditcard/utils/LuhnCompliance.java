package com.github.xcapdevila.creditcard.utils;

/**
 * @author Xavier Capdevila Estevez on 21/6/21.
 */
@FunctionalInterface
public interface LuhnCompliance {

  boolean validate(final String pan);

}
