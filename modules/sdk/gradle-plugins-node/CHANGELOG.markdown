# Liferay Gradle Plugins Node Change Log

## 1.0.1 - 2015-09-08

### Commits
- [LPS-58467]: Not necessary (1b2291a9d3)
- [LPS-58260]: Use the system "tar" to extract the tarball, otherwise the
symbolic links in it will be lost (0a1438b324)
- [LPS-51081]: Remove modules' Eclipse project files (b3f19f9012)
- [LPS-51081]: Replace modules' Ant files with Gradle alternatives (9e60160a85)
- [LPS-51081]: Remove modules' Ivy files (076b384eef)

### Dependencies
- [LPS-58467]: Update the com.liferay.gradle.util dependency to version 1.0.19.

## 1.0.3 - 2015-09-09

### Commits
- [LPS-58467]: Override PATH so package.json scripts will use the locally
downloaded Node (b68b6ed3df)

## 1.0.4 - 2015-09-14

### Commits
- [LPS-58609]: Move the NPM cache configuration as a command line argument
(8cdd47a961)
- [LPS-58609]: New methods to add default NPM arguments (07fc3922b8)
- [LPS-58609]: New methods to append NPM arguments (be2e990495)

## 1.0.5 - 2015-09-15

### Commits
- [LPS-58655]: Update sample (7562f2d297)
- [LPS-58655]: Convert to null if empty, so the task input validation will throw
an error (defdfdc104)
- [LPS-58655]: Automatically generate the package.json file (b794bd415c)

## 1.0.6 - 2015-09-17

### Commits
- [LPS-58655]: Add option to set the bugs URL in the package.json file, otherwise
it will incorrectly point to https://github.com/liferay/liferay-portal/issues
(2629048392)
- [LPS-58655]: Fix passing credentials to NPM (3e26375845)

## 1.0.7 - 2015-11-03

### Commits
- [LPS-59564]: Update directory layout for "sdk" modules (ea19635556)

### Dependencies
- [LPS-60153]: Update the com.liferay.gradle.util dependency to version 1.0.23.

## 1.0.9 - 2015-12-15

### Commits
- [LPS-61322]: Add a task which runs "npm install" in the project dir
(582d1cbab1)

## 1.0.10 - 2016-01-12

### Commits
- [LPS-61754]: Use osgiHelper for a better default value (7af54a97ac)
- [LPS-61420]: Incorrect tabs and linebreaks in /modules/sdk (955b0fba88)

## 1.0.11 - 2016-01-26

### Commits
- [LPS-62504]: Update Node version (36b93b345b)
- [LPS-62504]: Allow custom working dir (c7a318bb3d)
- [LPS-62504]: Use the version of NPM embedded in the Node distribution
(5d9446e14b)
- [LPS-62504]: Base task to execute a Node script (6e6a1a6fed)
- [LPS-62504]: Fix Node/NPM download for newer versions (e4d270947a)
- [LPS-61088]: Remove classes and resources dir from Include-Resource
(1b0e1275bc)

## 1.0.12 - 2016-01-28

### Commits
- [LPS-62671]: Add main property to generated package.json files (1540d21d9a)
- [LPS-61848]: An empty settings.gradle is enough (2e5eb90e23)

## 1.0.13 - 2016-04-21

### Commits
- [LPS-65245]: Disable "npmInstall" if package.json contains no dependencies
(14974c8631)
- [LPS-65245]: Let NPM download modules in "projectDir/node_modules"
(6c9ad97c93)
- [LPS-61099]: Delete build.xml in modules (c9a7e1d370)
- [LPS-63943]: This is done automatically now (f1e42382d9)
- [LPS-62883]: Update build.gradle of Node-based plugins (176b68ee4f)
- [LPS-62942]: Explicitly list exported packages for correctness (f095a51e25)

### Dependencies
- [LPS-65086]: Update the com.liferay.gradle.util dependency to version 1.0.25.

## 1.0.14 - 2016-06-07

### Commits
- [LPS-66410]: Add task description (f125b7b933)
- [LPS-66410]: Use Process Builder to bypass GRADLE-3329 (bd94f9f191)
- [LPS-66410]: Fix NPE when converting a closure that returns null (175b7a2046)
- [LPS-66410]: Add option to use local Node.js installation (afc0b6440f)
- [LPS-66410]: If nodeDir == null, use local Node.js installation (189e1f4b1e)
- [LPS-66410]: Move NPM cache configuration into task argument (83f60d9940)
- [LPS-65810]: Gradle plugins aren't used in OSGi, no need to export anything
(83cdd8ddcd)
- [LPS-64816]: Update Gradle plugin samples (3331002e5d)

## 1.0.15 - 2016-06-09

### Commits
- [LPS-66410]: Command line argument to set "node.download" property
(d70ba4da2a)

## 1.0.16 - 2016-06-16

### Dependencies
- [LPS-65749]: Update the com.liferay.gradle.util dependency to version 1.0.26.

## 1.0.17 - 2016-08-01

### Commits
- [LPS-66906]: Add option to set NPM registry (43431b0ca1)
- [LPS-66906]: Sync NPM and Gradle log levels (6231d7904b)
- [LPS-66906]: Ensure the working dir exists before invoking Node (0b659a2c23)
- [LPS-66906]: Sort (3b5a8b5672)
- [LPS-66906]: Preconfigure only our main "downloadNode" task (473866dcd1)
- [LPS-66906]: Add logging while calling Node (6dd9f144d7)
- [LPS-66906]: Use task logger (435a728425)
- [LPS-66906]: Copy proxy settings from Java system properties to NPM
(d37aef57fd)
- [LPS-66906]: Add option to remove URLs from npm-shrinkwrap.json (e4b01ca92b)
- [LPS-66906]: Add option to cache the node_modules dir (e66ec9e283)
- [LPS-66906]: Move "npm install" task in its own class (18a3666308)

