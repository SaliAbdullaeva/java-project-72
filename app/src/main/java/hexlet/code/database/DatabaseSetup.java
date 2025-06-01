package hexlet.code.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// Класс для инициализации структуры базы данных
public class DatabaseSetup {
    // 💾 База данных — схемы, структура
    // 👉 отвечает за создание таблиц, миграции, структуру БД

    public static void initialize(DataSource dataSource) throws SQLException {
        // Конструкция try-with-resources
        // Отслеживаем соединение с базой данных
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            // SQL-скрипт для создания таблицы urls
            String sql = """
                CREATE TABLE IF NOT EXISTS urls (
                    id IDENTITY PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """; // Чтобы автоматически ставилась дата

            stmt.execute(sql); // Выполняем создание таблицы
        }
    }
}
