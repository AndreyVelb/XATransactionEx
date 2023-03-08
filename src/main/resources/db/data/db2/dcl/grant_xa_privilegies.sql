##Run as root user
GRANT XA_RECOVER_ADMIN ON *.* TO 'second_db_user'@'%' ;
FLUSH PRIVILEGES;