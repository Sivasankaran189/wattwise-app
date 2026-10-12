#!/usr/bin/env bash
set -Eeuo pipefail

: "${IMAGE_NAME:?IMAGE_NAME is required (e.g. docker.io/username/wattwise-api)}"
: "${IMAGE_TAG:?IMAGE_TAG is required (e.g. git commit sha or semver v1.0.0)}"

echo "=========================================================="
echo " [CI Step] Building Docker Image: ${IMAGE_NAME}:${IMAGE_TAG}"
echo "=========================================================="

docker build \
  --pull \
  -t "${IMAGE_NAME}:${IMAGE_TAG}" \
  -t "${IMAGE_NAME}:latest" \
  .

echo "Docker image built and tagged successfully:"
echo "  -> ${IMAGE_NAME}:${IMAGE_TAG}"
echo "  -> ${IMAGE_NAME}:latest"
