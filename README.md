version: '3'
services:
db:
image: mysql:8.0
environment:
MYSQL_ROOT_PASSWORD: 1234
MYSQL_DATABASE: yourproject
ports:
- "3306:3306"
volumes:
- mysql-data:/var/lib/mysql

app:
build: .
ports:
- "8080:8080"
depends_on:
- db
environment:
- SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/yourproject
- SPRING_DATASOURCE_USERNAME=root
- SPRING_DATASOURCE_PASSWORD=1234

volumes:
mysql-data: