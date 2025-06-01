package hexlet.code;

import io.javalin.Javalin;                  // Легковесный веб-фреймворк для создания REST API и веб-приложений
import lombok.extern.slf4j.Slf4j;            // Lombok-аннотация для генерации логгера (SLF4J) в классе
import hexlet.code.database.DatabaseSetup;   // Класс для инициализации структуры базы данных (создание таблиц)
import hexlet.code.database.DatabaseConfig;  // Класс, который конфигурирует и создаёт DataSource (пул соединений)
import javax.sql.DataSource;                 // Интерфейс для работы с пулом соединений к базе данных
import java.sql.SQLException;                // Исключение для обработки ошибок SQL и БД

@Slf4j // Логгер от Lombok, создаёт поле log
public class App { // 🛰️ Сервер — точка входа, веб-приложение (Javalin)

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
        // 👉 Устанавливаем переменную окружения вручную (только для отладки!)
        if (System.getenv("JDBC_DATABASE_URL") == null) {
            System.setProperty("JDBC_DATABASE_URL",
                    "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=password");
        }

        // Получаем порт из переменной окружения или берём 7000 по умолчанию
        String portEnv = System.getenv().getOrDefault("PORT", "7000");
        int port = Integer.parseInt(portEnv);

        // Инициализируем пул соединений
        // Создание DataSource должно идти до инициализации базы —
        // иначе DatabaseSetup не отработает, потому что некуда коннектиться.
        DataSource dataSource = DatabaseConfig.createDataSource();

        // 🔧 Инициализация базы: создание таблиц и прочего
        // или механизм миграций или инициализации схемы бд
        try {
            DatabaseSetup.initialize(dataSource); // подготавливаем базу
        } catch (SQLException e) {
            log.error("Ошибка при инициализации базы данных", e);
            return; // Прерываем запуск, если база не инициализировалась
        }
        /* Инициализация базы должна идти до старта сервера с Джавалин —
        иначе Javalin запустится без готовой базы, и запросы будут фейлиться.*/

        // Создаём и запускаем Javalin-приложение
        Javalin app = getApp(dataSource);
        app.start(port);

        // Логируем успешный запуск
        log.info("Server started on http://localhost:{}", port);
    }
}
