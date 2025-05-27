package hexlet.code.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// Класс для инициализации структуры базы данных
public class DatabaseSetup {

    public static void initialize(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            // SQL-скрипт для создания таблицы urls
            String sql = """
                CREATE TABLE IF NOT EXISTS urls (
                    id IDENTITY PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP
                );
                """;

            stmt.execute(sql); // Выполняем создание таблицы
        }
    }
}
