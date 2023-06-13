package ${configYAML.apiPackagePath}.internal.resource.${versionDirName};

import ${configYAML.apiPackagePath}.resource.${versionDirName}.${schemaName}Resource;

import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;

import java.util.Date;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author ${configYAML.author}
 */
@Component(
	properties = "OSGI-INF/liferay/rest/${versionDirName}/${schemaPath}.properties",
	scope = ServiceScope.PROTOTYPE, service = ${schemaName}Resource.class
)
public class ${schemaName}ResourceImpl extends Base${schemaName}ResourceImpl {
}