/*
 * file's ownership: created by Jeff Ching
 * @ https://github.com/googleapis/google-auth-library-java#google-auth-library-credentials
 */

package com.example.lucientamno.encryptionphotosapp.external;

import java.util.Objects;

/**
 * Interface for a service account signer. A signer for a service account is capable of signing
 * bytes using the private key associated with its service account.
 */
public interface ServiceAccountSigner {

  class SigningException extends RuntimeException {

    private static final long serialVersionUID = -6503954300538947223L;

    public SigningException(String message, Exception cause) {
      super(message, cause);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof SigningException)) {
        return false;
      }
      SigningException other = (SigningException) obj;
      return Objects.equals(getCause(), other.getCause())
          && Objects.equals(getMessage(), other.getMessage());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getMessage(), getCause());
    }
  }

  /**
   * Returns the service account associated with the signer.
   *
   * @return The service account associated with the signer.
   */
  String getAccount();

  /**
   * Signs the provided bytes using the private key associated with the service account.
   *
   * @param toSign bytes to sign
   * @return signed bytes
   * @throws SigningException if the attempt to sign the provided bytes failed
   */
  byte[] sign(byte[] toSign);
}
