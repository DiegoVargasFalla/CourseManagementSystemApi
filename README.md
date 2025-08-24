# 🎓 API de Gestión de Cursos y Estudiantes

Una **API RESTful** desarrollada en **Java 17 con Spring Boot**,
este es un proyecto universitario para la materia practica profesional II que permite la gestión integral de cursos, estudiantes, notas y administradores, con un sistema de autenticación seguro basado en **JWT**.

---

## ✨ Características

- 🔐 **Autenticación JWT** - Sistema seguro de autenticación con tokens
- 👨‍💼 **Gestión de Roles** - Diferentes niveles de acceso (SuperAdmin, Admin)
- 📊 **Base de Datos ORM** - Gestión con Hibernate y Spring Data JPA
- 🏗 **Arquitectura por Capas** - Diseño modular y mantenible
- 📝 **Documentación Interactiva** - API documentada con Swagger/OpenAPI
- 🎯 **Relaciones Avanzadas** - Modelos con relaciones ManyToMany, OneToMany
- 📧 **Registro Seguro** - Sistema de invitación por email para administradores
- ⚙️ **Configuración Flexible** - Gradle como gestor de dependencias

---

## 🛠 Tecnologías Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Security
- JWT (JSON Web Tokens)
- Hibernate ORM
- Spring Data JPA
- Jackson (serialización/deserialización)
- Gradle
- Spring Doc OpenAPI
- Base de Datos: PostgreSQL / MySQL / H2 (para desarrollo)

---

## 📋 Requisitos Previos

- **Java JDK 17** o superior
- **Gradle 7.x** o superior
- **Base de datos** compatible (PostgreSQL, MySQL)
- **Servidor de correo** configurado (para envío de invitaciones)

---
## 📚 Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── ubbaackend/
│   │            ├── config/       # Configuraciones varias
│   │            ├── controller/   # endpoints
│   │            ├── DTO/          # Objetos de transferencia
│   │            ├── enumeration/  # Datos estaticos para uso especifico
│   │            ├── exception/    # Manejo de ecepciones personalisadas
│   │            ├── model/        # Objetos de transferencia
│   │            └── repository/   # Acceso a DB
│   │            └── security/     # Seguridad de la API
│   │            └── service/      # Interfaces metodos de uso en el controller
│   │            └── serviceImpl/  # Implementacion de los servicios
│   └── resources/
│       └── application.properties        # Configuración
└── test/                                 # Pruebas
```

## 🔐 Autenticación y Autorización

La API utiliza **JWT** para autenticación e incluye los siguientes roles:

- **SUPER_ADMIN**: Acceso total al sistema, puede crear otros administradores
- **ADMIN**: Gestión de cursos, estudiantes y notas

---

### 🔑 Flujo de Registro para Administradores

    1. El **SUPER_ADMIN** genera un código de invitación asociado a un email  
    2. El sistema envía automáticamente un email con URL personalizada y código  
    3. El destinatario completa el registro con sus datos personales  
    4. El código tiene tiempo limitado de validez y puede ser revocado  

## 📝 Licencia

Este proyecto está bajo la siguiente licencia [LICENSE](LICENSE).
