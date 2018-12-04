/*
*created by Jeff Ching @ https://github.com/googleapis/google-auth-library-java#google-auth-library-credentials
 */

package com.example.lucientamno.encryptionphotosapp.external;

import java.util.List;
import java.util.Map;

/**
 * The callback that receives the result of the asynchronous . Exactly one method should be called.
 */
public interface RequestMetadataCallback {
  /**
   * Called when metadata is successfully produced.
   *
   * @param metadata Metadata returned for the request.
   */
  void onSuccess(Map<String, List<String>> metadata);

  /**
   * Called when metadata generation failed.
   *
   * @param exception The thrown exception which caused the request metadata fetch to fail.
   */
  void onFailure(Throwable exception);
}
