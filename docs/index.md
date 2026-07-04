# Terra Stories - API de Monitoreo Ambiental Ciudadano

Bienvenido a la documentación oficial de la API del backend de **Terra Stories**. Esta documentación está diseñada para guiar a los desarrolladores frontend y otros consumidores de servicios en la integración con nuestra plataforma.

## Información General

Nuestra API sigue los principios RESTful y se comunica exclusivamente en formato **JSON**.

- **Base URL:** `http://localhost:6767/api/v1`
- **Formato de Intercambio:** `application/json`

Todas las peticiones (a menos que se especifique lo contrario) y respuestas deben usar este formato de intercambio de datos.

## Códigos de Estado HTTP

La API utiliza códigos de estado estándar de HTTP para indicar el éxito o fracaso de una solicitud en particular:

| Código | Descripción | Significado |
| :--- | :--- | :--- |
| **200 OK** | Éxito | La solicitud se completó correctamente y devuelve los datos solicitados. |
| **201 Created** | Creado | La solicitud fue exitosa y como resultado se creó un nuevo recurso (ej. un reporte). |
| **401 Unauthorized** | No Autorizado | El cliente debe autenticarse para obtener la respuesta solicitada (token ausente o inválido). |
| **403 Forbidden** | Prohibido | El cliente no tiene derechos de acceso al contenido; el servidor rechaza dar la respuesta. |
| **404 Not Found** | No Encontrado | El servidor no pudo encontrar el recurso solicitado. |
| **500 Internal Server Error**| Error Interno | El servidor encontró una situación que no sabe cómo manejar. |

---
*Para aprender a autenticarte en nuestra plataforma, visita la sección de [Autenticación](auth.md).*
