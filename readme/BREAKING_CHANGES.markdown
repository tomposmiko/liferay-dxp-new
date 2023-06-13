# What are the Breaking Changes for Liferay 7.2?

This document presents a chronological list of changes that break existing
functionality, APIs, or contracts with third party Liferay developers or users.
We try our best to minimize these disruptions, but sometimes they are
unavoidable.

Here are some of the types of changes documented in this file:

* Functionality that is removed or replaced
* API incompatibilities: Changes to public Java or JavaScript APIs
* Changes to context variables available to templates
* Changes in CSS classes available to Liferay themes and portlets
* Configuration changes: Changes in configuration files, like
  `portal.properties`, `system.properties`, etc.
* Execution requirements: Java version, J2EE Version, browser versions, etc.
* Deprecations or end of support: For example, warning that a certain
  feature or API will be dropped in an upcoming version.
* Recommendations: For example, recommending using a newly introduced API that
  replaces an old API, in spite of the old API being kept in Liferay Portal for
  backwards compatibility.

*This document has been reviewed through commit `c3b54108fcd4`.*

## Breaking Changes Contribution Guidelines

Each change must have a brief descriptive title and contain the following
information:

* **[Title]** Provide a brief descriptive title. Use past tense and follow
  the capitalization rules from
  <http://en.wikibooks.org/wiki/Basic_Book_Design/Capitalizing_Words_in_Titles>.
* **Date:** Specify the date you submitted the change. Format the date as
  *YYYY-MMM-DD* (e.g., 2014-Feb-25).
* **JIRA Ticket:** Reference the related JIRA ticket (e.g., LPS-12345)
  (Optional).
* **What changed?** Identify the affected component and the type of change that
  was made.
* **Who is affected?** Are end-users affected? Are developers affected? If the
  only affected people are those using a certain feature or API, say so.
* **How should I update my code?** Explain any client code changes required.
* **Why was this change made?** Explain the reason for the change. If
  applicable, justify why the breaking change was made instead of following a
  deprecation process.

Here's the template to use for each breaking change (note how it ends with a
horizontal rule):

```
### Title
- **Date:**
- **JIRA Ticket:**

#### What changed?

#### Who is affected?

#### How should I update my code?

#### Why was this change made?

---------------------------------------
```

