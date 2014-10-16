package de.bootstrap;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class AndroidPublisherHelper {

    private static final Log log = LogFactory.getLog(AndroidPublisherHelper.class);


    private static final String APPLICATION_NAME = "com.electronic_android";

    static final String MIME_TYPE_APK = "application/vnd.android.package-archive";

    static final String MIME_TYPE_IMAGE = "image/*";

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;


    /**
     * Global instance of the {@link com.google.api.client.util.store.DataStoreFactory}. The best practice is to
     * make it a single globally shared instance across your application.
     */

    private static Credential authorizeWithServiceAccount(String serviceAccountEmail, File pk12File)
            throws GeneralSecurityException, IOException {
        log.info(String.format("Authorizing using Service Account: %s", serviceAccountEmail));

        // Build service account credential.
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(serviceAccountEmail)
                .setServiceAccountScopes(
                        Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER))
                .setServiceAccountPrivateKeyFromP12File(pk12File)
                .build();
        return credential;
    }

    /**
     * Performs all necessary setup steps for running requests against the API.
     *
     * @param applicationName     the name of the application: com.example.app
     * @param serviceAccountEmail the Service Account Email (empty if using
     *                            installed application)
     * @return the {@Link AndroidPublisher} service
     * @throws java.security.GeneralSecurityException
     * @throws java.io.IOException
     */
    protected static AndroidPublisher init(String serviceAccountEmail, File pk12File)
            throws IOException, GeneralSecurityException {

        // Authorization.
        newTrustedTransport();
        Credential credential = authorizeWithServiceAccount(serviceAccountEmail, pk12File);

        // Set up and return API client.
        return new AndroidPublisher.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    private static void newTrustedTransport() throws GeneralSecurityException,
            IOException {
        if (null == HTTP_TRANSPORT) {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        }
    }


}
