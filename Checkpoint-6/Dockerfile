# Estágio de Build (usando multi-stage build)
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
# Copia o pom.xml e faz o download das dependências (melhora o cache)
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia o código fonte
COPY src ./src
# Empacota a aplicação como um JAR executável
RUN mvn clean package -DskipTests

# Estágio de Execução
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# O JAR gerado no estágio de build é copiado
COPY --from=build /app/target/checkpoint_6-0.0.1-SNAPSHOT.jar app.jar
# A aplicação roda com o profile 'prod' para usar o PostgreSQL do Docker Compose
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]