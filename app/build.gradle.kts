import org.gradle.api.tasks.testing.logging.TestExceptionFormat  // Форматы вывода исключений для логов тестов
import org.gradle.api.tasks.testing.logging.TestLogEvent         // Типы событий, которые логируются в тестах

plugins {
    application                                              // Плагин для запуска Java-приложений
    id("io.freefair.lombok") version "8.13.1"                // Плагин для автоматического подключения Lombok
    jacoco                                                   // Плагин для генерации отчётов покрытия тестов
    id("com.github.johnrengelman.shadow") version "8.1.1"   // Чтобы объединить все классы, ресурсы и зависимости проекта в один выходной Jar-архив
}

application {
    mainClass.set("hexlet.code.App")                         // Точка входа в приложение (класс с main)
}

group = "hexlet.code"                                        // Группа (неймспейс) проекта
version = "1.0-SNAPSHOT"                                     // Версия проекта (SNAPSHOT — рабочая, не финальная)

repositories {
    mavenCentral()                                           // Репозиторий для зависимостей
}


dependencies {
    implementation("io.javalin:javalin:6.6.0")                 // Основной веб-фреймворк Javalin
    implementation("org.slf4j:slf4j-simple:2.0.16")            // Логгер SLF4J с простой реализацией
    implementation("org.slf4j:slf4j-api:2.0.16")               // API для логирования SLF4J
    implementation("io.javalin:javalin-rendering:6.4.0")       // Поддержка шаблонов в Javalin
    implementation("gg.jte:jte:3.1.16")                        // Движок шаблонов JTE
    testImplementation(platform("org.junit:junit-bom:5.11.4")) // Управление версиями JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter")      // JUnit Jupiter для тестов
    implementation("org.projectlombok:lombok:1.18.36")         // Lombok — чтобы писать меньше шаблонного кода
    implementation("com.h2database:h2:2.3.232")                // Подключаемся к бд H2
    implementation("com.zaxxer:HikariCP:6.3.0")                // Настройка пула соединений

}

jacoco {
    toolVersion = "0.8.10" // Версия JaCoCo для генерации отчётов покрытия
}

tasks.test {
    useJUnitPlatform()                                                                        // Используем JUnit 5 для запуска тестов
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL                                            // Полные стеки ошибок в логах тестов
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED) // Логирование ключевых событий
        showStandardStreams = true                                                            // Показывать вывод System.out/err во время тестов
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)                                                         // Отчёт зависит от выполнения тестов
    reports {
        xml.required.set(true)                                                    // Генерировать XML отчёт (для CI)
        html.required.set(true)                                                   // Генерировать HTML отчёт (для визуального просмотра)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))    // Путь для HTML отчёта — удобно открыть в браузере
        csv.required.set(false)                                                   // CSV отчёт обычно не нужен
    }
}
