plugins {
	java
	idea
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}
println("This is executed during the configuration phase.")

group = "com.chatApp"
version = "0.0.1-SNAPSHOT"

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
	implementation("org.springframework.boot:spring-boot-starter-web:3.3.3")
	implementation("org.springframework.boot:spring-boot-starter-websocket:3.3.3")
	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.3")
	implementation(platform("software.amazon.awssdk:bom:2.28.4"))
    implementation("software.amazon.awssdk:dynamodb-enhanced")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0") 
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
