#!/bin/bash

function main {
	send_slack_message "Hello!"
}

function send_slack_message() {
	local slack_message=${1}

	echo "${slack_message}"

	if [ -z "${LIFERAY_LEARN_ETC_CRON_SLACK_ENDPOINT}" ]
	then
		return
	fi

	local log_url="https://console.${LCP_INFRASTRUCTURE_DOMAIN}/projects/${LCP_PROJECT_ID}/services/${LCP_SERVICE_ID}/logs?instanceId=${HOSTNAME}&logServiceId=${LCP_SERVICE_ID}"

	local text="$(date) *${LCP_PROJECT_ID}*->*${LCP_SERVICE_ID}* <${log_url}|${HOSTNAME}> \n>${slack_message}"

	curl \
		-X POST \
		-d "payload={\"channel\": '${LIFERAY_LEARN_ETC_CRON_SLACK_CHANNEL}\", \"icon_emoji\": \":robot_face:\", \"text\": '${text}', \"username\": \"devopsbot\"}" ${LIFERAY_LEARN_ETC_CRON_SLACK_ENDPOINT}
}

main "${@}"