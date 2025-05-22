package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

@Slf4j //для логгирования
public class App {

    // Метод для получения экземпляра Javalin
    public static Javalin getApp() {
        // Создаем экземпляр Javalin
        Javalin app = Javalin.create(config -> {
            // Настройка статических файлов
            config.staticFiles.add("/");
        });
        // Настройка маршрутов
        app.get("/", ctx -> ctx.result("Hello, World!"));

        // Возврат настроенного экземпляра Javalin
        return app;
    }

    // Метод для запуска приложения
    public static void main(String[] args) {
        // Получаем экземпляр приложения
        Javalin app = getApp();
        // Запускаем приложение на порту 7000
        app.start(7000);
        log.info("Server started on http://localhost:7000");
    }
}

