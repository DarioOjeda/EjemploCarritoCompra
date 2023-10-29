package com.example.carritocompra.interfaces;

import com.example.carritocompra.models.Carrito;
import com.example.carritocompra.models.Producto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
// Para convertir la API creada con SpringBoot en una interfaz de Java(y hacerla por tanto más manejable) he usado Retrofit2
// estos son los mismos endpoint que hay en la carpeta carrito-apirest, archivo CarritoApirestApplication
public interface CRUDInterface {
// Las peticiones a la Api desde la aplicación son las siguientes
    @GET("productos")
    Call<List<Producto>> obtenerListaCompletaProductos();

    @GET("carritos/{user_id}")
    Call<Carrito> obtenerListaCarritoPorUsuario(@Path(value = "user_id", encoded = true) String userId);
    @PUT("carritos/add/{id_carrito}/{product_id}")
    Call<ArrayList<Producto>> añadirArticulo(@Path(value = "id_carrito", encoded = true) int id_carrito, @Path(value = "product_id", encoded = true) int product_id);
    @PUT("carritos/delete/{id_carrito}/{product_id}/{posicion_carrito}")
    Call<ArrayList<Producto>> borrarArticulo(@Path(value = "id_carrito", encoded = true) int id_carrito, @Path(value = "product_id", encoded = true) int product_id, @Path("posicion_carrito") int posicion_carrito );
}
