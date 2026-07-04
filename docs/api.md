# Referencia de Endpoints

A continuación se detalla la referencia de los endpoints principales de la API de Terra Stories.

## Resumen de Endpoints

| Método | Endpoint | Descripción | Requiere Autenticación |
| :--- | :--- | :--- | :---: |
| `POST` | `/auth/login` | Inicia sesión y obtiene un token JWT. | No |
| `POST` | `/reportes` | Crea un nuevo reporte ambiental. | Sí |

---

## Detalle de Endpoints

### Autenticación (Login)

Inicia una sesión de usuario y devuelve un token JWT para la autenticación en el resto de la API.

- **Método:** `POST`
- **Ruta:** `/auth/login`
- **Autenticación:** No requerida.

#### Parámetros de la Petición (Body)

| Campo | Tipo | Descripción | Obligatorio |
| :--- | :--- | :--- | :---: |
| `username` | `string` | Nombre de usuario o correo electrónico. | Sí |
| `password` | `string` | Contraseña del usuario. | Sí |

#### Ejemplo de Petición

```bash
curl -X POST http://localhost:6767/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario_ejemplo",
    "password": "mi_password_seguro"
  }'
```

#### Respuesta Esperada (200 OK)

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "usuario_ejemplo",
  "roles": ["ROLE_USER"]
}
```

---

### Creación de Reportes

Permite a los usuarios registrados registrar un nuevo reporte ambiental en la plataforma.

- **Método:** `POST`
- **Ruta:** `/reportes`
- **Autenticación:** Requerida (Bearer Token).

#### Parámetros de la Petición (Body)

| Campo | Tipo | Descripción | Obligatorio |
| :--- | :--- | :--- | :---: |
| `descripcion` | `string` | Descripción detallada de la situación ambiental. | Sí |
| `latitud` | `number` | Coordenada de latitud del reporte. | Sí |
| `longitud` | `number` | Coordenada de longitud del reporte. | Sí |

#### Ejemplo de Petición

```bash
curl -X POST http://localhost:6767/api/v1/reportes \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "descripcion": "Acumulación de basura en el parque central.",
    "latitud": -12.046374,
    "longitud": -77.042793
  }'
```

#### Respuesta Esperada (201 Created)

```json
{
  "id": 105,
  "descripcion": "Acumulación de basura en el parque central.",
  "latitud": -12.046374,
  "longitud": -77.042793,
  "fechaCreacion": "2026-07-04T10:30:00Z",
  "estado": "PENDIENTE",
  "usuarioId": 42
}
```
