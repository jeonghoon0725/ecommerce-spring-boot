##### mysql 설치

docker run --name spring-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=spring_db
-d mysql:8.0

#### redis 설치

docker run -d --name redis-server -p 6379:6379 redis