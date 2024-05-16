# edge-courses
Copyright (C) 2023-2024 The Open Library Foundation

This software is distributed under the terms of the Apache License, Version 2.0. See the file "LICENSE" for more information.

# Introduction
Provides an ability to retrieve course-reserve information from FOLIO

# Overview
The purpose of this edge API is to bridge the gap between 3rd party discovery services and FOLIO mod-courses module.

# Security
See [edge-common-spring](https://github.com/folio-org/edge-common-spring)

## Requires Permissions

Institutional users should be granted the following permission in order to use this edge API:
- `"course-reserves-storage.reserves.collection.get"`
- `"course-reserves-storage.courselistings.reserves.collection.get"`

## Configuration

* See [edge-common](https://github.com/folio-org/edge-common) for a description of how configuration works.

***System properties***
Property                   | Default     | Description
------------------------   | ----------- | -------------
`secure_store`            | `Ephemeral` | Type of secure store to use.  Valid: `Ephemeral`, `AwsSsm`, `Vault`
`secure_store_props`      | `src/main/resources/ephemeral.properties`        | Path to a properties file specifying secure store configuration
`token_cache_ttl_ms`      | `3600000`   | How long to cache token, in milliseconds (ms)
`null_token_cache_ttl_ms` | `30000`     | How long to cache token failure (null JWTs), in milliseconds (ms)
`token_cache_capacity `   | `100`       | Max token cache size
`JAVA_OPTIONS`            | `-XX:MaxRAMPercentage=66.0`              | Java options, default - maximum heap size as a percentage of total memory)

### Secure Stores

Three secure stores currently implemented for safe retrieval of encrypted credentials:

#### EphemeralStore ####

Only intended for _development purposes_.  Credentials are defined in plain text in a specified properties file.  See `src/main/resources/ephemeral.properties`

### TLS Configuration for HTTP Endpoints

To configure Transport Layer Security (TLS) for HTTP endpoints in edge module, the following configuration parameters can be used. These parameters allow you to specify key and keystore details necessary for setting up TLS.

#### Configuration Parameters

1. **`spring.ssl.bundle.jks.web-server.key.password`**
- **Description**: Specifies the password for the private key in the keystore.
- **Example**: `spring.ssl.bundle.jks.web-server.key.password=SecretPassword`

2. **`spring.ssl.bundle.jks.web-server.key.alias`**
- **Description**: Specifies the alias of the key within the keystore.
- **Example**: `spring.ssl.bundle.jks.web-server.key.alias=localhost`

3. **`spring.ssl.bundle.jks.web-server.keystore.location`**
- **Description**: Specifies the location of the keystore file in the local file system.
- **Example**: `spring.ssl.bundle.jks.web-server.keystore.location=/some/secure/path/test.keystore.bcfks`

4. **`spring.ssl.bundle.jks.web-server.keystore.password`**
- **Description**: Specifies the password for the keystore.
- **Example**: `spring.ssl.bundle.jks.web-server.keystore.password=SecretPassword`

5. **`spring.ssl.bundle.jks.web-server.keystore.type`**
- **Description**: Specifies the type of the keystore. Common types include `JKS`, `PKCS12`, and `BCFKS`.
- **Example**: `spring.ssl.bundle.jks.web-server.keystore.type=BCFKS`

6. **`server.ssl.bundle`**
- **Description**: Specifies which SSL bundle to use for configuring the server. This parameter links to the defined SSL bundle, for example, `web-server`.
- **Example**: `server.ssl.bundle=web-server`

7. **`server.port`**
- **Description**: Specifies the port on which the server will listen for HTTPS requests.
- **Example**: `server.port=8443`

#### Example Configuration

To enable TLS for the edge module using the above parameters, you need to provide them as the environment variables. Below is an example configuration:

```properties
spring.ssl.bundle.jks.web-server.key.password=SecretPassword
spring.ssl.bundle.jks.web-server.key.alias=localhost
spring.ssl.bundle.jks.web-server.keystore.location=classpath:test/test.keystore.bcfks
spring.ssl.bundle.jks.web-server.keystore.password=SecretPassword
spring.ssl.bundle.jks.web-server.keystore.type=BCFKS

server.ssl.bundle=web-server
server.port=8443
```
### TLS Configuration for Feign HTTP Clients

To configure Transport Layer Security (TLS) for HTTP clients created using Feign annotations in the edge module, you can use the following configuration parameters. These parameters allow you to specify trust store details necessary for setting up TLS for Feign clients.

#### Configuration Parameters

1. **`folio.client.okapiUrl`**
- **Description**: Specifies the base URL for the Okapi service.
- **Example**: `folio.client.okapiUrl=https://okapi:443`

2. **`folio.client.tls.enabled`**
- **Description**: Enables or disables TLS for the Feign clients.
- **Example**: `folio.client.tls.enabled=true`

3. **`folio.client.tls.trustStorePath`**
- **Description**: Specifies the location of the trust store file.
- **Example**: `folio.client.tls.trustStorePath=classpath:/some/secure/path/test.truststore.bcfks`

4. **`folio.client.tls.trustStorePassword`**
- **Description**: Specifies the password for the trust store.
- **Example**: `folio.client.tls.trustStorePassword="SecretPassword"`

5. **`folio.client.tls.trustStoreType`**
- **Description**: Specifies the type of the trust store. Common types include `JKS`, `PKCS12`, and `BCFKS`.
- **Example**: `folio.client.tls.trustStoreType=bcfks`

#### Note
The `trustStorePath`, `trustStorePassword`, and `trustStoreType` parameters can be omitted if the server provides a public certificate.

#### Example Configuration

To enable TLS for Feign HTTP clients using the above parameters, you need to provide them as the environment variables. Below is an example configuration:

```properties
folio.client.okapiUrl=https://okapi:443
folio.client.tls.enabled=true
folio.client.tls.trustStorePath=classpath:test/test.truststore.bcfks
folio.client.tls.trustStorePassword=SecretPassword
folio.client.tls.trustStoreType=bcfks
```


## Additional Information
### Issue tracker

See project [EDGCOURSES](https://issues.folio.org/browse/EDGCOURSES)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker/).

### API Documentation

This module's [API documentation](https://dev.folio.org/reference/api/#edge-courses).

### Code analysis

[SonarQube analysis](https://sonarcloud.io/dashboard?id=org.folio%3Aedge-courses).

### Download and configuration

The built artifacts for this module are available.
See [configuration](https://dev.folio.org/download/artifacts) for repository access,
and the [Docker image](https://hub.docker.com/r/folioorg/edge-courses/)

### Development tips

The development tips are described on the following page: [Development tips](doc/development.md)
