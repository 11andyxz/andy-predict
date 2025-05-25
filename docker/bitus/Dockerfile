# 使用官方 Java 基础镜像
FROM eclipse-temurin:17-jdk


# 复制 Jar 包、模型和配置文件
COPY model-0.0.1-SNAPSHOT.jar /app.jar

# 暴露端口（与 Spring Boot 的 server.port 一致）
EXPOSE 2022

# 启动命令
CMD ["java", "-jar", "/app.jar"]