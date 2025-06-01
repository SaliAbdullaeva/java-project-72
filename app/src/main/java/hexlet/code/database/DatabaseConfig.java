package hexlet.code.database;

import com.zaxxer.hikari.HikariConfig;      // Класс конфигурации для настройки пула HikariCP
import com.zaxxer.hikari.HikariDataSource;  // Реализация DataSource с пулом соединений HikariCP
import javax.sql.DataSource;                 // Интерфейс для источника соединений с базой данных
/*
Метод createDataSource создает и настраивает пул соединений с базой данных с помощью библиотеки HikariCP.
В зависимости от переменной окружения выбирает конфигурацию для подключения к удалённой PostgreSQL
или локальной базе H2 и возвращает настроенный объект HikariDataSource, реализующий интерфейс DataSource.
*/
public class DatabaseConfig {
    // 💾 База данных — соединения
    // 👉 конфигурирует подключение, пул соединений (через HikariCP, например)


    // Создание источника данных с динамической конфигурацией на основе окружения
    public static DataSource createDataSource() {

        // Получаем значение переменной окружения с URL базы данных, если оно задано (для продакшена/CI)
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");

        // Создаём конфигурацию для HikariCP
        HikariConfig config = new HikariConfig();

        // Если переменная окружения JDBC_DATABASE_URL установлена — используем её для подключения к PostgreSQL
        if (jdbcUrl != null && !jdbcUrl.isBlank()) {
            // Продакшен-конфигурация (например, PostgreSQL через render.com)
            config.setJdbcUrl(jdbcUrl);
            // Логин/пароль берутся прямо из строки, отдельно задавать не нужно
            config.setDriverClassName("org.postgresql.Driver"); // // Явное указание драйвера для PostgreSQL (продакшен)
        } else {
            // Локальная разработка: встраиваемая база H2 в памяти
            config.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false");
            config.setDriverClassName("org.h2.Driver"); // Указываем драйвер H2
            config.setUsername("sa");                   // Стандартный логин H2
            config.setPassword("");                     // Пустой пароль
        }

        // Устанавливаем размер пула
        config.setMaximumPoolSize(10); // — это максимальное количество активных соединений одновременно.

        System.out.println(">>> Подключаюсь к БД через: " + config.getJdbcUrl());

        // Возвращаем готовый пул соединений
        return new HikariDataSource(config);

    }
}
