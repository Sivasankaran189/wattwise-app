#!/usr/bin/env bash
set -Eeuo pipefail

: "${GITOPS_DIR:?GITOPS_DIR path to wattwise-gitops is required}"
: "${NEW_IMAGE_NAME:?NEW_IMAGE_NAME is required}"
: "${NEW_IMAGE_TAG:?NEW_IMAGE_TAG is required}"
: "${APP_PATH:=${GITOPS_DIR}/apps/wattwise}"

echo "=========================================================="
echo " [GitOps Step] Updating Release Manifests"
echo "=========================================================="
echo "Target Directory: ${APP_PATH}"
echo "Setting Image:    ${NEW_IMAGE_NAME}:${NEW_IMAGE_TAG}"

if [ ! -d "${APP_PATH}" ]; then
    echo "ERROR: Target path does not exist: ${APP_PATH}"
    exit 1
fi

cd "${APP_PATH}"

# Update image using kustomize if available, or update kustomization.yaml directly
if command -v kustomize &> /dev/null; then
    kustomize edit set image "wattwise-api=${NEW_IMAGE_NAME}:${NEW_IMAGE_TAG}"
else
    # Fallback to sed replacement for maximum portability
    sed -i.bak -E "s|(newTag: ).*|\1\"${NEW_IMAGE_TAG}\"|g" kustomization.yaml
    rm -f kustomization.yaml.bak
fi

echo "GitOps manifest updated:"
git diff kustomization.yaml || true

cd "${GITOPS_DIR}"
git add apps/wattwise/kustomization.yaml
git commit -m "ci(gitops): promote image to ${NEW_IMAGE_NAME}:${NEW_IMAGE_TAG} [skip ci]" || echo "No changes to commit"

echo "Ready to push changes to remote GitOps repository."
