#!/bin/bash

cd $(dirname "${0}")

function main {
	for dir in "./"*
	do
		if [ ${dir} = "./liferay-sample-workspace" ] ||
		   [ -f ${dir} ]
		then
			continue
		fi

		rsync \
			-a --delete \
			--exclude "client-extensions" \
			--exclude "modules" \
			--exclude "node_modules" \
			--exclude "node_modules_cache" \
			--exclude "poshi/poshi-ext.properties" \
			--exclude "poshi/tests/**.testcase" \
			--exclude "themes" \
			liferay-sample-workspace/ ${dir}
	done
}

main "${@}"