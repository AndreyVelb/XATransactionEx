1. Atomikos 6 
2. Для того чтобы в mysql можно было использовать ха транзакции необходимо наделить этими правами пользователя
   ```GRANT ALL PRIVILEGES ON *.* TO 'second_db_user'@'%' ;```
   ```FLUSH PRIVILEGES;```

    ```GRANT XA_RECOVER_ADMIN ON *.* TO 'second_db_user'@'%' ;```
    ```FLUSH PRIVILEGES;```
3. 