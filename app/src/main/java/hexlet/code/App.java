package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

@Slf4j // Для логирования
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

        return app;
    }

    // Метод для запуска приложения
    public static void main(String[] args) {
        // Получаем порт из переменной окружения (для Render), иначе дефолтный 7000
        String portEnv = System.getenv().getOrDefault("PORT", "7000");
        int port = Integer.parseInt(portEnv);

        // Получаем экземпляр приложения
        Javalin app = getApp();
        app.start(port);

        log.info("Server started on http://localhost:{}", port); //логируем адрес сервера
    }
}
