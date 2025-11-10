# GitHub Secrets Requeridos

Este documento lista todos los secrets que deben configurarse en GitHub para el despliegue automático en Kubernetes.

## Secrets Compartidos (para todos los ambientes)

### Azure
- `AZURE_CREDENTIALS` - Credenciales de Azure en formato JSON (Service Principal)
  ```json
  {
    "clientId": "...",
    "clientSecret": "...",
    "subscriptionId": "...",
    "tenantId": "..."
  }
  ```

## Secrets para Ambiente DEV (develop branch)

### Azure y AKS
- `AZURE_RESOURCE_GROUP_DEV` - Nombre del Resource Group de Azure para dev
- `AKS_CLUSTER_NAME_DEV` - Nombre del cluster AKS para dev

### Azure Container Registry (ACR)
- `DEV_ACR_NAME` - Nombre del Azure Container Registry (sin .azurecr.io)
- `DEV_ACR_USERNAME` - Usuario del ACR
- `DEV_ACR_PASSWORD` - Contraseña del ACR

### Configuración de la Aplicación
- `DEV_SPRING_ZIPKIN_BASE_URL` - URL base de Zipkin para tracing
- `DEV_SPRING_CLOUD_CONFIG_SERVER_GIT_URI` - URI del repositorio Git del config server
- `DEV_GIT_USERNAME` - Usuario para acceder al repositorio Git del config server
- `DEV_GIT_PASSWORD` - Contraseña/token para acceder al repositorio Git del config server

---

## Secrets para Ambiente STAGE (stage branch)

### Azure y AKS
- `AZURE_RESOURCE_GROUP_STAGE` - Nombre del Resource Group de Azure para stage
- `AKS_CLUSTER_NAME_STAGE` - Nombre del cluster AKS para stage

### Azure Container Registry (ACR)
- `STAGE_ACR_NAME` - Nombre del ACR para stage
- `STAGE_ACR_USERNAME` - Usuario del ACR
- `STAGE_ACR_PASSWORD` - Contraseña del ACR

### Configuración de la Aplicación
- `STAGE_SPRING_ZIPKIN_BASE_URL` - URL base de Zipkin para tracing
- `STAGE_SPRING_CLOUD_CONFIG_SERVER_GIT_URI` - URI del repositorio Git del config server
- `STAGE_GIT_USERNAME` - Usuario para acceder al repositorio Git del config server
- `STAGE_GIT_PASSWORD` - Contraseña/token para acceder al repositorio Git del config server

---

## Secrets para Ambiente PROD (main branch)

### Azure y AKS
- `AZURE_RESOURCE_GROUP_PROD` - Nombre del Resource Group de Azure para prod
- `AKS_CLUSTER_NAME_PROD` - Nombre del cluster AKS para prod

### Azure Container Registry (ACR)
- `PROD_ACR_NAME` - Nombre del Azure Container Registry (sin .azurecr.io)
- `PROD_ACR_USERNAME` - Usuario del ACR
- `PROD_ACR_PASSWORD` - Contraseña del ACR

### Configuración de la Aplicación
- `PROD_SPRING_ZIPKIN_BASE_URL` - URL base de Zipkin para tracing
- `PROD_SPRING_CLOUD_CONFIG_SERVER_GIT_URI` - URI del repositorio Git del config server
- `PROD_GIT_USERNAME` - Usuario para acceder al repositorio Git del config server
- `PROD_GIT_PASSWORD` - Contraseña/token para acceder al repositorio Git del config server

---

## Cómo Configurar los Secrets en GitHub

1. Ve a tu repositorio en GitHub
2. Navega a **Settings** → **Secrets and variables** → **Actions**
3. Haz clic en **New repository secret**
4. Agrega cada secret con su nombre y valor correspondiente

## Notas Importantes

- **AZURE_CREDENTIALS** es compartido para todos los ambientes y debe ser el JSON completo del Service Principal de Azure
- Cada ambiente tiene su propio Resource Group y AKS Cluster, pero comparten las mismas credenciales de Azure
- Los secrets son sensibles y no deben compartirse ni exponerse en el código
- Asegúrate de que el Service Principal tenga los permisos necesarios en cada Resource Group y AKS cluster
- El workflow de DEV se activa con la rama `develop` (no `developer`)

