# 🧪 BDD Test Automation – OWASP Juice Shop | Módulo III

<div align="center">

[![CI – BDD Regression](https://github.com/TU_USUARIO/TU_REPO/actions/workflows/ci.yml/badge.svg)](https://github.com/TU_USUARIO/TU_REPO/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-17-blue?logo=java)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apache-maven)](https://maven.apache.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.44-43B02A?logo=selenium)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7-23D96C?logo=cucumber)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.12-FF6F00)](https://testng.org/)

**Curso:** BDD Test Automation &nbsp;|&nbsp; **Trainer:** Luis Ruiz &nbsp;|&nbsp; **Alumno:** Jose Antonio Uriarte  
**Revisor:** [@lruizajax](https://github.com/lruizajax)

</div>

---

## 📋 Tabla de contenidos

1. [Descripción del proyecto](#-descripción-del-proyecto)
2. [Tecnologías utilizadas](#-tecnologías-utilizadas)
3. [Historias de usuario cubiertas](#-historias-de-usuario-cubiertas)
4. [Estructura del proyecto](#-estructura-del-proyecto)
5. [Prerrequisitos e instalación](#-prerrequisitos-e-instalación)
6. [Ejecución de las pruebas](#-ejecución-de-las-pruebas)
7. [Reportes y evidencias](#-reportes-y-evidencias)
8. [GitHub Actions – CI/CD](#-github-actions--cicd)
9. [Resultados obtenidos](#-resultados-obtenidos)
10. [Notas importantes](#-notas-importantes)

---

## 📖 Descripción del proyecto

Framework de automatización de pruebas **BDD (Behavior-Driven Development)** sobre la aplicación web **OWASP Juice Shop**, desarrollado como entregable del Módulo III del curso _BDD Test Automation_.

El framework implementa el patrón **Page Object Model con Page Factory**, ejecución en **paralelo** con `TestNG DataProvider`, generación de **reportes Allure**, captura de **screenshots** en fallos y automatización del pipeline con **GitHub Actions**.

> 🐳 La aplicación bajo prueba (Juice Shop) se ejecuta localmente mediante Docker.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Versión | Rol |
|---|---|---|
| **Java** | 17 | Lenguaje principal |
| **Maven** | 3.9+ | Build tool y gestión de dependencias |
| **Selenium WebDriver** | 4.44.0 | Automatización del navegador |
| **Cucumber** | 7.34.3 | Framework BDD (Gherkin → Steps) |
| **TestNG** | 7.12.0 | Runner de pruebas y paralelismo |
| **WebDriverManager** | 5.9.2 | Gestión automática de ChromeDriver |
| **Allure** | 2.29.1 | Reportes interactivos |
| **Log4j2** | 2.24.1 | Logging estructurado |
| **DataFaker** | 2.5.2 | Generación de datos de prueba |
| **GitHub Actions** | — | CI/CD – pipeline automatizado |

---

## 📚 Historias de usuario cubiertas

| # | Feature | Escenario | Tag | Estado |
|---|---|---|---|---|
| 1 | `register.feature` | Registro exitoso de nuevos usuarios (usuario1) | `@registro` | ✅ |
| 2 | `register.feature` | Registro exitoso de nuevos usuarios (usuario2) | `@registro` | ✅ |
| 3 | `login.feature` | Inicio de sesión con credenciales correctas | `@login` | ✅ |
| 4 | `login.feature` | Inicio de sesión con credenciales incorrectas | `@login` | ✅ |
| 5 | `address.feature` | Agregar dos direcciones de envío | `@direccion` | ✅ |
| 6 | `payment.feature` | Agregar dos métodos de pago | `@pago` | ✅ |
| 7 | `shopping.feature` | Pedido 1 – Compra usando búsqueda de productos | `@cesta` | ✅ |
| 8 | `shopping.feature` | Pedido 2 – Compra de productos aleatorios | `@cesta` | ✅ |
| 9 | `order_history.feature` | Historial y evidencia de los 2 pedidos completados | `@historial` | ✅ |

**Total: 9 escenarios — 9 ✅ pasados / 0 ❌ fallidos**

---

## 🗂️ Estructura del proyecto

```
EXAMEN_BDD_AUTOMATION/
├── .github/
│   └── workflows/
│       └── ci.yml                  ← Pipeline GitHub Actions
├── pom.xml                         ← Dependencias y configuración Maven
├── testng.xml                      ← Suite completa de regresión (9 escenarios)
├── testng2.xml                     ← Suite: Registro + Login
├── testng3.xml                     ← Suite: Dirección + Pago
├── testng4.xml                     ← Suite: Cesta + Historial
├── screenShots/                    ← Screenshots de evidencia (auto-generado)
└── src/
    └── test/
        ├── java/com/juice/
        │   ├── factory/
        │   │   └── DriverFactory.java          ← WebDriver ThreadLocal (paralelo)
        │   ├── pages/                          ← Page Objects con PageFactory
        │   │   ├── BasePage.java               ← Esperas robustas + click con retry
        │   │   ├── HomePage.java
        │   │   ├── RegisterPage.java
        │   │   ├── LoginPage.java
        │   │   ├── AddressPage.java
        │   │   ├── PaymentPage.java
        │   │   ├── ShoppingPage.java
        │   │   ├── SearchResultsPage.java
        │   │   ├── CheckoutPage.java
        │   │   └── OrderHistoryPage.java
        │   ├── runner/                         ← Cucumber Runners (TestNG)
        │   │   ├── TestRunner.java             ← Runner principal (@regression)
        │   │   ├── RegistroLoginRunner.java
        │   │   ├── DireccionPagoRunner.java
        │   │   ├── CestaHistorialRunner.java
        │   │   └── HistorialRunner.java
        │   ├── steps/                          ← Step Definitions por historia
        │   │   ├── RegisterSteps.java
        │   │   ├── LoginSteps.java
        │   │   ├── AddressSteps.java
        │   │   ├── PaymentSteps.java
        │   │   ├── ShoppingSteps.java
        │   │   └── OrderHistorySteps.java
        │   ├── config/
        │   │   └── Hooks.java                  ← Setup/Teardown + Screenshot en fallo
        │   ├── listeners/
        │   │   └── ScreenshotListener.java     ← TestNG ITestListener
        │   ├── log/
        │   │   └── LogManager.java             ← Wrapper Log4j2
        │   └── utils/
        │       ├── ConfigReader.java           ← Lee config.properties
        │       ├── TestDataGenerator.java      ← Datos aleatorios con DataFaker
        │       ├── ScreenshotUtils.java        ← Captura y guarda screenshots
        │       └── TestContext.java            ← Contexto compartido por hilo
        └── resources/
            ├── features/
            │   ├── register.feature
            │   ├── login.feature
            │   ├── address.feature
            │   ├── payment.feature
            │   ├── shopping.feature
            │   └── order_history.feature
            ├── config.properties
            ├── log4j2.properties
            └── allure.properties
```

---

## ⚙️ Prerrequisitos e instalación

### Requisitos mínimos

| Herramienta | Versión mínima | Descarga |
|---|---|---|
| **Java JDK** | 17 | [Adoptium Temurin 17](https://adoptium.net/) |
| **Maven** | 3.9 | [maven.apache.org](https://maven.apache.org/download.cgi) |
| **Google Chrome** | Última | [chrome](https://www.google.com/chrome/) |
| **Docker Desktop** | Última | [docker.com](https://www.docker.com/products/docker-desktop/) |

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/TU_REPO.git
cd TU_REPO
```

> Recuerda invitar a **lruizajax** como colaborador revisor en la configuración del repositorio privado de GitHub.

### 2. Instalar dependencias

```bash
mvn dependency:resolve
```

### 3. Levantar OWASP Juice Shop con Docker

```bash
docker pull bkimminich/juice-shop
docker run --rm -p 3000:3000 bkimminich/juice-shop
```

Verificar que esté accesible en: **http://localhost:3000**

### 4. Configuración (`src/test/resources/config.properties`)

```properties
browser=chrome
headless=false          # cambiar a true para ejecución CI/headless
app.url=http://localhost:3000
timeout.implicit=0
timeout.explicit=30
screenshot.path=screenShots/
```

---

## ▶️ Ejecución de las pruebas

### Suite completa de regresión

```bash
mvn clean test
```

### Modo headless (sin ventana del navegador)

```bash
mvn clean test -Dheadless=true
```

### Suites parciales

```bash
# Registro + Login
mvn test -Dsurefire.suiteXmlFiles=testng2.xml

# Dirección + Pago
mvn test -Dsurefire.suiteXmlFiles=testng3.xml

# Cesta + Historial de pedidos
mvn test -Dsurefire.suiteXmlFiles=testng4.xml
```

### Filtrar por tag de Cucumber

```bash
mvn test '-Dcucumber.filter.tags=@cesta and @regression'
mvn test '-Dcucumber.filter.tags=@login and @regression'
```

### Tabla de suites

| Suite XML | Historias cubiertas | Tags |
|---|---|---|
| `testng.xml` | Regresión completa (9 escenarios) | `@regression` |
| `testng2.xml` | Registro + Inicio de sesión | `@registro`, `@login` |
| `testng3.xml` | Dirección + Método de pago | `@direccion`, `@pago` |
| `testng4.xml` | Cesta de compras + Historial | `@cesta`, `@historial` |

---

## 📊 Reportes y evidencias

### Allure Report (interactivo)

```bash
# 1. Ejecutar las pruebas (genera target/allure-results/)
mvn clean test

# 2. Generar el reporte HTML
mvn allure:report

# 3. Abrir el reporte en el navegador mediante servidor local
mvn allure:serve
```

> ⚠️ El `index.html` de Allure **no funciona** abierto directamente con `file://`.
> Usar siempre `mvn allure:serve` para verlo correctamente.

### Ubicación de artefactos

| Artefacto | Ruta |
|---|---|
| Resultados crudos de Allure | `target/allure-results/` |
| Reporte HTML de Allure | `target/site/allure-maven-plugin/index.html` |
| Reporte HTML de Cucumber | `target/cucumber-reports/cucumber.html` |
| Reporte JSON de Cucumber | `target/cucumber-reports/cucumber.json` |
| Logs de ejecución | `target/logs/` |
| Screenshots de evidencia | `screenShots/` |

### Screenshots

- **Fallos automáticos:** el hook `@After` captura screenshot y la adjunta al reporte Allure.
- **Historial de pedidos:** el escenario `@historial` captura una screenshot por pedido como evidencia explícita.

---

## 🚀 GitHub Actions – CI/CD

El pipeline (`.github/workflows/ci.yml`) se compone de dos jobs:

```
Push / PR ──► Job: regression
                 ├─ Setup JDK 17
                 ├─ Docker: levantar Juice Shop
                 ├─ mvn clean test -Dheadless=true
                 ├─ mvn allure:report
                 ├─ Upload artifacts (allure-report, screenshots)
                 └─ Docker: detener Juice Shop

             Job: publish-report  (solo push a main)
                 ├─ Download allure-report artifact
                 └─ Deploy en GitHub Pages (rama gh-pages)
```

### Disparadores configurados

| Evento | Rama | Job ejecutado |
|---|---|---|
| `push` | `main`, `master`, `develop` | `regression` + `publish-report` |
| `pull_request` | `main`, `master` | `regression` |
| `workflow_dispatch` | Cualquiera (manual) | `regression` |

### Reporte publicado en GitHub Pages

Tras cada push a `main`, el reporte Allure se publica automáticamente en:
```
https://TU_USUARIO.github.io/TU_REPO/
```

> Reemplaza `TU_USUARIO` y `TU_REPO` con los valores reales de tu repositorio en GitHub.

---

## ✅ Resultados obtenidos

> Última ejecución validada: **04/07/2026** — `testng.xml` (suite de regresión completa)

| Métrica | Valor |
|---|---|
| **Total de escenarios** | 9 |
| **✅ Pasados** | 9 |
| **❌ Fallidos** | 0 |
| **⚠️ Rotos** | 0 |
| **⏭️ Omitidos** | 0 |
| **Tiempo aproximado** | ~55 segundos |
| **Modo de ejecución** | Paralelo (3 threads) |

```
✅ Registro exitoso de nuevos usuarios (usuario1)
✅ Registro exitoso de nuevos usuarios (usuario2)
✅ Inicio de sesión con credenciales correctas
✅ Inicio de sesión con credenciales incorrectas
✅ Agregar dos direcciones de envío
✅ Agregar dos métodos de pago
✅ Pedido 1 – Compra usando búsqueda (manzana, banana, camiseta)
✅ Pedido 2 – Compra de 2 productos aleatorios del catálogo
✅ Historial y evidencia de los 2 pedidos completados
```

---

## 📝 Notas importantes

### 📌 Decisiones tecnicas (detalle centralizado)

Para mantener el codigo con comentarios cortos, el detalle tecnico queda aqui:

1. **Paralelismo:** `DriverFactory` crea un `WebDriver` por hilo con `ThreadLocal`. `TestContext` guarda datos por escenario (email/password/orderIds) y se limpia en `Hooks`.
2. **Estabilidad UI:** `BasePage` aplica esperas explicitas, reintentos en `click()` y `type()`, y fallback por JavaScript para overlays/transiciones de Angular.
3. **Busqueda de productos:** `HomePage.searchProduct()` abre el buscador solo si hace falta y escribe usando locator fresco para evitar errores de interactuabilidad.
4. **Checkout:** `CheckoutPage` maneja variaciones de UI (filas y radios) para direccion/metodo de pago y confirma pedido al final.
5. **Historial de pedidos:** `OrderHistorySteps` valida historial usando IDs reales guardados en `TestContext`; `OrderHistoryPage` espera y busca pedidos visibles por ID.
6. **Datos de prueba:** `TestDataGenerator` crea datos aleatorios para re-ejecuciones sin colisiones de usuarios.
7. **Evidencia:** `Hooks` + `ScreenshotUtils` capturan screenshot al fallar y lo adjuntan a Allure, ademas de guardarlo en `screenShots/`.
8. **Suites:** `TestRunner` corre regresion completa por `@regression`; runners parciales ejecutan grupos por tags para debug rapido.

### Búsqueda de productos

El buscador de Juice Shop indexa el nombre **interno en inglés**. El framework traduce automáticamente:

| Término (español) | Búsqueda interna |
|---|---|
| manzana | apple |
| banana | banana |
| camiseta | shirt |

### Estabilidad en ejecución paralela

| Estrategia | Dónde se aplica |
|---|---|
| `ThreadLocal<WebDriver>` — un driver por hilo | `DriverFactory` |
| `click()` con 3 reintentos + fallback JS | `BasePage` |
| Espera de overlay Angular (`cdk-overlay-backdrop`) | `AddressPage.addAddress()` |
| Refresh + retry si checkout está deshabilitado | `ShoppingPage.goToCheckout()` |
| Timeout explícito 30s (sin timeout implícito) | `BasePage` / `config.properties` |

### Advertencia CDP (informativa, no afecta pruebas)

```
WARNING: Unable to find an exact match for CDP version 149, returning closest: 148
```

Se resolverá con la próxima actualización de Selenium que soporte Chrome 149.

---

<div align="center">

**Curso BDD Test Automation** &nbsp;|&nbsp; Trainer: **Luis Ruiz** &nbsp;|&nbsp; Alumno: **Jose Antonio Uriarte**

</div>
