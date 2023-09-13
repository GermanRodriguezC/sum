FROM openjdk:17-alpine

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia todos los archivos del proyecto al contenedor
COPY . /app

# Ejecuta la construcción de la aplicación con Gradle
RUN ./gradlew clean bootJar

# Comando para ejecutar la aplicación
CMD java -jar /app/build/libs/sum-0.0.1-SNAPSHOT.jar