package hexlet.code.repository;

import javax.sql.DataSource;

// Абстрактный базовый класс для всех репозиториев.
// Содержит общее поле dataSource — источник соединений с БД.
public class BaseRepository {

    // Поле для хранения источника соединений с базой данных.
    protected final DataSource dataSource;

    // Конструктор, в который передаётся dataSource, чтобы репозитории могли брать соединения.
    protected BaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
