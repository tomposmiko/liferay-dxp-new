#!/bin/bash

function main {
	mkdir -p dist

	if [ ! -e dist/liferay-ce-portal-tomcat-7.1.1-ga2-20181101125651026.7z ]
	then
		wget -P dist https://releases.liferay.com/portal/7.1.1-ga2/.backup/liferay-ce-portal-tomcat-7.1.1-ga2-20181101125651026.7z
	fi

	if [ ! -e dist/liferay-ce-portal-wildfly-7.1.1-ga2-20181101125651026.7z ]
	then
		wget -P dist https://releases.liferay.com/portal/7.1.1-ga2/.backup/liferay-ce-portal-wildfly-7.1.1-ga2-20181101125651026.7z
	fi

	cd dist

	rm -f *.lpkg
	rm -f *.jar

	#
	# Decompress Tomcat.
	#

	rm -fr liferay-ce-portal-7.1.1-ga2

	7z x liferay-ce-portal-tomcat-7.1.1-ga2-20181101125651026.7z

	#
	# Patch portal-kernel.jar.
	#

	cd ../portal-kernel

	ant compile

	cd classes

	zip ../../dist/liferay-ce-portal-7.1.1-ga2/tomcat-9.0.10/lib/ext/portal-kernel.jar com/liferay/portal/kernel/util/ReleaseInfo.class

	cp ../../dist/liferay-ce-portal-7.1.1-ga2/tomcat-9.0.10/lib/ext/portal-kernel.jar ../../dist

	cd ../../dist

	#
	# Patch Marketplace, applying LPS-86917 02838fe9f12a2970edc53c7c0c1ab3597f92d632, 9c71cf682d86f190b2d3c82b409f28dfa1722f5c
	#

	cp "liferay-ce-portal-7.1.1-ga2/osgi/marketplace/Liferay Marketplace - Impl.lpkg" .

	unzip "Liferay Marketplace - Impl.lpkg" com.liferay.marketplace.service-4.0.8.jar

	cd ../modules

	../gradlew :apps:marketplace:marketplace-service:clean :apps:marketplace:marketplace-service:classes

	cd ../dist

	zip -d com.liferay.marketplace.service-4.0.8.jar com/liferay/marketplace/internal/lpkg/deployer/*

	cd ../modules/apps/marketplace/marketplace-service/classes

	zip ../../../../../dist/com.liferay.marketplace.service-4.0.8.jar com/liferay/marketplace/internal/lpkg/deployer/*

	cd ../../../../../dist

	zip "Liferay Marketplace - Impl.lpkg" com.liferay.marketplace.service-4.0.8.jar

	#
	# Patch Frontend JS, applying LPS-83829 4915dec992e53a3ebcafcfa4318f34a22a27b6e1
	#

	cp "liferay-ce-portal-7.1.1-ga2/osgi/marketplace/Liferay CE Foundation - Liferay CE Frontend JS - Impl.lpkg" .

	unzip "Liferay CE Foundation - Liferay CE Frontend JS - Impl.lpkg" com.liferay.frontend.js.web-2.0.24.jar

	cd ../modules/apps/frontend-js/frontend-js-web/src/main/resources

	zip ../../../../../../../dist/com.liferay.frontend.js.web-2.0.24.jar META-INF/resources/liferay/lazy_load.js
	zip ../../../../../../../dist/com.liferay.frontend.js.web-2.0.24.jar META-INF/resources/liferay/liferay.js

	cd ../../../../../../../dist

	zip "Liferay CE Foundation - Liferay CE Frontend JS - Impl.lpkg" com.liferay.frontend.js.web-2.0.24.jar

	#
	# Compress Tomcat.
	#

	cp *.lpkg liferay-ce-portal-7.1.1-ga2/osgi/marketplace

	local current_date=$(date)

	local timestamp=$(date -d "${current_date}" "+%Y%m%d%H%M%S%3N")

	7z a -md1024m liferay-ce-portal-tomcat-7.1.1-ga2-${timestamp}.7z liferay-ce-portal-7.1.1-ga2

	tar -czf liferay-ce-portal-tomcat-7.1.1-ga2-${timestamp}.tar.gz liferay-ce-portal-7.1.1-ga2

	#
	# Decompress Wildfly.
	#

	rm -fr liferay-ce-portal-7.1.1-ga2

	7z x liferay-ce-portal-wildfly-7.1.1-ga2-20181101125651026.7z

	#
	# Compress Wildfly.
	#

	cp portal-kernel.jar liferay-ce-portal-7.1.1-ga2/wildfly-11.0.0/modules/com/liferay/portal/main

	cp *.lpkg liferay-ce-portal-7.1.1-ga2/osgi/marketplace

	7z a -md1024m liferay-ce-portal-wildfly-7.1.1-ga2-${timestamp}.7z liferay-ce-portal-7.1.1-ga2

	tar -czf liferay-ce-portal-wildfly-7.1.1-ga2-${timestamp}.tar.gz liferay-ce-portal-7.1.1-ga2

	cd ..
}

main