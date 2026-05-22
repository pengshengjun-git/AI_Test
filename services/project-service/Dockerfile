FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build
RUN apk add --no-cache curl tar bash \
    && curl -fsSL "https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz" \
       | tar -xz -C /opt \
    && ln -sf /opt/apache-maven-3.9.9/bin/mvn /usr/local/bin/mvn
COPY services/maven/settings.xml /root/.m2/settings.xml
COPY apps/backend /build
RUN mvn clean package -DskipTests -pl project-service -am -q

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /build/project-service/target/project-service-1.0.0.jar app.jar
ENV LANG=en_US.UTF-8
ENV LC_ALL=en_US.UTF-8
EXPOSE 8003
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dsun.jnu.encoding=UTF-8", "-jar", "app.jar"]
