import org.gradle.api.tasks.testing.logging.TestExceptionFormat  // Форматы вывода исключений для логов тестов
import org.gradle.api.tasks.testing.logging.TestLogEvent         // Типы событий, которые логируются в тестах

plugins {
    application                                              // Плагин для запуска Java-приложений
    id("io.freefair.lombok") version "8.13.1"                // Плагин для автоматического подключения Lombok
}

application {
    mainClass.set("hexlet.code.App")                         // Точка входа в приложение (класс с main)
}

group = "hexlet.code"                                        // Группа (неймспейс) проекта
version = "1.0-SNAPSHOT"                                     // Версия проекта (SNAPSHOT — рабочая, не финальная)

repositories {
    mavenCentral()                                           // Репозиторий, откуда подтягиваются зависимости (Maven Central)
}

dependencies {
    implementation("io.javalin:javalin:6.4.0")                 // Основная библиотека Javalin (веб-фреймворк)
    implementation("org.slf4j:slf4j-simple:2.0.16")            // Логгер SLF4J с простой реализацией для вывода логов
    implementation("org.slf4j:slf4j-api:2.0.16")               // API для логирования SLF4J
    implementation("io.javalin:javalin-rendering:6.4.0")       // Поддержка шаблонов и рендеринга в Javalin
    implementation("gg.jte:jte:3.1.16")                        // Шаблонный движок JTE (для HTML и т.п.)
    testImplementation(platform("org.junit:junit-bom:5.11.4")) // Платформа управления версиями JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter")      // Библиотека JUnit Jupiter для написания тестов
    implementation("org.projectlombok:lombok:1.18.36")         // Lombok — чтобы не писать много шаблонного кода (геттеры, сеттеры)
}

tasks.test {
    useJUnitPlatform()                                                                        // Использовать JUnit 5 для запуска тестов
    // https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL                                            // Показывать полные стеки ошибок в логах тестов
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED) // Логировать все ключевые события тестов
        // showStackTraces = true                                                             // (опционально) показывать стек трейсы
        // showCauses = true                                                                  // (опционально) показывать причины ошибок
        showStandardStreams = true                                                            // Показывать вывод System.out/err во время тестов
    }
}