**80 Columns Rule:** Text should not exceed 80 columns. Keeping text within 80
columns makes it easier to see the changes made between different versions of
the document. Titles, links, and tables are exempt from this rule. Code samples
must follow the column rules specified in Liferay's
[Development Style](http://www.liferay.com/community/wiki/-/wiki/Main/Liferay+development+style).

The remaining content of this document consists of the breaking changes listed
in ascending chronological order.

## Breaking Changes List

### Removed Support for JSP Templates in Themes
- **Date:** 2018-Nov-14
- **JIRA Ticket:** [LPS-87064](https://issues.liferay.com/browse/LPS-87064)

#### What changed?

Themes can no longer leverage JSP templates. Also, related logic has been
removed from the public APIs `com.liferay.portal.kernel.util.ThemeHelper` and
`com.liferay.taglib.util.ThemeUtil`.

#### Who is affected?

This affects anyone who has themes using JSP templates or is using the removed
methods.

#### How should I update my code?

If you have a theme using JSP templates, consider migrating it to FreeMarker.

#### Why was this change made?

JSP is not a real template engine and is rarely used. FreeMarker is the
recommended template engine moving forward.

The removal of JSP templates allows for an increased focus on existing and new
template engines.

---------------------------------------

### Lodash Is No Longer Included by Default
- **Date:** 2018-Nov-27
- **JIRA Ticket:** [LPS-87677](https://issues.liferay.com/browse/LPS-87677)

#### What changed?

Previously, Lodash was included in every page by default and made available
through the global `window._` and scoped `AUI._` variables. Lodash is no longer
included by default and those variables are now undefined.

#### Who is affected?

This affects any developer who used the `AUI._` or `window._` variables in their
custom scripts.

#### How should I update my code?

You should provide your own Lodash version for your custom developments to use
following any of the possible strategies to add third party libraries.

As a temporary measure, you can bring back the old behavior by setting the
*Enable Lodash* property in Liferay Portal's *Control Panel* &rarr;
*Configuration* &rarr; *System Settings* &rarr; *Third Party* &rarr; *Lodash* to
`true`.

#### Why was this change made?

This change was made to avoid bundling and serving additional library code on
every page that was mostly unused and redundant.

---------------------------------------

### Moved Two Staging Properties to OSGi Configuration
- **Date:** 2018-Dec-12
- **JIRA Ticket:** [LPS-88018](https://issues.liferay.com/browse/LPS-88018)

#### What changed?

Two Staging properties have been moved from `portal.properties` to an
OSGi configuration named `ExportImportServiceConfiguration.java` in the
`export-import-service` module.

#### Who is affected?

This affects anyone using the following portal properties:

- `staging.delete.temp.lar.on.failure`
- `staging.delete.temp.lar.on.success`

#### How should I update my code?

Instead of overriding the `portal.properties` file, you can manage the
properties from Portal's configuration administrator. This can be accessed by
navigating to Liferay Portal's *Control Panel* &rarr; *Configuration* &rarr;
*System Settings* &rarr; *Infrastructure* &rarr; *Export/Import* and editing
the settings there.

If you would like to include the new configuration in your application, follow
the instructions for
[making applications configurable](https://dev.liferay.com/develop/tutorials/-/knowledge_base/7-1/making-applications-configurable).

#### Why was this change made?

This change was made as part of the modularization efforts to ease portal
configuration changes.

---------------------------------------

### Remove Link Application URLs to Page Functionality
- **Date:** 2018-Dec-14
- **JIRA Ticket:** [LPS-85948](https://issues.liferay.com/browse/LPS-85948)

#### What changed?

The *Link Portlet URLs to Page* option in the Look and Feel portlet was marked
as deprecated in Liferay Portal 7.1, allowing the user to show and hide the
option through a configuration property. In Liferay Portal 7.2, this has been
removed and can no longer be configured.

#### Who is affected?

This affects administrators who used the option in the UI and developers who
leveraged the option in the portlet.

#### How should I update my code?

You should update any portlets leveraging this feature, since any preconfigured
reference to the property is ignored in the portal.

#### Why was this change made?

A limited number of portlets use this property; there are better ways to achieve
the same results.

---------------------------------------

### Move TermsOfUseContentProvider out of kernel.util
- **Date:** 2019-Jan-07
- **JIRA Ticket:** [LPS-88869](https://issues.liferay.com/browse/LPS-88869)

#### What changed?

Interface `TermsOfUseContentProvider` in package `com.liferay.portal.kernel.util`
was moved to package `com.liferay.portal.kernel.term.of.use`.
`TermsOfUseContentProviderRegistryUtil` in package `com.liferay.portal.kernel.util`
was moved to package `com.liferay.portal.internal.terms.of.use` and renamed to
`TermsOfUseContentProviderUtil`.
The logic of getting `TermsOfUseContentProvider` was changed. Instead of always
returning the first service registered, which is random and depends on the order
of service registered, we keep track of the `TermsOfUseContentProvider` service
and update it with `com.liferay.portal.kernel.util.ServiceProxyFactory`. As a
result, the `TermsOfUseContentProvider` we are getting now respects service
ranking.

#### Who is affected?

This affects anyone who used `TermsOfUseContentProviderRegistryUtil` in package
`com.liferay.portal.kernel.util` to lookup `TermsOfUseContentProvider` service
originally in package `com.liferay.portal.kernel.util`

#### How should I update my code?

If `com.liferay.portal.kernel.util.TermsOfUseContentProvider` is used, update
the import package name. If there is any usage in `portal-web`, update
`com.liferay.portal.kernel.util.TermsOfUseContentProviderRegistryUtil` to
`com.liferay.portal.kernel.term.of.use.TermsOfUseContentProviderUtil`. Remove
usages of `com.liferay.portal.kernel.util.TermsOfUseContentProviderRegistryUtil`
in modules and use @Reference annotation to fetch the
`com.liferay.portal.kernel.term.of.use.TermsOfUseContentProvider` service
instead.

#### Why was this change made?

It's one of several steps to clean up kernel provider interfaces to reduce the
chance of package version lock down.

---------------------------------------

### Remove HibernateConfigurationConverter and Converter
- **Date:** 2019-Jan-07
- **JIRA Ticket:** [LPS-88870](https://issues.liferay.com/browse/LPS-88870)

#### What changed?

Interface `com.liferay.portal.kernel.util.Converter` and its implementation
`com.liferay.portal.spring.hibernate.HibernateConfigurationConverter` were
removed.

#### Who is affected?

This removes the support of generating customized `portlet-hbm.xml` implemented
by `HibernateConfigurationConverter`.
Please refer to [LPS-5363](https://issues.liferay.com/browse/LPS-5363).

#### How should I update my code?

Remove usages of `HibernateConfigurationConverter`. Make sure the generated
`portlet-hbm.xml` is accurate.

#### Why was this change made?

It's one of several steps to clean up kernel provider interfaces to reduce the
chance of package version lock down.

---------------------------------------

### Switch to use JDK Function and Supplier
- **Date:** 2019-Jan-08
- **JIRA Ticket:** [LPS-88911](https://issues.liferay.com/browse/LPS-88911)

#### What changed?

`Function` and `Supplier` in package `com.liferay.portal.kernel.util` were
removed. Their usages were replaced with `java.util.function.Function` and
`java.util.function.Supplier`.

#### Who is affected?

This affects anyone who used `Function` and `Supplier` in package
`com.liferay.portal.kernel.util`.

#### How should I update my code?

Replace usages of `com.liferay.portal.kernel.util.Function` with
`java.util.function.Function`. Replace usages of
`com.liferay.portal.kernel.util.Supplier` with `java.util.function.Supplier`.

#### Why was this change made?

It's one of several steps to clean up kernel provider interfaces to reduce the
chance of package version lock down.

---------------------------------------

### Deprecate com.liferay.portal.service.InvokableService
- **Date:** 2019-Jan-08
- **JIRA Ticket:** [LPS-88912](https://issues.liferay.com/browse/LPS-88912)

#### What changed?

Interface `InvokableService` and `InvokableLocalService` in package
`com.liferay.portal.kernel.service` were removed.

#### Who is affected?

This affects anyone who used `InvokableService` and `InvokableLocalService` in
package `com.liferay.portal.kernel.service`.

#### How should I update my code?

Remove usages of `InvokableService` and `InvokableLocalService`. Make sure to
use the latest version of `ServiceBuilder` to generate implementations for
services in case there is any compile error after the removal.

#### Why was this change made?

It's one of several steps to clean up kernel provider interfaces to reduce the
chance of package version lock down.

---------------------------------------

### Drop support of ServiceLoaderCondition
- **Date:** 2019-Jan-08
- **JIRA Ticket:** [LPS-88913](https://issues.liferay.com/browse/LPS-88913)

#### What changed?

Interface `ServiceLoaderCondition` and its implementation
`DefaultServiceLoaderCondition` in package `com.liferay.portal.kernel.util` were
removed.

#### Who is affected?

This affects anyone used `ServiceLoaderCondition` and
`DefaultServiceLoaderCondition`.

#### How should I update my code?

Remove usages of `ServiceLoaderCondition`. Update usages of `load` methods in
`com.liferay.portal.kernel.util.ServiceLoader` according to the updated method
signatures.

#### Why was this change made?

It's one of several steps to clean up kernel provider interfaces to reduce the
chance of package version lock down.

---------------------------------------

### Switch to use JDK Predicate
- **Date:** 2019-Jan-14
- **JIRA Ticket:** [LPS-89139](https://issues.liferay.com/browse/LPS-89139)

#### What changed?

Interface `com.liferay.portal.kernel.util.PredicateFilter` was removed and
replaced with `java.util.function.Predicate`. As a result of that, all
implementations of the interface: `AggregatePredicateFilter`,
`PrefixPredicateFilter` in package `com.liferay.portal.kernel.util`,
`JavaScriptPortletResourcePredicateFilter` in package
`com.liferay.portal.kernel.portlet` and `DDMFormFieldValuePredicateFilter`
in package `com.liferay.dynamic.data.mapping.form.values.query.internal.model`
were removed. `com.liferay.portal.kernel.util.ArrayUtil_IW` was regenerated.

#### Who is affected?

This affects anyone who used `PredicateFilter`, `AggregatePredicateFilter`,
`PrefixPredicateFilter`, `JavaScriptPortletResourcePredicateFilter` and
`DDMFormFieldValuePredicateFilter`.

#### How should I update my code?

Replace usages of `com.liferay.portal.kernel.util.PredicateFilter` with
`java.util.function.Predicate`. Remove usages of `AggregatePredicateFilter`,
`PrefixPredicateFilter`, `JavaScriptPortletResourcePredicateFilter` and
`DDMFormFieldValuePredicateFilter`.

#### Why was this change made?

It's one of several steps to clean up kernel provider interfaces to reduce the
chance of package version lock down.

---------------------------------------

### Remove unsafe functional interfaces in package com.liferay.portal.kernel.util
- **Date:** 2019-Jan-15
- **JIRA Ticket:** [LPS-89223](https://issues.liferay.com/browse/LPS-89223)

#### What changed?

`com.liferay.portal.osgi.util.test.OSGiServiceUtil` was removed.
`UnsafeConsumer`, `UnsafeFunction` and `UnsafeRunnable` in package
`com.liferay.portal.kernel.util` were removed.

#### Who is affected?

This affects anyone used `com.liferay.portal.osgi.util.test.OSGiServiceUtil` and
`UnsafeConsumer`, `UnsafeFunction`, `UnsafeRunnable` in package
`com.liferay.portal.kernel.util`

#### How should I update my code?

`com.liferay.portal.osgi.util.test.OSGiServiceUtil` has been deprecated since
7.1. If there is still any usage of the class, replace it with its direct
replacement `com.liferay.osgi.util.service.OSGiServiceUtil`. Replace usages of
`UnsafeConsumer`, `UnsafeFunction` and `UnsafeRunnable` with corresponding interface
in package `com.liferay.petra.function`.

#### Why was this change made?

It's one of several steps to clean up kernel provider interfaces to reduce the
chance of package version lock down.

---------------------------------------

### Deprecated NTLM in Portal Distribution
- **Date:** 2019-Jan-21
- **JIRA Ticket:** [LPS-88300](https://issues.liferay.com/browse/LPS-88300)

#### What changed?

Four NTLM modules have been moved from the `portal-security-sso` project to a
new project named `portal-security-sso-ntlm`. This new project is deprecated and
is available to download from Liferay Marketplace.

#### Who is affected?

This affects anyone using NTLM as an authentication system.

#### How should I update my code?

If you want to continue using NTLM as an authentication system, you must
download the corresponding modules from Liferay Marketplace. Alternatively, you
can migrate to Kerberos (recommended), which requires no changes and is
compatible with Liferay Portal 7.0+.

#### Why was this change made?

This change was made to avoid using an old proprietary solution (NTLM). Kerberos
is now recommended, which is a standard protocol and a more secure method of
authentication compared to NTLM.

---------------------------------------