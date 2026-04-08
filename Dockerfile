FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src

RUN mvn -B clean package -DskipTests \
 && JAR_FILE=$(find target -maxdepth 1 -name "*.jar" ! -name "*original" | head -n 1) \
 && test -n "$JAR_FILE" \
 && cp "$JAR_FILE" app.jar

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/app.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
