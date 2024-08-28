FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/ExamAndClassAttendanceApi-0.0.1-SNAPSHOT.jar ExamAndClassAttendanceApi-0.0.1-SNAPSHOT.jar
EXPOSE 8088
CMD ["java","-jar","ExamAndClassAttendanceApi-0.0.1-SNAPSHOT.jar"]
