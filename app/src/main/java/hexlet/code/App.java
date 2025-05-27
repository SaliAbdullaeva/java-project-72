package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.database.DatabaseSetup;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j // Логгер от Lombok, создаёт поле log
public class App {

    // Создание источника данных с динамической конфигурацией на основе окружения
    public static DataSource createDataSource() {
        // Получаем значение переменной окружения, если оно задано (для продакшена/CI)
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");

        // Создаём конфигурацию для HikariCP
        HikariConfig config = new HikariConfig();

        // Если переменная окружения JDBC_DATABASE_URL установлена — используем её для подключения к PostgreSQL
        if (jdbcUrl != null && !jdbcUrl.isBlank()) {
            // Продакшен-конфигурация (например, PostgreSQL через render.com)
            config.setJdbcUrl(jdbcUrl);
            // Логин/пароль берутся прямо из строки, отдельно задавать не нужно
        } else {
            // Локальная разработка: встраиваемая база H2 в памяти
            config.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false");
            config.setDriverClassName("org.h2.Driver"); // Указываем драйвер H2
            config.setUsername("sa");                   // Стандартный логин H2
            config.setPassword("");                     // Пустой пароль
        }

        // Устанавливаем максимальное число соединений в пуле
        config.setMaximumPoolSize(10);

        // Возвращаем готовый пул соединений
        return new HikariDataSource(config);
    }

    /*Создание DataSource должно идти до инициализации базы —
    иначе DatabaseSetup не отработает, потому что некуда коннектиться.*/


    // Создаёт и настраивает Javalin-приложение с базовым роутом
    public static Javalin getApp(DataSource dataSource) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/"); // Раздача статики из resources/public
        });

        // Обработчик GET-запроса на корень ("/")
        app.get("/", ctx -> ctx.result("Hello, World!"));

        return app;
    }

    // Точка входа в приложение
    public static void main(String[] args) {
        // Получаем порт из переменной окружения или берём 7000 по умолчанию
        String portEnv = System.getenv().getOrDefault("PORT", "7000");
        int port = Integer.parseInt(portEnv);

        // Инициализируем пул соединений
        DataSource dataSource = createDataSource();

        // 🔧 Инициализация базы: создание таблиц и прочего
        // или механизм миграций или инициализации схемы
        try {
            DatabaseSetup.initialize(dataSource);
        } catch (SQLException e) {
            log.error("Ошибка при инициализации базы данных", e);
            return; // Прерываем запуск, если база не инициализировалась
        }
        /* Инициализация базы должна идти до старта сервера —
        иначе Javalin запустится без готовой базы, и запросы будут фейлиться.*/

        // Создаём и запускаем Javalin-приложение
        Javalin app = getApp(dataSource);
        app.start(port);

        // Логируем успешный запуск
        log.info("Server started on http://localhost:{}", port);
    }
}
