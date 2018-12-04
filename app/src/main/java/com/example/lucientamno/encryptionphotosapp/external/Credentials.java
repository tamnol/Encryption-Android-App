/*
 * file's ownership: created by Jeff Ching
 * @ https://github.com/googleapis/google-auth-library-java#google-auth-library-credentials
 */

package com.example.lucientamno.encryptionphotosapp.external;

import com.google.auth.RequestMetadataCallback;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Represents an abstract authorized identity instance.
 */
public abstract class Credentials implements Serializable {

  private static final long serialVersionUID = 808575179767517313L;

  /**
   * A constant string name describing the authentication technology.
   *
   * <p>E.g. “OAuth2”, “SSL”. For use by the transport layer to determine whether it supports the
   * type of authentication in the case where {@link Credentials#hasRequestMetadataOnly} is false.
   * Also serves as a debugging helper.</p>
   *
   * @return The type of authentication used.
   */
  public abstract String getAuthenticationType();

  /**
   * Get the current request metadata, refreshing tokens if required.
   *
   * <p>This should be called by the transport layer on each request, and the data should be
   * populated in headers or other context. The operation can block and fail to complete and may do
   * things such as refreshing access tokens.</p>
   *
   * <p>The convention for handling binary data is for the key in the returned map to end with
   * {@code "-bin"} and for the corresponding values to be base64 encoded.</p>
   *
   * @return The request metadata used for populating headers or other context.
   * @throws IOException if there was an error getting up-to-date access.
   */
  public Map<String, List<String>> getRequestMetadata() throws IOException {
    return getRequestMetadata(null);
  }

  /**
   * Get the current request metadata without blocking.
   *
   * <p>This should be called by the transport layer on each request, and the data should be
   * populated in headers or other context. The implementation can either call the callback inline
   * or asynchronously. Either way it should <strong>never block</strong> in this method. The
   * executor is provided for tasks that may block.</p>
   *
   * <p>The default implementation will just call {@link #getRequestMetadata(URI)} then the callback
   * from the given executor.</p>
   *
   * <p>The convention for handling binary data is for the key in the returned map to end with
   * {@code "-bin"} and for the corresponding values to be base64 encoded.</p>
   *
   * @param uri URI of the entry point for the request.
   * @param executor Executor to perform the request.
   * @param callback Callback to execute when the request is finished.
   */
  public void getRequestMetadata(final URI uri, Executor executor,
      final RequestMetadataCallback callback) {
    executor.execute(new Runnable() {
        @Override
        public void run() {
          blockingGetToCallback(uri, callback);
        }
      });
  }

  /**
   * Call {@link #getRequestMetadata(URI)} and pass the result or error to the callback.
   *
   * @param uri URI of the entry point for the request.
   * @param callback Callback handler to execute when the metadata completes.
   */
  protected final void blockingGetToCallback(URI uri, RequestMetadataCallback callback) {
    Map<String, List<String>> result;
    try {
      result = getRequestMetadata(uri);
    } catch (Throwable e) {
      callback.onFailure(e);
      return;
    }
    callback.onSuccess(result);
  }

  /**
   * Get the current request metadata in a blocking manner, refreshing tokens if required.
   *
   * <p>This should be called by the transport layer on each request, and the data should be
   * populated in headers or other context. The operation can block and fail to complete and may do
   * things such as refreshing access tokens.</p>
   *
   * <p>The convention for handling binary data is for the key in the returned map to end with
   * {@code "-bin"} and for the corresponding values to be base64 encoded.</p>
   *
   * @param uri URI of the entry point for the request.
   * @return The request metadata used for populating headers or other context.
   * @throws IOException if there was an error getting up-to-date access.
   */
  public abstract Map<String, List<String>> getRequestMetadata(URI uri) throws IOException;

  /**
   * Whether the credentials have metadata entries that should be added to each request.
   *
   * <p>This should be called by the transport layer to see if
   * {@link Credentials#getRequestMetadata} should be used for each request.</p>
   *
   * @return Whether or not the transport layer should call {@link Credentials#getRequestMetadata}
   */
  public abstract boolean hasRequestMetadata();

  /**
   * Indicates whether or not the Auth mechanism works purely by including request metadata.
   *
   * <p>This is meant for the transport layer. If this is true a transport does not need to take
   * actions other than including the request metadata. If this is false, a transport must
   * specifically know about the authentication technology to support it, and should fail to
   * accept the credentials otherwise.</p>
   *
   * @return Whether or not the Auth mechanism works purely by including request metadata.
   */
  public abstract boolean hasRequestMetadataOnly();

  /**
   * Refresh the authorization data, discarding any cached state.
   *
   * <p> For use by the transport to allow retry after getting an error indicating there may be
   * invalid tokens or other cached state.
   *
   * @throws IOException if there was an error getting up-to-date access.
   */
  public abstract void refresh() throws IOException;
}
