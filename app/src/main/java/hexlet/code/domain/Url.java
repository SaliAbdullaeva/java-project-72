package hexlet.code.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
// Вот Url-модель — это как бы программное представление одной такой строки.
public class Url {
    private Long id;                  // автоинкрементное поле
    private String name;             // адрес сайта
    private LocalDateTime createdAt; // дата добавления
}

