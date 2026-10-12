#!/usr/bin/env bash
set -Eeuo pipefail

: "${APP_URL:=http://localhost:8080}"
HEALTH_ENDPOINT="${APP_URL}/actuator/health"

echo "=========================================================="
echo " [Smoke Test] Verifying application readiness: ${HEALTH_ENDPOINT}"
echo "=========================================================="

ATTEMPTS=15
DELAY_SECONDS=3

for ((i=1; i<=ATTEMPTS; i++)); do
    echo "Attempt ${i}/${ATTEMPTS}: Checking health status..."
    RESPONSE=$(curl --silent --show-error --max-time 5 "${HEALTH_ENDPOINT}" || true)
    
    if echo "${RESPONSE}" | grep -q '"status":"UP"'; then
        echo "SUCCESS: WattWise API is UP and healthy!"
        echo "Response payload: ${RESPONSE}"
        exit 0
    fi
    
    echo "Waiting ${DELAY_SECONDS} seconds before next retry..."
    sleep "${DELAY_SECONDS}"
done

echo "ERROR: Application health check timed out after ${ATTEMPTS} attempts."
echo "Last response: ${RESPONSE:-<no response>}"
exit 1
