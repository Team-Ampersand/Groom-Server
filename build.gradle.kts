plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ampersand"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

configurations.named("compileOnly") {
	extendsFrom(configurations.getByName("annotationProcessor"))
}

repositories {
	mavenCentral()
	maven { url = uri("https://jitpack.io") }
}

dependencies {
	/* Spring Boot Starter */
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	/** AOP */
	implementation("org.aspectj:aspectjweaver:1.9.19")

	/** Lombok */
	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	/** JWT */
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")

	/** Database */
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	runtimeOnly("com.mysql:mysql-connector-j:9.2.0")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.5.1")

	/** QueryDSL */
	implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
	annotationProcessor("com.querydsl:querydsl-apt:5.1.0:jakarta")

	/** Jakarta */
	implementation("jakarta.persistence:jakarta.persistence-api")
	implementation("jakarta.transaction:jakarta.transaction-api")
	implementation("jakarta.validation:jakarta.validation-api")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")

	/** Test */
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.mockito:mockito-core:5.10.0")
	testImplementation("org.assertj:assertj-core:3.25.3")

	/** Monitoring */
	implementation("io.micrometer:micrometer-registry-prometheus:1.14.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}