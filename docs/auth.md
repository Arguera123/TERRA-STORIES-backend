# Autenticación

La API de Terra Stories utiliza **JSON Web Tokens (JWT)** para gestionar la autenticación y autorización de los usuarios de manera segura.

## Obtención del Token JWT

Para consumir los endpoints protegidos, primero debes autenticarte enviando tus credenciales al endpoint de inicio de sesión. Si las credenciales son válidas, el servidor te devolverá un token JWT.

*Consulta la referencia del endpoint [POST /auth/login](api.md#autenticacion-login) en la sección de Endpoints para más detalles sobre cómo solicitar el token.*

## Uso del Token

Una vez que obtengas el JWT, debes incluirlo en el header de todas las subsecuentes peticiones HTTP dirigidas a endpoints protegidos.

El token debe ser enviado usando el esquema `Bearer` dentro del header `Authorization`.

**Formato del Header:**
```http
Authorization: Bearer <tu_token_jwt_aqui>
```

**Ejemplo de Petición cURL con Autenticación:**

```bash
curl -X GET http://localhost:6767/api/v1/reportes \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Accept: application/json"
```

!!! warning "Importante"
    Mantén el token seguro y no lo expongas en el código fuente del lado del cliente si es posible evitarlo. Los tokens expiran después de un tiempo determinado (configurado en el servidor) y devolverán un error `401 Unauthorized` cuando caduquen.
