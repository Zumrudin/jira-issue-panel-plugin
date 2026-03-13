# Jira Issue Panel Plugin

Плагин для Jira Data Center 8.20 — добавляет кастомную панель на страницу задачи с возможностью просматривать и редактировать данные.

## Структура проекта

```
jira-issue-panel-plugin/
├── pom.xml
├── .gitignore
├── .github/
│   └── workflows/
│       └── build.yml                        ← CI/CD сборка JAR через GitHub Actions
└── src/main/
    ├── java/com/Gadzhiev/jira/panel/
    │   ├── ao/
    │   │   └── PanelData.java               ← Active Objects сущность (хранение в БД)
    │   ├── PanelDataBean.java               ← DTO
    │   ├── PanelDataService.java            ← Интерфейс сервиса
    │   ├── PanelDataServiceImpl.java        ← Реализация сервиса
    │   ├── IssuePanelContextProvider.java   ← Контекст для Velocity-шаблона
    │   └── PanelRestResource.java          ← REST API (GET + POST)
    └── resources/
        ├── atlassian-plugin.xml             ← Дескриптор плагина
        ├── templates/
        │   └── panel-view.vm               ← Velocity-шаблон панели
        ├── css/
        │   └── panel.css                   ← Стили
        └── js/
            └── panel.js                    ← Логика редактирования
```

## Как загрузить в Git и собрать JAR

### 1. Инициализация репозитория

```bash
cd jira-issue-panel-plugin
git init
git add .
git commit -m "Initial commit: Jira Issue Panel Plugin"
```

### 2. Загрузка на GitHub

```bash
# Создай репозиторий на github.com, затем:
git remote add origin https://github.com/ВАШ_ЛОГИН/jira-issue-panel-plugin.git
git branch -M main
git push -u origin main
```

### 3. Автоматическая сборка (GitHub Actions)

После `git push` GitHub автоматически запустит сборку (`.github/workflows/build.yml`).

Скачать JAR:
- Перейди в `Actions` → выбери последний запуск → `Artifacts` → скачай `jira-panel-plugin-jar`

### 4. Локальная сборка (опционально)

Требования: JDK 11+, Maven 3.6+

```bash
mvn package -DskipTests
# JAR будет в: target/jira-issue-panel-plugin-1.0.0.jar
```

## Установка в Jira DC

1. Войди в Jira как администратор
2. Перейди: `Settings → Apps → Manage apps → Upload app`
3. Загрузи JAR-файл
4. Плагин появится в списке установленных

## REST API

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/rest/gadzhiev-panel/1.0/panel/{issueKey}` | Получить данные панели |
| POST | `/rest/gadzhiev-panel/1.0/panel/{issueKey}` | Сохранить данные панели |

### Пример POST-запроса

```json
{
  "customText": "Текст поля",
  "customNote": "Заметка"
}
```

## Настройка под себя

- Изменить расположение панели: отредактируй `location` в `atlassian-plugin.xml`
  - `atl.jira.view.issue.right.context` — правая колонка
  - `atl.jira.view.issue.left.context` — левая колонка
- Добавить поля: расширь `PanelData.java`, `PanelDataBean.java`, шаблон и JS
