# =================
# 1. 빌드 단계 (Builder Stage)
# =================
# Gradle과 JDK 17이 포함된 이미지를 기반으로 빌드 환경을 구성합니다.
FROM gradle:8.5-jdk17 AS builder

# 작업 디렉토리를 생성합니다.
WORKDIR /build

# build.gradle과 settings.gradle을 먼저 복사하여 종속성을 캐싱합니다.
COPY build.gradle settings.gradle /build/
COPY gradle /build/gradle

# 나머지 소스 코드를 복사합니다.
COPY src /build/src

# Gradle을 사용하여 프로젝트를 빌드하고 실행 가능한 jar 파일을 생성합니다.
# '--no-daemon' 옵션은 Docker 환경에서 권장됩니다.
RUN gradle build --no-daemon -x test

# =================
# 2. 실행 단계 (Final Stage)
# =================
# 실제 애플리케이션을 실행할 최소한의 환경을 구성합니다.
# Java 17만 포함된 가벼운 이미지를 사용합니다.
FROM openjdk:17-jdk-slim

# 작업 디렉토리를 생성합니다.
WORKDIR /app

# 빌드 단계에서 생성된 jar 파일을 실행 단계로 복사합니다.
# 경로와 파일 이름은 실제 생성된 jar 파일에 맞게 조정해야 할 수 있습니다.
COPY --from=builder /build/build/libs/Academy_Project-0.0.1-SNAPSHOT.jar ./app.jar

# 컨테이너 외부로 노출할 포트를 지정합니다.
EXPOSE 9090

# 컨테이너가 시작될 때 실행할 명령어를 지정합니다.
# "java -jar app.jar" 명령어로 Spring Boot 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]