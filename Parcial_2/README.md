# 🪙 Service-Currency-Converter | Spring Boot REST Microservice

Microservicio backend desarrollado en Java 17 y Spring Boot 3.2.5 para la sincronización, persistencia y conversión de cotizaciones de divisa en tiempo real. 

Este proyecto nace como un **proceso de refactorización arquitectónica**: se migró una aplicación monolítica de escritorio basada en Java Swing y scraping frágil con Jsoup hacia una **API REST desacoplada, reactiva y autónoma**.

---

## 🛠️ Tech Stack & Herramientas

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.2.5
* **Persistencia:** Spring Data JPA + Base de Datos H2 (in-memory)
* **Consumo de API:** `RestTemplate` + `@Scheduled` (Tareas en segundo plano)
* **Pruebas Automatizadas:** JUnit 5 + Mockito
* **Contenedor:** Docker

---

## 📐 Arquitectura del Sistema

El proyecto implementa una arquitectura limpia organizada en capas:

* `controller`: Exposición de endpoints REST y gestión de respuestas HTTP.
* `persistencia`: Repositorios JPA y cliente externo `DolarApiClient`.
* `modelo`: Entidades mapeadas para la base de datos relacional.
* `exception`: Manejo global de excepciones mediante `@RestControllerAdvice`.

---

## 🚀 Capacidades Destacadas

1. **Sincronización Autónoma (`@Scheduled`):** Un proceso en segundo plano consume `dolarapi.com` al iniciar la app y refresca las cotizaciones en la BBDD cada 10 minutos.
2. **Conversor de Divisa (`/api/monedas/convertir`):** Endpoint que calcula conversiones dinámicas según el tipo de dólar y la operación (compra/venta).
3. **Manejo Robusto de Errores:** Intercepción de parámetros inválidos y errores de negocio devolviendo objetos `ErrorResponse` estandarizados (`400 Bad Request`).
4. **Pruebas Unitarias:** Cobertura de lógica de negocio y controladores mediante dobles de prueba (*mocks*).

---

## 📌 Endpoints Principales

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/monedas` | Obtiene el listado completo de cotizaciones persistidas. |
| `GET` | `/api/monedas/convertir?monto=150000&tipo=blue&operacion=venta` | Realiza el cálculo de conversión de divisas. |
| `POST` | `/api/monedas/sincronizar` | Permite la sincronización manual bajo demanda. |

---

## 🐳 Ejecución con Docker

1. Generar el paquete de la aplicación:
   ```bash
   mvn clean package -DskipTests
