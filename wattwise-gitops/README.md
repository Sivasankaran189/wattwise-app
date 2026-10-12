# WattWise GitOps: Kubernetes Delivery & Infrastructure Repository

This repository serves as the single source of truth for continuous deployment and cluster state managed by **Argo CD**.

---

## 📁 Repository Structure

```
wattwise-gitops/
│
├── apps/
│   └── wattwise/
│       ├── deployment.yaml       # WattWise API pod replica specification
│       ├── service.yaml          # ClusterIP service on port 8080
│       ├── configmap.yaml        # Utility tariffs & baseline thresholds
│       ├── secret-reference.yaml # Database credential references
│       ├── ingress.yaml          # External routing definitions
│       └── kustomization.yaml    # Declarative image version managed by CI
│
├── database/
│   ├── statefulset.yaml          # Containerized PostgreSQL 16
│   ├── service.yaml              # Internal DB service (wattwise-postgres:5432)
│   └── pvc.yaml                  # Persistent volume claim for DB durability
│
├── argocd/
│   └── application.yaml          # Argo CD custom resource for automated sync
│
└── monitoring/
    ├── values.yaml               # Helm values for kube-prometheus-stack
    └── servicemonitor.yaml       # Actuator /actuator/prometheus scraper
```

---

## 🚀 Manual Deployment (Without Argo CD)
```bash
# 1. Create namespace
kubectl create namespace wattwise

# 2. Deploy database
kubectl apply -f database/ -n wattwise

# 3. Deploy WattWise application
kubectl apply -k apps/wattwise/ -n wattwise
```
