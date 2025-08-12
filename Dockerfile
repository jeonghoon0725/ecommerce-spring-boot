# 1단계: 빌드 환경
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# 캐시 최적화를 위해 Gradle 관련 파일 먼저 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# gradlew에 실행 권한을 부여
RUN chmod +x gradlew

# 의존성 캐시 다운로드
RUN ./gradlew dependencies --no-daemon

# 전체 소스 복사 후 빌드
COPY . .

# 다시 gradlew에 실행 권한 부여
RUN chmod +x gradlew

# Spring Boot JAR 빌드 (테스트는 생략)
RUN ./gradlew clean bootJar -x test --no-daemon

# 2단계: 실행 환경
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 실행 시 사용될 프로파일 지정 (기본값: dev)
ARG PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 오픈 (필요 시 수정)
EXPOSE 8080

# 앱 실행
ENTRYPOINT ["sh", "-c", "java -jar -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} app.jar"]


#dev
#docker build --build-arg PROFILE=dev -t spring-docker:dev .
#docker run --name spring-docker-dev -e SPRING_PROFILES_ACTIVE=dev -p 8000:8080 -d spring-docker:dev

#prod
#docker build --build-arg PROFILE=prod -t spring-docker:prod .
#docker run --name spring-docker-prod -e SPRING_PROFILES_ACTIVE=prod -p 8000:8080 -d spring-docker:prod