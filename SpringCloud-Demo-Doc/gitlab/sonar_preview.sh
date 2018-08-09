#!/bin/bash

mvn --batch-mode verify sonar:sonar \
    -Dsonar.host.url=http://ip:9000/sonarqube \
    -Dsonar.login=admin \
    -Dsonar.password=admin \
    -Dsonar.analysis.mode=preview \
    -Dsonar.gitlab.project_id=$CI_PROJECT_ID \
    -Dsonar.gitlab.commit_sha=$CI_COMMIT_SHA \
    -Dsonar.gitlab.ref_name=$CI_COMMIT_REF_NAME \
    -Dsonar.gitlab.url=http://39.108.36.134
    -Dsonar.gitlab.user_token=9sxq9ZutmrQmf4EKV-wW

if [ $? -eq 0 ]; then
    echo "sonarqube code-analyze-preview over."
fi
