# EjemploCarritoCompra
Aplicación Android en Java para cubrir un CRUD, usando, de por medio, una API REST con Springboot
permita ver el precio calculado de todos los artículos que el cliente agregue.

Este proyecto consta de dos partes (o aplicaciones):
-La aplicación Android en Java. Una aplicación simple que cubre algunos de los conceptos fundamentales de Java
para implementar un CRUD: Los ArrayList, interfaces, los RecyclerView...
Además, se usa retrofit2 para comunicar con la Api (ver aplicación 2)

-La Api Rest en puro Java, que utiliza las librerías de Springboot para montar un "servidor" en localhost y que la aplicación Android pueda realizar peticiones http a esta.
Las dos clases principales que representan objetos con una correspondecia directa en la realidad son Producto y Carrito, teniendo sendas clases sus correspondientes representaciones
en Java en la aplicación Android.

Para poder visualizar correctamente el ejmplo será necesario:
-Un IDE de Java como, por ejemplo, El InteiJ, para ejecutar correctamente de la Api REST (carpeta "carrito-apirest"). Una vez lanzada podemos ejecutar la aplicación Android.

-Tener instalado el Android Studio, con el cual, compilaremos y montaremos la aplicación de Android sobre un emulador de Android. Pero antes, tenemos que modificar el fichero 
Constants.java de la varpeta AplicacionAndroid\app\src\main\java\com\example\carritocompra\utils\constants (en el repositorio de la aplicación de Android), para que la app de Android,
mediante retrofit2, establezca la comunicación con la Api Rest en localhost, con Springboot(para saber dicha ip en equipos Windows, abrimos la consola cmd y ejecutamos ipconfig)

-Hecho esto, ejecutamos la app(carpeta AplicacionAndroid) sobre un emulador de Android con una version de Android 7 o superior.
