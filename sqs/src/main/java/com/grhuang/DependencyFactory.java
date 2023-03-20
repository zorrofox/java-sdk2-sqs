
package com.grhuang;

import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * The module containing all dependencies required by the {@link Handler}.
 */
public class DependencyFactory {

    private DependencyFactory() {}

    /**
     * @return an instance of SqsClient
     */
    public static SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                        .build();
    }

    public static SqsClient sqsClient() {
        return SqsClient.builder()
                        .httpClient(UrlConnectionHttpClient.builder().build())
                        .build();
    }
}
