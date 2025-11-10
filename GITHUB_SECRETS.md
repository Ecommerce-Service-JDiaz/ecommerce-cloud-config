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

### Docker Hub
- `DOCKERHUB_USERNAME` - Usuario de Docker Hub
- `DOCKERHUB_TOKEN` - Token de acceso de Docker Hub (Access Token, no la contraseña)
  > **Nota:** Puedes generar un Access Token en Docker Hub: Account Settings → Security → New Access Token

## Secrets para Ambiente DEV (develop branch)

### Azure y AKS
- `AZURE_RESOURCE_GROUP_DEV` - Nombre del Resource Group de Azure para dev
- `AKS_CLUSTER_NAME_DEV` - Nombre del cluster AKS para dev

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
- **DOCKERHUB_USERNAME** y **DOCKERHUB_TOKEN** son compartidos para todos los ambientes
- Cada ambiente tiene su propio Resource Group y AKS Cluster, pero comparten las mismas credenciales de Azure y Docker Hub
- Las imágenes Docker se publican en Docker Hub con el formato: `DOCKERHUB_USERNAME/cloud-config:COMMIT_SHA`
- Los secrets son sensibles y no deben compartirse ni exponerse en el código
- Asegúrate de que el Service Principal tenga los permisos necesarios en cada Resource Group y AKS cluster
- El workflow de DEV se activa con la rama `develop` (no `developer`)
- Para Docker Hub, usa un Access Token en lugar de tu contraseña por seguridad

