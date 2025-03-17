# Sử dụng image của OpenJDK 17 (phiên bản slim để giảm kích thước)
FROM openjdk:17-jdk-slim

# Tham số để truyền vào tên file jar sau khi build
ARG JAR_FILE=target/*.jar

# Copy file jar vào image và đặt tên là app.jar
COPY ${JAR_FILE} app.jar

# Mở cổng mà ứng dụng sử dụng (thông thường là 8080)
EXPOSE 8080

# Lệnh khởi động container chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app.jar"]
