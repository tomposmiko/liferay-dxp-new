# Liferay Gradle Plugins App Docker Change Log

## 1.0.1 - 2017-09-19

### Changed
- [LPS-74785]: Avoid failing the build in the case of a Git error.

## 1.0.2 - 2017-09-20

### Fixed
- [LPS-74811]: Include the WAR file of WAR projects in the Docker image.

## 1.0.3 - 2017-10-18

### Added
- [LPS-75327]: Automatically convert `.sh` files to Unix-style line endings when
building the app's Docker image.

## 1.0.4 - 2017-11-03

### Changed
- [LPS-75704]: Update the [Gradle Docker Plugin] dependency to version 3.2.0.

[Gradle Docker Plugin]: https://github.com/bmuschko/gradle-docker-plugin
[LPS-74785]: https://issues.liferay.com/browse/LPS-74785
[LPS-74811]: https://issues.liferay.com/browse/LPS-74811
[LPS-75327]: https://issues.liferay.com/browse/LPS-75327
[LPS-75704]: https://issues.liferay.com/browse/LPS-75704