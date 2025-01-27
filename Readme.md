# Nemesis.Hack.Memory 
## хакерская программа для чтения и изменения памяти процесса

# TODO: Создание аналога Cheat Engine @ Сгенерировано нейросетью

## 1. Исследование и планирование
- [ ] Изучить основы работы Cheat Engine (основные функции, принципы работы).
    - [x] Александр
    - [ ] n0o0m4d
    - [ ] jhowoo
- [ ] Разобраться с основами работы памяти в ОС (Windows/Linux).
    - [x] Александр
    - [ ] n0o0m4d
    - [ ] jhowoo
- [ ] Ознакомиться с библиотеками для работы с памятью (например, `ReadProcessMemory`, `WriteProcessMemory` в Windows API).
    - [ ] Александр
    - [ ] n0o0m4d
    - [ ] jhowoo
- [ ] Определить минимальный функционал программы:
    - Сканирование памяти.
    - Поиск значений (точное значение, диапазон, изменение).
    - Редактирование значений.
    - Интерфейс.

## 2. Архитектура проекта
- [ ] Спроектировать архитектуру программы:
    - Модуль взаимодействия с процессами.
    - Модуль для анализа и поиска данных.
    - Графический или консольный интерфейс.
- [ ] Решить, будет ли приложение кроссплатформенным.
    - [ ] Александр
    - [ ] n0o0m4d
    - [ ] jhowoo

## 3. Работа с процессами
- [ ] Реализовать получение списка запущенных процессов. ## Александр взял в работу
- [ ] Настроить выбор процесса для анализа.
- [ ] Реализовать чтение памяти процесса:
    - Чтение указанных адресов.
    - Построение механизма поиска данных (точное значение, диапазон, изменение).

## 4. Функционал сканирования памяти
- [ ] Добавить поддержку поиска по:
    - Целым числам.
    - Числам с плавающей запятой.
    - Строкам.
    - Байтам.
- [ ] Реализовать динамическое обновление списка найденных адресов.
- [ ] Реализовать механизм заморозки значений в памяти.

## 5. Редактирование памяти
- [ ] Добавить возможность изменения значений в памяти.
- [ ] Добавить возможность сохранения адресов для дальнейшего использования.

## 6. Пользовательский интерфейс
- [ ] Выбрать тип интерфейса:
    - Консольный.
    - Графический (например, с использованием ImGui или другого фреймворка).
- [ ] Реализовать удобный список адресов и значений.
- [ ] Добавить опции фильтрации и поиска.

## 7. Тестирование
- [ ] Написать тесты для проверки всех основных функций.
- [ ] Провести тестирование на реальных приложениях/играх.
- [ ] Реализовать обработку ошибок (например, недостаточные права, неверный ввод).

## 8. Дополнительные функции (опционально)
- [ ] Создать модуль для анализа структуры памяти (например, указатели, массивы).
- [ ] Реализовать сканирование динамической памяти (pointer scanning).
- [ ] Добавить возможность создания скриптов для автоматизации.
- [ ] Поддержка работы с таблицами данных (например, .CT-файлы из Cheat Engine).
- [ ] Поддержка сетевых игр (анализ пакетов).

## 9. Оптимизация и релиз
- [ ] Оптимизировать скорость сканирования памяти.
- [ ] Убедиться в кроссплатформенности (если нужно).
- [ ] Подготовить документацию для пользователей.
- [ ] Выпустить минимально работоспособную версию (MVP).

## 10. Драйвер
- Пока в планах

---
# Модули проекта Nemesis.Hack.Memory

## 1. CoreProcessManager (Управление процессами)
**Ответственность**: Работа с процессами ОС.  
**Будет включать**:
- Получение списка запущенных процессов.
- Выбор процесса для анализа.
- Открытие хендла процесса.
- Закрытие хендлов для предотвращения утечек.

**Взаимодействие с другими модулями**:
- Используется в модулях чтения/записи памяти и сканирования.

## 2. MemoryReaderWriter (Чтение/запись памяти)
**Ответственность**: Работа с памятью процесса.  
**Будет включать**:
- Чтение памяти (`ReadProcessMemory`).
- Запись памяти (`WriteProcessMemory`).
- Основу для операций редактирования значений.

**Взаимодействие с другими модулями**:
- Используется в функционале поиска (поиск и чтение данных) и редактирования памяти.

## 3. MemoryScanner (Сканирование памяти)
**Ответственность**: Поиск данных в памяти.  
**Содержит**:
- Алгоритмы поиска значений:
  - Точное значение.
  - Диапазон значений.
  - Изменение значений.
- Поиск по типам данных:
  - Целые числа.
  - Float.
  - Строки.
  - Биты.
- Динамическое обновление адресов.
- Механизм заморозки значений.

**Взаимодействие с другими модулями**:
- Использует `MemoryReaderWriter` для чтения памяти.

## 4. MemoryEditor (Редактирование памяти)
**Ответственность**: Изменение значений в памяти.  
**Будет включать**:
- Инструменты для изменения значений.
- Сохранение адресов для дальнейшего использования.
- Функцию заморозки значений.

**Почему отдельный модуль**:  
Хотя функционал тесно связан с `MemoryScanner`, выделение редактора в отдельный модуль упрощает сопровождение кода. Этот модуль сосредоточен на изменении, тогда как `MemoryScanner` занимается только поиском.

## 5. UIManager (Интерфейс пользователя)
**Ответственность**: Реализация интерфейса (консольного или графического).  
**Содержит**:
- Вывод списка процессов.
- Отображение найденных адресов и их значений.
- Контролы для редактирования и заморозки.
- Возможность фильтрации данных.

**Особенности**:
- Консольный интерфейс для минимальной версии (MVP).
- Возможность дальнейшего перехода на графический интерфейс (например, через ImGui).

## 6. Tests (Модуль тестирования)
**Ответственность**: Автоматическое и ручное тестирование.  
**Будет включать**:
- Набор тестов для чтения/записи памяти.
- Тесты поиска значений.
- Проверку работы интерфейса.
- Тестирование на реальных приложениях/играх.

## 7. OptionalDriver (Драйвер, если потребуется)
**Ответственность**: Низкоуровневый доступ к памяти (опционально).  
**Когда использовать**:
- Если требуется доступ к защищённым областям памяти.
- Для работы с системами защиты от отладчиков.