## 1.0.18 - 2016-08-05

### Commits
- [LPS-66906]: Retry "npm install" a few times if the Node invocation fails
(d5a5d6cd1f)
- [LPS-66906]: Extract method (fe7f58737c)
- [LPS-66906]: No need to call "npm install" after restoring the cache
(7c336b46e7)
- [LPS-66906]: Fail the Gradle build if Node.js fails (ac60817eb3)
- [LPS-66906]: Add option to hide the NPM progress bar (4ec3547f9e)
- [LPS-66906]: Block NPM parallel execution if we're writing on a shared dir
(c8ecdbb235)
- [LPS-66906]: Add option to download Node.js only once in the root project
(cfeca18aa0)

## 1.0.19 - 2016-08-09

### Commits
- [LPS-66906]: Automatically remove .bin dirs to bypass GRADLE-1843 (65a0d0ec03)

## 1.0.20 - 2016-08-11

### Commits
- [LPS-66906]: Rethrow the exception if copy fails (df634b2172)
- [LPS-66906]: Make "npm install" retries configurable, defaults to disabled
(5b346ef74f)

## 1.0.21 - 2016-08-15

### Commits
- [LPS-66906]: Stop retrying if Node.js completed succesfully (490ff7c860)

## 1.0.22 - 2016-08-27

### Commits
- [LPS-67023]: Do not download a module if it will be done by "npm install"
(711b415559)
- [LPS-67658]: Convert gradle-plugins-node sample into a smoke test (d4d40c57be)
- [LPS-67658]: Configure GradleTest in gradle-plugins-node (3bea1444af)
- [LPS-67658]: Need "compileOnly" to keep dependencies out of "compile"
(4a3cd0bc9d)
- [LPS-67658]: These plugins must work with Gradle 2.5+ (5b963e363d)

### Description
- [LPS-67023]: A `DownloadNodeModuleTask` task is automatically disabled if the
following conditions are met:
	- The task is configured to download in the project's `node_modules`
	directory.
	- The project has a `package.json` file.
	- The `package.json` file contains the same module name in the
	`dependencies` or `devDependencies` object.

## 1.0.23 - 2016-09-20

### Commits
- [LPS-67573]: Export package (d80649bd18)
- [LPS-67573]: Move internal classes to their own packages (9dd224cdb1)
- [LPS-67573]: Remove unused parameter (d80a3d77ce)
- [LPS-67573]: Make methods private to reduce API surface (0bf4bdb787)
- [LPS-66906]: Remove unnecessary cast (25f3c8ffdf)
- [LPS-66906]: Set proxy settings as environment variables (d3ad326f10)
- [LPS-67352]: SF, enforce empty line after finishing referencing variable
(4ff2bb6038)
- [LPS-67352]: Keep empty line between declaring a variable and using it
(ea421a5ab5)

## 1.1.0 - 2016-09-20

### Description
- [LPS-66906]: Add property `inheritProxy` to all tasks that extend
`ExecuteNodeTask`. If `true`, the Java proxy system properties are passed to
Node.js via the environment variables `http_proxy`, `https_proxy`, and
`no_proxy`.
- [LPS-67573]: Make most methods private in order to reduce API surface.
- [LPS-67573]: Move utility classes to the
`com.liferay.gradle.plugins.node.internal` package.

## 1.1.1 - 2016-10-06

### Commits
- [LPS-68564]: Fix task description (0cdf5e142a)
- [LPS-68564]: Bypass SF warning (fa64a53786)
- [LPS-68564]: Use tabs in npm-shrinkwrap.json for consistency (82903e3b6c)
- [LPS-68564]: Add task to call "npm shrinkwrap" and exclude dependencies
(1270dd4ca6)
- [LPS-68231]: Test plugins with Gradle 3.1 (49ec4cdbd8)

## 1.2.0 - 2016-10-06

### Description
- [LPS-68564]: Add task `npmShrinkwrap` to call `npm shrinkwrap` and exclude
unwanted dependencies from the generated `npm-shrinkwrap.json` file.

## 1.2.1 - 2016-10-21

### Commits
- [LPS-66906]: Deprecate old method (052b59dc21)
- [LPS-66906]: By default, remove shrinkwrapped URLs if a registry is set
(a53533c32b)
- [LPS-66709]: Consistency, we use "command line" in all the other READMEs
(b17f3daf06)
- [LPS-66709]: Wordsmithing READMEs for consistency (a3cc8c4c6b)
- [LPS-66709]: Edit gradle-plugins-node README (6843a73be9)
- [LPS-66709]: Add README for gradle-plugins-node (1a4fb0a9cd)

## 1.3.0 - 2016-10-21

### Commits
- [LPS-66906]: Update readme (860f57aff8)

### Description
- [LPS-66906]: Add the ability to use callables and closures as a value for the
`removeShrinkwrappedUrls` property of `NpmInstallTask`.
- [LPS-66906]: Set the `removeShrinkwrappedUrls` property of all tasks that
extend `NpmInstallTask` to `true` by default if the property `registry` has a
value.

