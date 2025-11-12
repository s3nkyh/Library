FROM gradle:8.4-jdk17 as builder
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .

RUN gradle --no-daemon build -x test

FROM eclipse-temurin:17-jre-jammy
RUN useradd --no-create-home --uid 1000 appuser
WORKDIR /app

COPY --from=builder /home/gradle/project/build/libs/*.jar ./app.jar
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080
USER appuser
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]