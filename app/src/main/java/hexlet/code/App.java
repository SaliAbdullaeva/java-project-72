package hexlet.code;

import io.javalin.Javalin;                   // Легковесный веб-фреймворк для создания REST API и веб-приложений
import lombok.extern.slf4j.Slf4j;            // Lombok-аннотация для генерации логгера (SLF4J) в классе
import hexlet.code.database.DatabaseSetup;   // Класс для инициализации структуры базы данных (создание таблиц)
import hexlet.code.database.DatabaseConfig;  // Класс, который конфигурирует и создаёт DataSource (пул соединений)
import javax.sql.DataSource;                 // Интерфейс для работы с пулом соединений к базе данных
import java.sql.SQLException;                // Исключение для обработки ошибок SQL и БД

import gg.jte.ContentType;                   // Тип контента, который будет отдавать шаблонизатор (HTML)
import gg.jte.TemplateEngine;                // Основной класс Jte-шаблонизатора
import gg.jte.resolve.ResourceCodeResolver;  // Класс для указания, откуда брать шаблоны

@Slf4j // Логгер от Lombok, создаёт приватное поле log для логирования
public class App { // 🛰️ Сервер — точка входа, веб-приложение (Javalin)

    // Создаёт и настраивает Javalin-приложение с базовым роутом
    public static Javalin getApp(DataSource dataSource) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/"); // Раздача статики из resources/public
            config.fileRenderer(new io.javalin.rendering.template.JavalinJte(createTemplateEngine())); // Подключаем шаблонизатор
        });

        // Обрабатывает GET-запрос на корень  "/" рендерит и возвращает сгенерированную HTML-страницу из шаблона index.jte
        app.get("/", ctx -> ctx.render("index.jte"));
        return app;
    }

    // Создание и конфигурация движка Jte-шаблонизатора
    private static TemplateEngine createTemplateEngine() {
        // Получаем ClassLoader текущего класса, чтобы загрузить шаблоны из ресурсов
        ClassLoader classLoader = App.class.getClassLoader();
        // Указываем путь к шаблонам
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        // Создаем и возвращаем движок шаблонизатора Jte, который будет рендерить HTML-шаблоны,
        // используя указанный код-резолвер для поиска шаблонов в папке "templates"
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    // Точка входа в приложение
    public static void main(String[] args) {
        // Проверяем и станавливаем переменную окружения для подключения к базе вручную (только для отладки!)
        if (System.getenv("JDBC_DATABASE_URL") == null) {
            System.setProperty("JDBC_DATABASE_URL",
                    "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=password");
        }

        // Получаем порт из переменной окружения или берём 7000 по умолчанию
        String portEnv = System.getenv().getOrDefault("PORT", "7000");
        int port = Integer.parseInt(portEnv);

        // Инициализируем пул соединений
        DataSource dataSource = DatabaseConfig.createDataSource();

        // Инициализация базы: создание таблиц и прочего
        try {
            DatabaseSetup.initialize(dataSource); // подготавливаем базу
        } catch (SQLException e) {
            log.error("Ошибка при инициализации базы данных", e);
            return; // Прерываем запуск, если база не инициализировалась
        }

        // Создаём и запускаем Javalin-приложение
        Javalin app = getApp(dataSource);
        app.start(port);

        // Логируем успешный запуск
        log.info("Server started on http://localhost:{}", port);
    }
}
