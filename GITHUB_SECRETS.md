# GitHub Secrets Requeridos

Este documento lista todos los secrets que deben configurarse en GitHub para el despliegue automático en Kubernetes.

## Secrets en GitHub (Solo uno requerido)

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
  > **Nota:** Este secret es necesario para autenticarse con Azure y acceder al Key Vault.

---

## Secrets en Azure Key Vault

Todos los demás secrets están almacenados en **Azure Key Vault** (`ecommercekv8486`) en el resource group `ecommerce-rg-global`.

### Secrets Compartidos (para todos los ambientes)

- `DOCKERHUB-USERNAME` - Usuario de Docker Hub
- `DOCKERHUB-TOKEN` - Token de acceso de Docker Hub (Access Token, no la contraseña)
- `SPRING-CLOUD-CONFIG-SERVER-GIT-URI` - URI del repositorio Git del config server (compartido para todos los ambientes)
  > **Nota:** El repositorio Git es público, por lo que no se requieren credenciales de autenticación

### Secrets Opcionales (con valores por defecto)

- `SPRING-CLOUD-CONFIG-SERVER-GIT-DEFAULT-LABEL` - Rama por defecto del repositorio Git (default: `main`)
- `EUREKA-CLIENT-SERVICE-URL-DEFAULTZONE` - URL de Eureka (default: `http://service-discovery:8761/eureka/`)
- `EUREKA-INSTANCE-HOSTNAME` - Hostname de la instancia en Eureka (default: `cloud-config-service`)

### Secrets para Ambiente DEV (develop branch)

- `AZURE-RESOURCE-GROUP-DEV` - Nombre del Resource Group de Azure para dev
- `AKS-CLUSTER-NAME-DEV` - Nombre del cluster AKS para dev
- `DEV-SPRING-ZIPKIN-BASE-URL` - URL base de Zipkin para tracing (opcional)

### Secrets para Ambiente STAGE (stage branch)

- `AZURE-RESOURCE-GROUP-STAGE` - Nombre del Resource Group de Azure para stage
- `AKS-CLUSTER-NAME-STAGE` - Nombre del cluster AKS para stage
- `STAGE-SPRING-ZIPKIN-BASE-URL` - URL base de Zipkin para tracing (opcional)

### Secrets para Ambiente PROD (main branch)

- `AZURE-RESOURCE-GROUP-PROD` - Nombre del Resource Group de Azure para prod
- `AKS-CLUSTER-NAME-PROD` - Nombre del cluster AKS para prod
- `PROD-SPRING-ZIPKIN-BASE-URL` - URL base de Zipkin para tracing (opcional)

---

## Cómo Configurar los Secrets

### En GitHub

1. Ve a tu repositorio en GitHub
2. Navega a **Settings** → **Secrets and variables** → **Actions**
3. Haz clic en **New repository secret**
4. Agrega `AZURE_CREDENTIALS` con el JSON completo del Service Principal de Azure

### En Azure Key Vault

Los secrets deben estar almacenados en el Key Vault `ecommercekv8486` en el resource group `ecommerce-rg-global`.

Para agregar un secret al Key Vault:

```bash
az keyvault secret set \
  --vault-name ecommercekv8486 \
  --name SECRET-NAME \
  --value "secret-value"
```

Ejemplo:

```bash
az keyvault secret set \
  --vault-name ecommercekv8486 \
  --name DOCKERHUB-USERNAME \
  --value "tu-usuario-dockerhub"
```

---

## Notas Importantes

- **AZURE_CREDENTIALS** es el único secret que debe estar en GitHub. Todos los demás están en Azure Key Vault.
- El Service Principal usado en `AZURE_CREDENTIALS` debe tener permisos para leer secrets del Key Vault `ecommercekv8486`.
- Los nombres de los secrets en Key Vault usan guiones (`-`) en lugar de guiones bajos (`_`).
- Las imágenes Docker se publican en Docker Hub con el formato: `DOCKERHUB_USERNAME/cloud-config:COMMIT_SHA`
- Los secrets son sensibles y no deben compartirse ni exponerse en el código
- Asegúrate de que el Service Principal tenga los permisos necesarios en cada Resource Group y AKS cluster
- El workflow de DEV se activa con la rama `develop` (no `developer`)
- Para Docker Hub, usa un Access Token en lugar de tu contraseña por seguridad

## Seguridad en los Pipelines

Los workflows implementan las siguientes medidas de seguridad para proteger los valores sensibles:

1. **Enmascaramiento de valores sensibles**: Los valores de `DOCKERHUB_TOKEN`, `DOCKERHUB_USERNAME`, `SPRING_CLOUD_CONFIG_SERVER_GIT_URI` y `SPRING_ZIPKIN_BASE_URL` se enmascaran automáticamente en los logs usando `::add-mask::`.

2. **Desactivación del modo debug**: Se usa `set +x` al inicio del script para evitar que los comandos se impriman en los logs, lo que podría exponer valores sensibles.

3. **Escritura segura de variables**: Las variables de entorno se escriben usando un bloque `{}` para evitar que se expongan en los logs durante la asignación.

4. **Sin exposición en comandos**: Los valores sensibles nunca se pasan directamente como argumentos de línea de comandos que puedan aparecer en los logs.

Estas medidas aseguran que los valores sensibles no aparezcan en los logs de GitHub Actions, manteniendo la privacidad y seguridad de la información confidencial.
