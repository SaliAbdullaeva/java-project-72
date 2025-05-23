# Используем официальный образ Gradle с JDK 17
FROM gradle:8.2-jdk17 AS build


# Копируем всё в контейнер
COPY ./app /home/gradle/project
WORKDIR /home/gradle/project

# Собираем приложение
RUN gradle installDist

# Создаём финальный образ
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копируем собранное приложение
COPY --from=build /home/gradle/project/build/install/app /app

# Указываем команду запуска
CMD ["./bin/app"]
