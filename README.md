# Vvps
A cloud-based Virtual Power Plant (VPP) system that aggregates distributed, small-scale energy sources into a unified provider for grid-level power. Enables efficient energy distribution and allows producers to sell power to utilities. Supports solar, wind, storage, and other resources.


# ⚡ Virtual Power Plant (Battery API) - Spring Boot Application

A Spring Boot REST API for managing and querying batteries in a virtual power plant system. It supports concurrent battery registration, efficient data queries using Java Streams, and integration with a PostgreSQL database hosted on [Render](https://render.com/).

---

## 🚀 Features

### ✅ Core Features (as per assignment):
- REST API with Spring Boot
- Accepts and saves battery data: `name`, `postcode`, and `watt capacity`
- Fetches batteries within a given postcode range
- Returns:
  - Alphabetically sorted battery names
  - Total watt capacity
  - Average watt capacity
- Uses Java Streams for sorting and statistical calculations
- Unit test coverage ≥ 70%

### ✨ Additional Enhancements:
- ✅ **PostgreSQL integration** (hosted on Render)
- ✅ **Asynchronous processing** with `@Async`
- ✅ **Robust logging** using SLF4J
- ✅ **Integration tests** using **Testcontainers**
- ✅ **Handles high-volume concurrent battery registrations**

---

## 🔧 Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Web
- Spring Data JPA
- PostgreSQL (Render-hosted)
- Lombok
- JUnit 5 + AssertJ
- Testcontainers
- SLF4J + Logback

---

## 🏗️ Architecture Overview

```plaintext
Controller → Service → Mapper → Repository → PostgreSQL
