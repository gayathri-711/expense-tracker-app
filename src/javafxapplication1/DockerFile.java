/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication1;

/**
 *
 * @author BEST SOLUTION SALEM
 */
# Use a base image with JDK 20
FROM openjdk:20-jdk

# Install required GUI libraries for JavaFX
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your host into the container
COPY MyJavaFXApp.jar /app/MyJavaFXApp.jar

# Run your JavaFX app
CMD ["java", "-jar", "MyJavaFXApp.jar"]
