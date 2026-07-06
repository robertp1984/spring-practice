FROM eclipse-temurin:26-jre-ubi10-minimal AS builder

WORKDIR /builder
COPY target/springjpa*.jar springjpa.jar

RUN java -Djarmode=tools -jar springjpa.jar extract --layers --destination extracted


# Build the actual image using builder from above
FROM eclipse-temurin:26-jre-ubi10-minimal

RUN groupadd app && adduser -g app app

WORKDIR /app
RUN chown app:app /app
USER app

COPY src/main/resources/tls/truststore.jks ./tls/truststore.jks

COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

ENTRYPOINT ["java", "-Djavax.net.ssl.trustStore=/app/tls/truststore.jks", "-Djavax.net.ssl.trustStorePassword=", "-jar", "springjpa.jar"]
