# Etapa de build
FROM gradle:8.5-jdk17 AS builder

USER root
WORKDIR /app

# Copiamos el proyecto completo
COPY . .

# Construir el .jar sin ejecutar los tests
RUN gradle --no-daemon clean build -x test

# Etapa de ejecución (imagen más liviana)
FROM arm64v8/eclipse-temurin:17-jdk

WORKDIR /app

# Copiar solo el .jar generado
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# Ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]