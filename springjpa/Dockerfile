FROM amazoncorretto:26.0.0

RUN yum -y install shadow-utils

RUN groupadd app && adduser -g app app

WORKDIR /app
RUN chown app:app /app
USER app

COPY target/springjpa*.jar ./springjpa.jar
COPY src/main/resources/tls/truststore.jks ./tls/truststore.jks


ENTRYPOINT ["java", "-Djavax.net.ssl.trustStore=/app/tls/truststore.jks", "-Djavax.net.ssl.trustStorePassword=", "-jar", "springjpa.jar"]