## 1.3.1 - 2016-11-29

### Commits
- [LPS-69445]: Use Gradle "exec" if we're running in a Gradle daemon
(8306c4af68)
- [LPS-69445]: Add option to run Node.js via a regular Gradle "exec"
(bbb756b9e7)
- [LPS-69259]: Test plugins with Gradle 3.2.1 (72873ed836)
- [LPS-69259]: Test plugins with Gradle 3.2 (dec6105d3d)

## 1.4.0 - 2016-11-29

### Commits
- [LPS-69445]: Update readme (e1e901c854)

### Description
- [LPS-69445]: Add the `useGradleExec` property to all tasks that extend
`ExecuteNodeTask`. If `true`, Node.js is invoked via
[`project.exec`](https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:exec(org.gradle.api.Action)),
which can solve hanging problems with the Gradle Daemon.

## 1.4.1 - 2016-12-08

### Commits
- [LPS-69618]: Disable up-to-date check for the "npmInstall" task (ce8c7d2854)
- [LPS-66709]: README typo (283446e516)
- [LPS-66709]: Add supported Gradle versions in READMEs (e0d9458520)

### Description
- [LPS-69618]: Disable the up-to-date check for the `npmInstall` task.

## 1.4.2 - 2016-12-14

### Commits
- [LPS-69677]: Not needed (da7a0fac6b)
- [LPS-69677]: Fix NPE and run NPM synchronously if using the global Node
(b94a58d4d5)

### Description
- [LPS-69677]: Fix problem with `ExecuteNpmTask` when `node.download = false`.

## 1.4.3 - 2016-12-21

### Commits
- [LPS-69802]: Clean NPM files and dirs before running "npmShrinkwrap" task
(394ef8643a)

## 1.5.0 - 2016-12-21

### Commits
- [LPS-69802]: Update readme (197e85d133)
- [LPS-66709]: Add links to the NPM documentation (362862a5ab)
- [LPS-68564]: Update readme (bdefd8d8e8)

### Description
- [LPS-69802]: Add the task `cleanNPM` to delete the `node_modules` directory
and the `npm-shrinkwrap.json` file from the project, if present.
- [LPS-69802]: Execute the `cleanNPM` task before generating the
`npm-shrinkwrap.json` file via the `npmShrinkwrap` task.

## 1.5.1 - 2016-12-29

### Commits
- [LPS-69920]: Update readme (882ad1d8df)
- [LPS-69920]: Retry "npm install" 2 times (so run it 3 times max) (0ab6e3085a)
- [LPS-69920]: Reuse argument to retry "npm install" a few times if it fails
(fb6002bbf5)
- [LPS-69802]: Update Node Gradle plugin README (45abdf0dfd)

### Description
- [LPS-69920]: Retry `npm install` automatically if it fails.

## 1.5.2 - 2017-02-09

### Commits
- [LPS-69920]: "npmInstall" does not have up-to-date check anymore (f47c9cae3b)
- [LPS-69920]: Remove IO annotations until
https://github.com/gradle/gradle/issues/1365 is fixed (376df707c5)
- [LPS-70060]: Test plugins with Gradle 3.3 (09bed59a42)

### Description
- [LPS-69920]: Remove up-to-date check from all tasks that extend
`NpmInstallTask`.

## 1.5.3 - 2017-02-23

### Commits
- [LPS-70870]: Pass proxy authentication to Ant (used by FileUtil.get)
(0286be601e)
- [LPS-70870]: Extract method (8d7d822776)
- [LPS-69920]: Reset original args to avoid duplicating them at next runs
(162fa980d4)
- [LPS-69920]: Update readme (ccd98edf8a)
- [LPS-69920]: Expose real list of args (d8a3c28263)

## 2.0.0 - 2017-02-23

### Description
- [LPS-69920]: Fix duplicated NPM arguments while retrying `npm install` in case
of failure.
- [LPS-70870]: Fix Node.js download with authenticated proxies.

## 2.0.1 - 2017-03-09

### Commits
- [LPS-70634]: Don't override package.json if it already exists (9b85118e41)
- [LPS-66709]: Update supported Gradle versions in READMEs (06e315582b)
- [LPS-67573]: Enable semantic versioning check on CI (63d7f4993f)
- [LPS-67352]: Rename *JsonObject -> *JSONObject (55adf55a72)
- [LPS-67352]: SF - Vars named *JSON is reserved for String. Vars named
*JSONObject is reserved for JSONObject. Vars named *JSONArray is reserved for
JSONArray (082700d388)

### Description
- [LPS-70634]: Reuse the `package.json` file of a project, if it exists, while
executing the `PublishNodeModuleTask`.

## 2.0.2 - 2017-03-13

### Commits
- [LPS-71222]: Always sort npm-shrinkwrap.json (e42a18b3f1)

### Description
- [LPS-71222]: Always sort the generated `npm-shrinkwrap.json` files.

## 2.0.3 - 2017-04-11

### Commits
- [LPS-71826]: Make NPM log level configurable (d1da271de5)

## 2.1.0 - 2017-04-11

### Commits
- [LPS-71826]: Update readme (694a93eeed)

### Description
- [LPS-71826]: Add the ability to set the NPM log level by setting the property
`logLevel` of `ExecuteNPMTask`.

## 2.1.1 - 2017-04-25

### Commits
- [LPS-72152]: Update readme (8e96c1ff44)
- [LPS-72152]: Add option to use a different NPM version (569a4e4f04)

## 2.2.0 - 2017-04-25

