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
