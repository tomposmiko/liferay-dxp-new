import escape from 'lodash.escape';
import groupBy from 'lodash.groupby';
import isEqual from 'lodash.isequal';
import portlet from './portlet/portlet.es';
import navigate from './util/navigate.es';
import ns from './util/ns.es';
import objectToFormData from './util/object_to_form_data.es';
import toCharCode from './util/to_char_code.es';
import unescape from 'lodash.unescape';

Liferay.Util.escape = escape;
Liferay.Util.groupBy = groupBy;
Liferay.Util.isEqual = isEqual;
Liferay.Util.navigate = navigate;
Liferay.Util.ns = ns;
Liferay.Util.objectToFormData = objectToFormData;
Liferay.Util.toCharCode = toCharCode;

Liferay.Util.openToast = (...args) => {
	Liferay.Loader.require(
		'frontend-js-web/liferay/toast/commands/OpenToast.es',
		commands => {
			commands.openToast(...args);
		}
	);
};

Liferay.Util.unescape = unescape;

export {portlet};