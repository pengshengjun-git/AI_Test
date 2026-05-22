FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build
RUN apk add --no-cache curl tar bash \
    && curl -fsSL "https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz" \
       | tar -xz -C /opt \
    && ln -sf /opt/apache-maven-3.9.9/bin/mvn /usr/local/bin/mvn
COPY services/maven/settings.xml /root/.m2/settings.xml
COPY apps/backend /build
RUN mvn clean package -DskipTests -pl testcase-service -am -q

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /build/testcase-service/target/testcase-service-1.0.0.jar app.jar
EXPOSE 8004
ENTRYPOINT ["java", "-jar", "app.jar"]
