# Globetrotting

Globetrotting es una aplicación desarrollada en Android con Kotlin, destinada a la gestión de reservas en una agencia de viajes. Los usuarios registrados pueden realizar y gestionar sus reservas.

## Tecnologías Utilizadas

- **Android con Kotlin**: El lenguaje principal utilizado para desarrollar la aplicación.
- **API de ChatGPT**: Integración con la API de ChatGPT para proporcionar asistencia inteligente y recomendaciones personalizadas.
- **Backend de Firebase**: Utilizado para la autenticación de usuarios, almacenamiento en tiempo real y funciones de backend.

## Estructura de la Aplicación

La aplicación sigue el patrón de arquitectura MVVM (Model-View-ViewModel):

- **Model**: Contiene las clases de datos y la lógica de negocio de la aplicación. Interactúa con Firebase para recuperar y almacenar datos.
- **View**: Incluye las actividades y fragmentos que forman la interfaz de usuario de la aplicación.
- **ViewModel**: Actúa como un intermediario entre el Model y la View. Gestiona los datos para las interfaces de usuario y maneja la lógica de presentación.

### Diagrama de Estructura MVVM
+-----------+ +-----------+ +-----------+
| View | <--> | ViewModel | <--> | Model |
+-----------+ +-----------+ +-----------+
## Interfaz del Usuario

La interfaz de usuario de Globetrotting está diseñada para ser intuitiva y fácil de usar. Aquí se muestran algunas capturas de pantalla de la aplicación:
![Google Pixel 4 XL Screenshot 1](https://github.com/marruiart/globetrotting-android/assets/88201067/3adb403d-3787-4cb4-ade4-42d6887a7568)![Google Pixel 4 XL Screenshot 2](https://github.com/marruiart/globetrotting-android/assets/88201067/035ead60-bc23-4078-a00a-3e63cff53829)![Google Pixel 4 XL Screenshot 3](https://github.com/marruiart/globetrotting-android/assets/88201067/c03c882d-f17b-4335-b1b9-c4742439ff1c)![Google Pixel 4 XL Screenshot 4](https://github.com/marruiart/globetrotting-android/assets/88201067/8e018219-357a-4b32-9460-ed55efe58469)