# 🚀 Запуск проекта (Docker + PostgreSQL + Spring Boot + Flyway)

Инструкция по запуску проекта локально **после** клонирования репозитория.

---

## 📋 Требования
- **Git**
- **Docker** (и Docker Compose / `docker compose`)
- **Java + Maven** (только если нужно собирать/проверять jar/миграции локально)

---

## 1) Клонировать репозиторий и перейти в папку
```bash
git clone <REPO_URL>
cd <repo-folder>
```

---

## 2) Проверить/установить переменные окружения (пример)
```bash
POSTGRES_DB=auth-db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=root
DB_PORT=5432
```

---

## 3) Сборка и запуск контейнеров
(в корне, где лежит `docker-compose.yml`):
```bash
docker compose up --build

Пароли от всех юзеров в таблице это username123. 
```

---
Если не работает, то
## 4) Проверить состояние контейнеров
```bash
docker compose ps
# или
docker ps
```

---

## 5) Подключиться к PostgreSQL
```bash
docker exec -it postgres-db psql -U postgres -d auth-db
```

### Внутри psql:
```sql
\dt
SELECT * FROM flyway_schema_history;
\q
```

---

## 🔄 Очистка

### Полностью удалить volumes (удалит БД):
```bash
docker compose down -v
```

### Быстрая очистка таблиц (для повторного применения миграций Flyway):
```bash
docker exec -it postgres-db psql -U postgres -d auth-db \
  -c "DROP TABLE IF EXISTS users CASCADE; DROP TABLE IF EXISTS flyway_schema_history CASCADE;"
```

После этого перезапустите контейнеры:
```bash
docker compose up -d --build
```
