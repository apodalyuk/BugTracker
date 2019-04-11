Реализация [тестового задания](Task.pdf)  

Сборка: `mvn clean install`  
Запуск: `docker-compose up`

Описание API:

GET /projects/ - перечень проектов  
Get-параметры:  
page - номер страницы, с 1, по умолчанию 1  
perPage - проектов на странице, по умолчанию 20  

GET /projects/{id} - конкретный проект  

POST /projects/ - создание проекта  
Параметры JSON:  
name - название, обязательное  
description - описание  

PATCH /projects/{id} - изменение проекта, обновляет только заполненные поля  
Параметры JSON:  
name - название  
description - описание  

DELETE /projects/{id} - удаление проекта  

GET /projects/{id}/tasks - перечень задач проекта  
Get-параметры:  
page - номер страницы, с 1, по умолчанию 1  
perPage - проектов на странице, по умолчанию 20  
sort - сортировка; допускает date:desc, date:asc, priority:desc, priority:asc  
minDate - минимальная дата создания включительно  
maxDate - максимальная дата создания включительно(время не учитывается)  
minPriority - минимальный приоритет включительно  
maxPriority - максимальный приоритет включительно  
status - фильтр по статусу задачи, допускает NEW, IN_PROGRESS, CLOSED  

GET /projects/{id}/tasks/{id} - конкретная задача  

POST /projects/{id}/tasks/ - создание задачи  
Параметры JSON:  
name - название, обязательное  
description - описание  
priority - приоритет, обязательное  

PATCH /projects/{id}/tasks/{id}  - изменение задачи, обновляет только заполненные поля  
Параметры JSON:  
name - название    
description - описание  
status - статус, допускает NEW, IN_PROGRESS, CLOSED    
priority - приоритет    

DELETE /projects/{id}/tasks/{id} - удаление задачи  
