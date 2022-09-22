## 1.2.2 Released
The focus of this release was:
* major refactoring work related to renaming references from GOPAC to LOCATE
* format samlpe JSON responses in the API documentation

### Stories
* [US965340](https://rally1.rallydev.com/#/?detail=/userstory/638721191931&fdp=true): Major refactoring work related to renaming references to GOPAC
* [US967724](https://rally1.rallydev.com/#/?detail=/userstory/639214307523&fdp=true): Format sample JSON response in the API documentation

## 1.2.1 Released
The primary focus of this release was to add interface version login 7.1 to ModuleDescriptor

## 1.2.0 Released
The primary focus of this release was to update dependencies and fix small bug

### Stories
* [US899497](https://rally1.rallydev.com/#/?detail=/userstory/621159017243&fdp=true): Update spring base version for all modules
* [US864576](https://rally1.rallydev.com/#/?detail=/userstory/611047734181&fdp=true): NFR - Upgrade third-party dependencies

### Bug Fixes
* [DE65545](https://rally1.rallydev.com/#/?detail=/defect/623837392323&fdp=true): X-Okapi-Tenant is required, but shouldn't be

## 1.1.0 Released
The primary focus of this release was to fix log4 vulnerability

### Stories
* [US886457](https://rally1.rallydev.com/#/?detail=/userstory/616628060529&fdp=true): Implement courses and reserves APIs

### Bug Fixes
* [US894249](https://rally1.rallydev.com/#/?detail=/userstory/619656931913&fdp=true): CO - Patch all modules, shared libraries for log4j vulnerability


## 1.0.0 Released
The primary focus of this release was to implement edge-courses logic

### Stories
* [US819172](https://rally1.rallydev.com/#/?detail=/userstory/602597502327&fdp=true): Replace restTemplate with feign client in edge-courses
* [US813268](https://rally1.rallydev.com/#/?detail=/userstory/601488209459&fdp=true): Misc repo cleanup/maintenance
* [US802770](https://rally1.rallydev.com/#/?detail=/userstory/604213883576&fdp=true): edge-courses - Allow log level to be specified as system property or program argument
* [US770488](https://rally1.rallydev.com/#/?detail=/userstory/505595886008&fdp=true): Implement edge-courses business logic
* [US770020](https://rally1.rallydev.com/#/?detail=/userstory/505175766644&fdp=true): Create edge-courses skeleton

### Bug Fixes
* [DE59078](https://rally1.rallydev.com/#/?detail=/defect/600947886829&fdp=true): edge-courses ignores limit/offset parameters
* [DE58502](https://rally1.rallydev.com/#/?detail=/defect/604211273856&fdp=true): edge-courses calls the wrong API
