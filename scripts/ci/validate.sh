#!/usr/bin/env bash
set -Eeuo pipefail

echo "=========================================================="
echo " [CI Step] WattWise Build & Verification"
echo "=========================================================="

# Check Maven wrapper or system maven
if [ -f "./mvnw" ]; then
    MAVEN_CMD="./mvnw"
else
    MAVEN_CMD="mvn"
fi

echo "Running clean verify lifecycle with automated test execution..."
${MAVEN_CMD} -B clean verify

echo "Validation completed successfully. Artifact ready in target/."
