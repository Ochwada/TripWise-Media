package com.tripwise.tripmedia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.config
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Monday,  25.Aug.2025 | 13:40
 * Description : Configuration class for creating and configuring a {@link S3Client} and {@link S3Presigner} bean.
 * - {@link S3Client} - a low-level client for interacting with S3 buckets and object - for upload/download operation
 * - {@link S3Presigner} - a utility for generating pre-signed URLs for secure object access- to create temporary URLs (e.g., for secure file sharing)
 * ================================================================
 */
@Configuration
public class S3Config {
    /**
     * Creates and Configures an {@link S3Client} bean.
     * Can be used to perform common S3 operations e.g. uploading, downloading and deleting objects in S3 buckets.
     *
     * @param endpoint  the S3 service endpoint (e.g., AWS S3, MinIO, localstack)
     * @param region    the AWS region (e.g., us-east-1)
     * @param accessKey the access key used for authentication
     * @param secretKey the secret key used for authentication
     * @param partStyle whether path-style access should be enabled (true by default)
     * @return a configured {@link S3Client} instance.
     *
     */
    @Bean
    public S3Client s3Client(@Value("${media.s3.endpoint}") String endpoint,
                             @Value("${media.s3.region}") String region,
                             @Value("${media.s3.access-key}") String accessKey,
                             @Value("${media.s3.secret-key}") String secretKey,
                             @Value("${media.s3.path-style-access:true}") boolean partStyle) {

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(accessKey, secretKey)))
                .serviceConfiguration(servConf -> servConf.pathStyleAccessEnabled(partStyle))
                .endpointOverride(URI.create(endpoint))
                .build();
    }

    /**
     * Creates and configures an {@link S3Presigner} bean.
     * - It's for generating pre-signed URLs that grant temporary access to S3 Objects without exposing credentials.
     * - Generate a pre-signed URL for downloading a file.
     *
     * @param endpoint  the S3 service endpoint
     * @param region    the AWS region
     * @param accessKey the access key used for authentication
     * @param secretKey the secret key used for authentication
     * @return a configured {@link S3Presigner} instance.
     */
    @Bean
    S3Presigner presigner(@Value("${media.s3.endpoint}") String endpoint,
                          @Value("${media.s3.region}") String region,
                          @Value("${media.s3.access-key}") String accessKey,
                          @Value("${media.s3.secret-key}") String secretKey) {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                ).endpointOverride(
                        URI.create(
                                endpoint
                        )
                ).build();
    }
}
