package hexlet.code.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
// Url-модель — это как бы программное представление одной url строки.
public class Url {
    // 📦 Модели (Domain) — сущности, с которыми работает бизнес-логика

    private Long id;                  // автоинкрементное поле
    private String name;             // адрес сайта
    private LocalDateTime createdAt; // дата добавления
}
