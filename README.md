# Globetrotting

Globetrotting es una aplicación desarrollada en Android con Kotlin, destinada a la gestión de reservas en una agencia de viajes. Los usuarios registrados pueden realizar y gestionar sus reservas.

## Tecnologías Utilizadas

- **Android con Kotlin**: El lenguaje principal utilizado para desarrollar la aplicación.
- **API de ChatGPT**: Integración con la API de ChatGPT para proporcionar descripciones de los destinos de viaje.
- **Backend de Firebase**: Utilizado para la autenticación de usuarios y almacenamiento en tiempo real.

## Estructura de la Aplicación

La aplicación sigue el patrón de arquitectura MVVM (Model-View-ViewModel):

- **Model**: Contiene las clases de datos y la lógica de negocio de la aplicación. Interactúa con Firebase para recuperar y almacenar datos.
- **View**: Incluye las actividades y fragmentos que forman la interfaz de usuario de la aplicación.
- **ViewModel**: Actúa como un intermediario entre el Model y la View. Gestiona los datos para las interfaces de usuario y maneja la lógica de presentación.

## Interfaz del Usuario

La interfaz de usuario de Globetrotting está diseñada para ser intuitiva y fácil de usar. Aquí se muestran algunas capturas de pantalla de la aplicación:
![Interfaz de usuario](https://github.com/marruiart/globetrotting-android/assets/88201067/b101fe58-c8b4-4efe-999a-0dac1c44e2ba)
