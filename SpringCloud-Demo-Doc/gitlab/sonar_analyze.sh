#!/bin/bash

mvn --batch-mode sonar:sonar \
    -Dsonar.host.url=http://ip:9000/sonarqube \
    -Dsonar.login=admin \
    -Dsonar.password=admin \
    -Dsonar.issuesReport.html.enable=true \
    -Dsonar.analysis.mode=preview \
    -Dsonar.preview.excludePlugins=issueassign,scmstats

if [ $? -eq 0 ]; then
    echo "sonarqube code-analyze over."
fi
