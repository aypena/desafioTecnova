# 📊 Desafío Técnico - PROCESAMIENTO DE NÓMINA DESDE ARCHIVO CSV 

Este proyecto consiste en una API RESTful desarrollada con **Java + Spring Boot**, que permite cargar un archivo CSV con datos de empleados,
validar su contenido y calcular el salario final aplicando reglas de negocio.

---

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3.5.4
- Spring Web
- Spring Validation
- JUnit 5
- Mockito
- Maven 
- Lombok
- Logback (logging)
- CSV (archivo de entrada y salida)
- Intellij como IDE
- Postman

---

# 🧱 Arquitectura

La aplicación está desarrollada siguiendo el principio de **Arquitectura Limpia**, separando responsabilidades en tres capas principales:
src/

#presentation/controller/ # Controladores REST

#domain/usecase/ # Interfaces de casos de uso

#data/impl/ # Implementaciones de casos de uso

la cual es un conjunto de patrones de  diseño y buenas prácticas que se preocupa en separar las capas bien definidas y establecer límites entre ellas. 

# ⚙️ Cómo ejecutar la aplicación

Clonar el repositorio desde GitHub Ve a la página del repositorio en GitHub: https://github.com/aypena/desafioTecnova
Haz clic en el botón Code y copia la URL (HTTPS ).

Abre tu terminal (Git Bash / Terminal / PowerShell).

Posiciónate sobre una carpeta en la cual descargaras el proyecto

Ejecuta el comando:

git clone https://github.com/aypena/desafioTecnova.git
Esto creará una carpeta llamada desafioTecnova con todos los archivos del proyecto localmente.

Siguiente paso:
Abrir el proyecto en IntelliJ IDEA el cual fue el IDE que yo utilice.
Selecciona File → Open... (o Open Project).

Navega hasta la carpeta desafioTecnova que acabas de clonar y ábrela.

IntelliJ detectará automáticamente el proyecto.

Ejecutar la aplicación Con el IDE configurado:
En IntelliJ, busca la clase con el método main(...) que arranca Spring Boot (normalmente en src/main/java/DesafioTecnovaApplication)


Una vez el Proyecto ya se este ejecutando procedemos a enviar una peticion Post a traves de la aplicacion Postman el cual se detallara a continuacion

# ⚙️ Cómo ejecutar la aplicación desde Postman

- Iniciar aplicación Postman
- hacer clic en el botón New  -> collection  -> colocar un nombre.
- nos vamos al nombre de la collection que acabamos de hacer el cual se encontrara en un listado del lado izquierdo.
- posamos el cursor en el nombre y hacemos clic en el símbolo de Add request (+)
- hacemos el cambio en el Combo de la petición GET a POST y donde se escribe la url pegamos lo siguiente:
- http://localhost:8080/api/nomina/procesar?archivoNombre=empleados.csv
- por último hacemos clic en el botón Send

Podremos observar la respuesta de la petición enviada. en este cado puede ser:
Procesamiento completado. Revisa los CSV en resources/output.


# 📂 Entrada esperada
La API espera un archivo CSV el cual se encontrara en la siguiente ruta del proyecto: "src/main/resources/input/empleados.csv” 

# 📤 Salida generada

Se genera un archivo CSV con los salarios calculados, en la ruta:
- "src/main/resources/output/empleados_validos.csv"
- "src/main/resources/output/empleados_invalidos.csv"

# También se podrá ver la información en el terminal o consola del ide un log Con la Siguiente información:
- Total empleados válidos: 307635
- Total empleados inválidos: 692365
- Promedio salario final: 394919,55
- Promedio antigüedad (años): 1,82

