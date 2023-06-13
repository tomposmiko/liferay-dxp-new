## String methods

Methods that are deal with case sensitivity in `java.util.String` are
inefficient.
While in reality most Strings contain `ASCII` characters only, the methods will
use `Unicode` detection.

The methods in `com.liferay.portal.kernel.util.StringUtil` will assume the
String only contains `ASCII` which makes the detection much faster. When a
`non-ASCII` character is detected if will fall back on the methods in
`java.util.String`

So instead of the following methods in `java.util.String` use, the counterpart
in `com.liferay.portal.kernel.util.StringUtil` instead.

- `equalsIgnoreCase`
- `toLowerCase`
- `toUpperCase`

For better performance, enforce Character replacement over String replacement,
when possible.

Instead of `StringUtil.replace(s, "$", "DOLLAR")` we should use
`StringUtil.replace(s, '$', "DOLLAR")`