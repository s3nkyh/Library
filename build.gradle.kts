import org.apache.hc.core5.io.SocketTimeoutExceptionFactory.create
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.openApiGenerate

plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("nu.studer.jooq") version "9.0"
    id ("org.openapi.generator") version "7.0.1"
}

group = "org"
version = "0.0.1-SNAPSHOT"
description = "Library"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    /**
     * Spring Boot Starters
     */
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")

    /**
     * Database
     */
    implementation("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")
    jooqGenerator("org.postgresql:postgresql")

    /**
     * Utils and Logging
     */
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
    annotationProcessor ("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    /**
     * Testing
     */
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sourceSets {
    main {
        java {
            srcDir("src/main/generator")
            srcDir ("${buildDir}/openapi-generated/src/main/java")
            srcDir ("$buildDir/generated/openapi-client/src/main/java")
        }
    }
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/openapi.yaml".replace("\\", "/"))
    outputDir.set("$buildDir/openapi-generated")
    apiPackage.set("org.library.generated.api")
    modelPackage.set("org.library.generated.model")
    invokerPackage.set("org.library.generated.invoker")

    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "useTags" to "true",
            "java8" to "true",
            "dateLibrary" to "java8",
            "delegatePattern" to "false"
        )
    )
}

openApiGenerate {
    generatorName = "java"
    inputSpec.set("$rootDir/src/main/resources/openapi.yaml".replace("\\", "/"))
    outputDir.set("$buildDir/generated/openapi-client")
    apiPackage.set("org.library.generated.client.api")
    modelPackage.set("org.library.generated.client.model")
    invokerPackage.set("org.library.generated.client.invoker")

    configOptions.set(
        mapOf(
            "library" to "resttemplate",
            "dateLibrary" to "java8",
            "useJakartaEe" to "true"
        )
    )
}

jooq {
    version.set("3.19.26")

    configurations {
        maybeCreate("main").apply {
            generateSchemaSourceOnCompilation.set(false)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN

                jdbc = org.jooq.meta.jaxb.Jdbc().apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/postgres"
                    user = "postgres"
                    password = ""
                }

                generator = org.jooq.meta.jaxb.Generator().apply {
                    name = "org.jooq.codegen.DefaultGenerator"

                    database = org.jooq.meta.jaxb.Database().apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }

                    generate = org.jooq.meta.jaxb.Generate().apply {
                        isDaos = false
                        isPojos = true
                        isRecords = true
                    }

                    target = org.jooq.meta.jaxb.Target().apply {
                        packageName = "com.example.jooq"
                        directory = "src/main/generated"
                    }
                }
            }
        }
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}
