# Консольный ZIP-архиватор

Учебный проект на Java 21.

## Возможности
- создание ZIP-архива из файла или папки;
- распаковка ZIP-архива;
- консольное меню;
- собственное исключение `ArchiveException`;
- интерфейс `ArchiveOperation` с полиморфным методом `execute`;
- `enum ArchiveAction`;
- `record ArchiveRequest` с автоматическими `equals/hashCode`;
- `sealed interface` и `switch`-выражение Java 21.

## Запуск
Открыть проект как Maven-проект в IntelliJ IDEA и запустить `Main.java`.
