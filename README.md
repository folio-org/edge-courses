# edge-courses
Copyright (C) 2018-2019 The Open Library Foundation

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
