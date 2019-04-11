FROM postgres:9.5

COPY schema.sql /docker-entrypoint-initdb.d/

RUN chmod 775 /docker-entrypoint-initdb.d/schema.sql
