
# Tomcat 11 with Java 17
FROM tomcat:11-jdk17-temurin

# Remove default applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy Spring Boot WAR file
COPY target/schedule.war /usr/local/tomcat/webapps/schedule.war

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat server
CMD ["catalina.sh", "run"]