### Description
- [LPS-72152]: Add property `npmUrl` to all tasks that extend
`DownloadNodeTask`. If set, it downloads a specific version of NPM to override
the one that comes with the Node.js installation.
- [LPS-72152]: Add properties `npmUrl` and `npmVersion` to the `node` extension
object. By default, `npmUrl` is equal to
`https://registry.npmjs.org/npm/-/npm-${node.npmVersion}.tgz`. These properties
let you set a specific version of NPM to download with the `downloadNode` task.

## 2.2.1 - 2017-05-03

### Commits
- [LPS-72340]: Update readme (7c61e05e8d)
- [LPS-72340]: Skip "npmShrinkwrap" task if package.json does not exist
(833701abc8)

### Description
- [LPS-72340]: Skip task `npmShrinkwrap` if project does not contain a
`package.json` file.

## 2.2.2 - 2017-07-07

### Commits
- [LPS-73472]: Run the "build" script during the Java lifecycle (632d05d339)
- [LPS-73472]: Add tasks to run NPM scripts (69738178bf)
- [LPS-73472]: Read the package.json file only once (f65172f069)
- [LRDOCS-3663]: Gradle Node plugin does not support setting the global property
via CLI (af53e8530c)
- [LPS-72429]: Use Collections.addAll() (6c448e59ea)

### Dependencies
- [LPS-72914]: Update the com.liferay.gradle.util dependency to version 1.0.27.

## 2.3.0 - 2017-07-07

### Commits
- [LPS-73472]: Add Gradle test (8fd83a9b11)
- [LPS-73472]: Update readme (fce09c33f7)

