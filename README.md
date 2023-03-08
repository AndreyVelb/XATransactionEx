1. Atomikos 6 
2. Для того чтобы в mysql можно было использовать ха транзакции необходимо наделить этими правами пользователя
```
    GRANT XA_RECOVER_ADMIN ON *.* TO 'second_db_user'@'%' ;
    FLUSH PRIVILEGES;
```
4. Для PostgreSQL необходимо установить максимальное количество подготовленных транзакций в postgresql.conf. 
В случае развертывания в Docker устанавливаем
```    
    services:
        postgres:
            command:
                - "postgres"
                - "-c"
                - "max_prepared_transactions=2"
```