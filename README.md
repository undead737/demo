# Проект - сервис по работе с документами

Требования к инструментам
* java версии 17

### Запуск приложения

* Запустить класс ru.demo.DemoApplication или
Собрать demo-parent с профилем jar-generate, запустить jar (java -jar demo-0.0.1-SNAPSHOT.jar)
 <p></p>
* К проекту подключен Swagger http://localhost:8080/swagger-ui/index.html#/
<p></p>
* Ошибки работы сервиса для пользователя пишутся в protocolRecords. (Стоит @JsonIgnore, чтобы выходные данные соответствовали постановке)
    
### Добавить новый вид документа для подсчета количество страниц

1) Создаем класс обработчик - наследуем от AbstractDocument или имплементируем Document
2) Добавляем новый тип документа в DocumentType, указываем расширение файла, указываем класс обработчик