### Description
- [LPS-73472]: Add a `npmRum[script]` task for each
[script](https://docs.npmjs.com/misc/scripts) declared in the `package.json`
file.
- [LPS-73472]: Run the `"build"` script (if declared in the `package.json` file)
when compiling a Java project.

## 2.3.1 - 2017-07-17

### Commits
- [LPS-73472]: Remove deprecated method (1840f69439)
- [LPS-73472]: Remove unnecessary property, it makes sense to be always true
(82312e98b5)
- [LPS-73472]: Recreate symbolic links in the ".bin" directory (a5fa38aa50)
- [LPS-73472]: Delete only symlinks in ".bin" directories, keep real files
(9e23d1ad74)
- [LPS-73472]: Edit Readme (837196de5c)

### Dependencies
- [LPS-73584]: Update the com.liferay.gradle.util dependency to version 1.0.29.
- [LPS-73584]: Update the com.liferay.gradle.util dependency to version 1.0.28.

## 3.0.0 - 2017-07-17

### Commits
- [LPS-73472]: Update readme (b13904c177)

### Description
- [LPS-73472]: Recreate symbolic links in the `.bin` directories of
`node_modules` if the `NpmInstallTask`'s `nodeModulesCacheDir` property is set.
- [LPS-73472]: Remove all deprecated methods.
- [LPS-73472]: The `NpmInstallTask`'s `nodeModulesCacheRemoveBinDirs` property
is no longer available.

## 3.0.1 - 2017-08-29

### Commits
- [LPS-73070]: Update readme (1f5f825868)
- [LPS-73070]: Use the global cache concurrently with NPM >= 5 (4aee4b75f1)
- [LPS-73070]: Reuse variable (aba7dbceca)
- [LPS-73070]: Add option to use the NPM cache concurrently, if supported
(660ff1dcf7)
- [LPS-73070]: Move logic to plugin class (375279bdef)
- [LPS-73070]: Rename method to avoid conflict (5ec672eaf5)
- [LPS-73070]: Use package-lock.json to calculate the cache digest (959f408370)
- [LPS-73070]: Extract util method (9cbfd011ff)
- [LPS-73070]: Remove package-lock.json when running the "cleanNPM" task
(72345a135e)

## 3.1.0 - 2017-08-29

### Commits
- [LPS-73472]: Delete before creating the new symlink (58aeefca50)

### Description
- [LPS-73070]: Add the ability to run `ExecuteNpmTask` instances concurrently
even when pointing to a shared NPM's cache directory, if supported.
- [LPS-73070]: By default, use the current user's NPM cache directory
concurrently if running on NPM 5. Prior versions of NPM do not allow for
concurrent access to the cache directory; only one NPM invocation at a time.
- [LPS-73070]: Delete the `package-lock.json` file when running the `cleanNPM`
task, if present.
- [LPS-73070]: Use the `package-lock.json` file to calculate the `node_modules`
cache digest.
- [LPS-73472]: Remove spurious files before recreating symbolic links in the
`.bin` directories of `node_modules`.

## 3.1.1 - 2017-09-18

### Commits
- [LPS-74770]: Add Gradle test (3213d30bf3)
- [LPS-74770]: Run "npm test" with the "check" task (b96373a714)

### Description
- [LPS-74770]: Run the `"test"` script (if declared in the `package.json` file)
when executing the `check` task.

## 3.1.2 - 2017-09-28

### Commits
- [LPS-74933]: Merge package.json from the root dir when publishing (1deb5279b2)
- [LPS-74770]: Update readme (f186fe58cc)

## 3.2.0 - 2017-09-28

### Commits
- [LPS-74933]: Update readme (ef3c37eba4)

### Description
- [LPS-74933]: Add the ability to merge the existing `package.json` of the
project with the values provided by the task properties of
`PublishNodeModuleTask` when publishing a package to the NPM registry.

## 3.2.1 - 2017-10-10

### Commits
- [LPS-75175]: Fix circular task dependency (f2aed246eb)
- [LPS-75175]: Add Gradle test (1111a0b28c)
- [LPS-74770]: Additional wordsmithing for the Node Gradle Plugin's README
(e51c56cabf)
- [LPS-74770]: In some programming languages, you need if/then. In English, you
often do not. (4f6b075e4c)

### Description
- [LPS-75175]: Fix the `downloadNode` task's circular dependency when setting
the `node.global` property to `true` in the root project.

## 3.2.2 - 2017-11-20

### Commits
- [LPS-75965]: Fix readme (24b5d5b9b0)
- [LPS-75965]: Update readme (757d7f9e79)
- [LPS-75965]: Download and use the Windows Node.js archive (ccbfd853dd)

## 4.0.0 - 2017-11-20

### Description
- [LPS-75965]: Download the Node.js Windows distribution if running on Windows.
- [LPS-75965]: The `downloadNode.nodeExeUrl` and `node.nodeExeUrl` properties
are no longer available.

## 4.0.1 - 2018-01-02

### Commits
- [LPS-74904]: Fail the build if all the retries failed (7794f42571)
- [LPS-76644]: Add description to Gradle plugins (5cb7b30e6f)

### Description
- [LPS-74904]: Fail the build if all retries configured in the
`npmInstallRetries` property of an `ExecuteNodeTask` instance have been
exhausted.

## 4.0.2 - 2018-01-17

### Commits
- [LPS-76644]: Enable Gradle plugins publishing (8bfdfd53d7)

## 4.0.3 - 2018-02-08

### Commits
- [LPS-69802]: Remove wrong task dependency (95756ed799)
- [LPS-69802]: Update readme (806f766578)
- [LPS-69802]: Add 'npmPackageLock' task (cfd5235b60)
- [LRDOCS-4129]: Fix Gradle plugin README links (4592b9f829)

## 4.1.0 - 2018-02-08

### Description
- [LPS-69802]: Add the task `npmPackageLock` to delete the NPM files and run
`npm install` to install the dependencies declared in the project's
`package.json` file, if present.

## 4.1.1 - 2018-02-13

### Commits
- [LPS-77996]: Update readme (f9bf327f34)
- [LPS-77996]: Set default value for nodeModulesDigestFile (b84d26b430)
- [LPS-77996]: (f8f017d085)

## 4.2.0 - 2018-02-13

### Commits
- [LPS-77996]: Reuse path instance (4b05326b96)
- [LPS-77996]: Delete the node_modules dir if there's no digest (ab53f71c97)
- [LPS-77996]: By forcing "reset" to true, we'll remove the node_modules dir
(eafb87484f)
- [LPS-77996]: This method doesn't change any file outside the project dir
(dbbd8c5e5b)
- [LPS-77996]: Let's enable this new feature only in our modules (f9806a4fbb)
- [LPS-77996]: Fix readme (3bec8177c9)
- [LPS-77996]: Use project.delete (b1ccdbca00)

### Description
- [LPS-77996]: Add the property `nodeModulesDigestFile`. If this property is
set, the digest is compared with the `node_modules` directory's digest. If they
don't match, the `node_modules` directory is deleted before running
`npm install`.

## 4.2.1 - 2018-03-15

### Commits
- [LPS-73472]: Allow single "bin" values in package.json (5104167c2f)
- [LPS-78741]: Update readme (217b0808d3)
- [LPS-78741]: Simplify (the default value is false) (98ee03c627)
- [LPS-78741]: Add boolean argument to run `npm ci` (15e543b9b7)
- [LPS-77425]: Partial revert of d25f48516a9ad080bcbd50e228979853d3f2dda5
(60d3a950d6)
- [LPS-77425]: Increment all major versions (d25f48516a)

### Dependencies
- [LPS-77425]: Update the com.liferay.gradle.util dependency to version 1.0.29.

## 4.3.0 - 2018-03-15

### Description
- [LPS-78741]: Add the property `useNpmCI` to the `NpmInstallTask`. If `true`,
run `npm ci` instead of `npm install`.
- [LPS-73472]: Allow single `"bin"` values in the `package.json` files.

## 4.3.1 - 2018-03-22

### Commits
- [LPS-78741]: Update readme (570b6170b2)
- [LPS-78741]: Skip running 'npm install' if the saved digest matches with the
new digest (7d67225c0f)

### Description
- [LPS-78741]: Do not run `npm install` if the `nodeModulesDigestFile` matches
the `node_modules` directory's digest.

## 4.3.2 - 2018-03-30

### Commits
- [LPS-78741]: Update readme (853eaa597d)
- [LPS-78741]: Check digest before running 'npm ci' (21195cb57b)

### Description
- [LPS-78741]: Do not run `npm ci` if the `nodeModulesDigestFile` matches the
`node_modules` directory's digest.

## 4.3.3 - 2018-04-05

### Commits
- [LPS-78741]: Update readme (374aa74891)
- [LPS-78741]: Use 'npm ci' only if the 'pacakge-lock.json' file exists
(df2cc0420e)

### Description
- [LPS-78741]: Fix the `npmPackageLock` task execution when the `npmInstall`
task's `useNpmCI` property is set to `true`.

## 4.3.4 - 2018-05-07

### Commits
- [LPS-75530]: outputs for npmInstall (395da5841e)
- [LPS-78741]: Readme fixes (a865c2bcf4)

### Description
- [LPS-75530]: Define task inputs and outputs for `NpmInstallTask`.

## 4.3.5 - 2018-06-08

### Commits
- [LPS-82310]: Recreate "npm" symbolic link (e4e51be325)

### Description
- [LPS-82130]: Fix the broken `npm` symbolic link.

## 4.3.6 - 2018-06-22

### Commits
- [LPS-82568]: Log environment variables (7c14fb2e9c)
- [LPS-82568]: Allow to pass environment variables to Node and NPM (7644469a3c)

## 4.4.0 - 2018-06-22

### Description
- [LPS-82568]: Add the property `environment` to all tasks that extend
`ExecuteNodeTask`. This provides a way to set environment variables.

## 4.4.1 - 2018-10-03

### Commits
- [LPS-85959]: Update readme (35e60010f4)
- [LPS-85959]: Delete the npm cached data before retrying npm install
(f525119df2)
- [LPS-71117]: Test plugins with Gradle up to 3.5.1 (c3e12d1cf3)
- [LPS-71117]: Update supported Gradle versions in READMEs (fdcc16c0d4)
- [LPS-67658]: Fix gradleTest for windows (6386e70e90)
- [LPS-82568]: Fix readme (8b71557f78)
- [LPS-82568]: Update readme (7af903d2a7)

### Dependencies
- [LPS-84094]: Update the com.liferay.gradle.util dependency to version 1.0.31.
- [LPS-84094]: Update the com.liferay.gradle.util dependency to version 1.0.30.

### Description
- [LPS-85959]: Delete the NPM cached data before retrying `npm install`.

## 4.4.2 - 2018-10-09

### Commits
- [LPS-85959]: Update readme (9ad882d597)
- [LPS-85959]: Update log message (a6c882d669)
- [LPS-85959]: Use 'npm cache verify' instead (e14ed0a697)

### Description
- [LPS-85959]: Verify the NPM cached data before retrying `npm install`.

## 4.4.3 - 2018-10-22

### Commits
- [LPS-86576]: Simplify gradle tests (use Node.js 5.5.0) (3cfaed9d50)
- [LPS-86576]: Fix downloadNode task for Windows (Node.js versions 5.5.0-6.2.0)
(051ea17262)
- [LPS-84119]: SF, declare when used (efa63307f1)

### Description
- [LPS-86576]: Node.js provides Windows binaries bundled with NPM and Node.js
beginning from version 6.2.1. Download and install Node.js and NPM separately
for Node.js versions 5.5.0 - 6.2.0.

## 4.4.4 - 2018-11-16

### Commits
- [LPS-87192]: Set the Eclipse task property gradleVersion (040b2abdee)
- [LPS-87192]: Add variable gradleVersion (no logic changes) (2f7c0b2fe4)
- [LPS-85609]: Fix for CI (test only 4.10.2) (4eed005731)
- [LPS-85609]: Test plugins up to Gradle 4.10.2 (60905bc960)
- [LPS-85609]: Update supported Gradle versions (d79b89682b)
- [LPS-86589]: Update readme (4280a3d596)
- [LPS-86589]: Test Gradle plugins from Gradle 2.14.1 to 3.5.1 (6df521a506)

### Dependencies
- [LPS-87466]: Update the com.liferay.gradle.util dependency to version 1.0.32.

## 4.4.5 - 2018-11-16

### Commits
- [LPS-87465]: Update readme (1ad0bbf3f1)
- [LPS-87465]: Move production property to the executeNPM task (f0e2d2ea29)
- [LPS-87465]: The executeNPM task has a registry property (b41de3e7d9)
- [LPS-87465]: set production and registry flags for npmIstall (e32aa79975)

## 4.5.0 - 2018-11-16

### Description
- [LPS-87465]: Add the property `production` to all tasks that extend
`ExecuteNodeTask`. If `true`,
[`devDependencies`](https://docs.npmjs.com/files/package.json#devdependencies)
are not installed when running `npm install` without any arguments and sets
`NODE_ENV=production` for lifecycle scripts.

## 4.5.1 - 2018-11-19

### Dependencies
- [LPS-87466]: Update the com.liferay.gradle.util dependency to version 1.0.33.

## 4.5.2 - 2019-01-07

### Commits
- [LPS-87479]: Create the directory if the script deletes it (c679cf96b5)
- [LPS-87479]: Remove redundant input checks (bb6300fcb5)
- [LPS-87479]: Update default includes (add *.json and *.jsx) (02322a1475)
- [LPS-87479]: Improve the up-to-date check for npmInstall (ec0fadeb00)
- [LPS-87479]: Update the npmInstall digest check (no logic changes)
(b515e04aa8)
- [LPS-87479]: Set the up-to-date check to false if the classes directory does
not exist (8625065553)
- [LPS-87479]: Sort attributes alphabetically (5bfe771d93)
- [LPS-87479]: Move logic to StringUtil (aba3a15477)
- [LPS-87479]: Make the task name logic more generic (fcb1764705)
- [LPS-87479]: Update scriptName property for consistency with other gradle
plugins (fffd355d2a)
- [LPS-87479]: Check package.json files and Node.js version to improve
performance (8c2c3f0809)
- [LPS-87479]: Make the source files optional (5e7aa83c87)
- [LPS-87479]: Create a digest from the sources (3494f40166)
- [LPS-87479]: Update sources (e19724e6bc)
- [LPS-87479]: Add option for includes and excludes (c6c84041dd)
- [LPS-87479]: camelCase npmRun task names (e8cb9d43d6)
- [LPS-87479]: create NpmRunTask and set inputs and outputs so Gradle can check
to see if task is uptodate (e4ad8d8111)
- [LPS-85609]: Update readme (c182ff396d)
- [LPS-85609]: Simplify gradleTest (a8b0feff31)
- [LPS-85609]: Use Gradle 4.10.2 (9aa90f8961)

## 4.6.0 - 2019-01-07

### Description
- [LPS-87479]: Update the [Liferay Gradle Util] dependency to version 1.0.33.
- [LPS-87479]: Add inputs and outputs for tasks of type `NpmRunTask` to skip
rerunning the task if none of the inputs or outputs have changed.
- [LPS-87479]: Update the inputs for tasks of type `NpmInstallTask` to use the
`nodeModulesDigestFile` instead of the `node_modules` directory as an input.

## 4.6.1 - 2019-01-09

### Commits
- [LPS-88909]: Prevent the processResources task from overwriting transpiled
Javascript files (4f6e6bbbb1)
- [LPS-88909]: Use NpmRunTask (no logic changes) (1e238ce693)

### Description
- [LPS-88909]: The `processResources` task will skip overwriting files ending
with `.es.js` if the `npmRun.sourceDigestFile` matches the `npmRun.sourceFiles`
digest to preserve changes made by the `npmRun` task.

## 4.6.2 - 2019-01-09

### Commits
- [LPS-87479]: Check the classes directory for Java projects (6e5549beb9)

### Description
- [LPS-87479]: Set the up-to-date check for `npmRun` tasks to `true` if the
classes directory does not exist for Java projects.

## 4.6.3 - 2019-01-14

### Commits
- [LPS-89126]: Update outputs for the npmInstall task (34504e3e82)

### Description
- [LPS-89126]: Fix failures in parallel builds by updating the outputs for tasks
of type `NpmInstallTask` to check the `nodeModulesDir` instead of the
`nodeModulesDigestFile`.

## 4.6.4 - 2019-01-16

### Commits
- [LPS-88909]: Include all files that end with the js extension (16443339ad)
- [LPS-88909]: Prevent processResources from overwriting transpiled files
(294d4807e3)

### Description
- [LPS-88909]: If the `npmRun.sourceDigestFile` matches the `npmRun.sourceFiles`
digest, all files ending with the `js` extension in the classes directory will
be copied before the `processResources` task runs and then copied back to
preserve changes made by the `npmRun` task.

## 4.6.5 - 2019-01-24

### Commits
- [LPS-89436]: Add Soy files to inputs (d0862c127a)
- [LPS-89436]: Sort alphabetically (no logic changes) (7fa06d88f9)
- [LPS-89369]: Delete the digest to avoid skipping the task (f877c8e96b)
- [LPS-89369]: Catch Soy compile errors (31311019ea)

### Description
- [LPS-89369]: The `npmRun` task should fail when there are Soy compile errors.
- [LPS-89436]: Add Soy files as inputs for tasks of type `NpmRunTask`.

## 4.6.6 - 2019-02-04

### Commits
- [LPS-89916]: Add ability to filter npmLinkTasks (0f43909b73)
- [LPS-89916]: Add npmLink tasks (7f2c72c770)
- [LPS-89916]: Move common logic to base class (744281bde9)

## 4.6.7 - 2019-02-20

### Commits
- [LPS-90945]: Remove org.apache.commons dependency (7dff9f303d)

## 4.6.8 - 2019-03-20

### Commits
- [LPS-91967]: Move reusable logic into FileUtil (f10dc01f04)
- [LPS-91967]: Add ability to override the default NPM command (d8f29b10ea)
- [LPS-91967]: Add ability to override the default Node modules directory
(a9d4a2ef6b)
- [LPS-91967]: Check script file name before applying NPM arguments (133df2f1d2)

## 4.6.9 - 2019-04-03

### Commits
- [LPS-93258]: Enable the production flag for Yarn (9d1aeab7bc)
- [LPS-93258]: Enable the registry flag for Yarn (91c438e2c4)
- [LPS-91967]: Skip node_modules_cache directory (43a618c849)

## 4.6.10 - 2019-04-10

### Commits
- [LPS-91967]: Configure DownloadNodeModuleTask for Yarn (f59ecd4e7d)

## 4.6.11 - 2019-04-11

### Commits
- [LPS-91967]: Configure DownloadNodeModuleTask for Yarn (408ce16abc)

## 4.6.12 - 2019-04-25

### Commits
- [LPS-77425]: Update the npmrc file location for Yarn (8afaa2938b)

## 4.6.13 - 2019-05-01

### Commits
- [LPS-91967]: Update the digest path so its the same for both NPM and Yarn
(22071074bc)

## 4.6.14 - 2019-05-06

### Commits
- [LPS-91967]: Ignore nodejs.npm.args for Yarn (0ef0891f12)

## 4.6.15 - 2019-05-06

### Commits
- [LPS-94947]: Use Collections#emptyList for performance (46ca71a49d)
- [LPS-94947]: deleteAllActions is deprecated and removed in Gradle 5.x
(b456fce82d)

## 4.6.16 - 2019-05-24

### Commits
- [LPS-88909]: Disable cache (see
https://github.com/liferay/liferay-portal/commit/fd8763c#diff-082f1d3a132d6a95e4d04c1b3ee8ccedR155)
(21e1e8f960)

## 4.6.17 - 2019-06-10

### Commits
- [LPS-93220]: Logging (dbf8127026)
- [LPS-93220]: Check the cli directory too (fix for babel) (eb24ea1ac7)
- [LPS-93220]: Create deleted NPM symbolic links (cdd077348b)
- [LPS-93220]: Abstract out reusable logic (no logic changes) (22aba734ba)
- [LPS-96376]: Update to liferay-npm-scripts v2.1.0 (prettier) (7930ab3625)
- [LPS-0]: SF. Space character correction for build scripts & READMEs
(9dd5d12c9a)
- []: Revert "LPS-88909 Disable cache (see
https://github.com/liferay/liferay-portal/commit/fd8763c#diff-082f1d3a132d6a95e4d04c1b3ee8ccedR155)"
(c0cc8d0404)

## 4.6.18 - 2019-06-21

### Commits
- [LPS-96247]: Update (7385039e86)
- [LPS-96247]: Source formatting (aae01ea1d4)
- [LPS-96247]: Migrate away from deprecated SourceSetOutput.getClassesDir
(5b0f9860d5)

### Dependencies
- [LPS-96247]: Update the com.liferay.gradle.util dependency to version 1.0.34.

## 4.6.20 - 2019-08-14

### Commits
- [LPS-99774]: Expose constant (961034d5fb)

## 4.7.1 - 2019-08-19

### Commits
- [LPS-99977]: Remove package.json link tasks from the build group (bd900159af)
- [LPS-99977]: Create digest file for just the build script in package.json
(dba8424c41)
- [LPS-99977]: Add SuppressWarnings (4dcd2a1c1f)
- [LPS-99977]: Rename NpmRunTask to PackageRunTask (5731f4c5ea)
- [LPS-99977]: Rename NpmLinkTask to PackageLinkTask (d01483ea92)
- [LPS-99977]: Rename BaseNpmCommandTask to ExecutePackageManagerDigestTask
(4a38fdb2a3)
- [LPS-99977]: Rename ExecuteNpmTask to ExecutePackageManagerTask (e47fedfc9e)

## 4.7.2 - 2019-08-21

### Commits
- [LPS-100168]: Add LiferayYarnPlugin (6620b72135)
- [LPS-100168]: Move Yarn logic to gradle-plugins-node (ce1ab89ae2)

## 4.8.1 - 2019-08-24

### Commits
- [LPS-100168]: Fix gradleTest (55ca3f97c1)
- [LPS-100168]: Update tasks that extend ExecutePackageManagerTask (381329db65)
- [LPS-100168]: Use new property (bcefa7a3b1)
- [LPS-100168]: Add useNpm to NodeExtension (0a22995cf5)

## 4.9.1 - 2019-08-28

### Commits
- [LPS-100163]: Remove Gradle task packageLinks (e17f3ba33a)

## 5.0.1 - 2019-09-16

### Commits
- [LRQA-52072]: Add property to ignore failures for packageRunTest (61b712b72c)
- [LRQA-52072]: Add PackageRunTestTask (no logic changes) (3e5b79d009)

## 5.1.1 - 2019-09-18

### Commits
- [LPS-101470]: Source formatting (4f1067a006)
- [LPS-101470]: Move digest inputs into a separate method (a60750e64b)
- [LPS-101470]: Rename method (1595244c98)
- [LPS-101470]: Inline (a9d1f7bf97)
- [LPS-101470]: Avoid swallowing exception (57a0c91c07)
- [LPS-101470]: Filter out null values (npmUrl is an optional input)
(e3f24d6f82)
- [LPS-101470]: Use logic from DigestUtil (218458d57f)
- [LPS-101470]: Consolidate (edfba9e995)
- [LPS-101470]: Move existing method to DigestUtil (cae9d33aca)
- [LPS-101470]: Move logic to DigestUtil (d74c04d5f6)
- [LPS-101470]: After changing nodeVersion, the version of node is not correctly
changed (c0d952fb13)

## 5.1.2 - 2019-09-19

### Commits
- [LPS-101470]: Fix typo (58c95da818)

## 5.1.3 - 2019-10-16

### Commits
- [LPS-102367]: Just check the major version (7fb17bd806)
- [LPS-102367]: Check for liferay-npm-scripts version 12 (3d847962de)
- [LPS-102367]: Add node_modules directory to packageRunBuild inputs
(2f472bbe0c)
- [LPS-102367]: Add system property to turn off the cache (b1c4a24b6e)
- [LPS-102367]: Add incremental check (f9dbe64130)
- [LPS-102367]: Rerun the jar task (12beba1b06)
- [LPS-102367]: Run packageRunBuild before processResources if
liferay-npm-scripts version is greater than 10 (2f12ed7710)
- [LPS-102367]: Source formatting (f65072896c)
- [LPS-102367]: Add scriptFile property to NodeExtension (1af32195ad)
- [LPS-102367]: Remove stale digest check (8dc60dd4ae)

## 6.0.1 - 2019-10-21

### Commits
- [LPS-102367]: The scriptFile property must check useNpm (52c2c7f703)
- [LPS-102367]: Move reusable logic to NodePluginUtil (5ba5c9babb)

## 6.0.2 - 2019-10-23

### Commits
- [LPS-103580]: Semver (fed965fad1)
- [LPS-103580]: Update method name (190ba2834f)
- [LPS-103580]: Enable incremental cache for only Yarn projects (42bf3330ff)
- [LPS-103580]: Update input to check the Yarn project's node_modules directory
(feecab8bef)