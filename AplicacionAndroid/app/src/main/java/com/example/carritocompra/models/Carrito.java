package com.example.carritocompra.models;

import android.util.Log;
import android.widget.BaseAdapter;

import com.example.carritocompra.CarritoActivity;
import com.example.carritocompra.MainActivity;
import com.example.carritocompra.interfaces.CRUDInterface;
import com.example.carritocompra.utils.constants.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Carrito implements Serializable {
    private int id;
    private int idCliente;
    List<Producto> productos;
    BaseAdapter adapter;


    public Carrito(int id, int idCliente, List<Producto> productos) {
        this.id = id;
        this.idCliente = idCliente;
        this.productos = productos;
    }


    public List<Producto> getProductos() {
        return productos;
    }

    public int getId() {
        return id;
    }


    public void añadirArticulo(int id_articulo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CRUDInterface crudInterface = retrofit.create(CRUDInterface.class);
        Call<ArrayList<Producto>> call = crudInterface.añadirArticulo(getId(), id_articulo);

        call.enqueue(
                new Callback<ArrayList<Producto>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Producto>> call, Response<ArrayList<Producto>> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Response Err: ", response.message());
                        } else {
                            productos = response.body();
                            MainActivity.repintarAdapter();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Producto>> call, Throwable t) {
                        Log.e("Throw Err: ", t.getMessage());
                    }
                }
        );
    }

    public void borrarArticuloDelCarrito(int id_articulo, int posicion) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CRUDInterface crudInterface = retrofit.create(CRUDInterface.class);
        Call<ArrayList<Producto>> call = crudInterface.borrarArticulo(getId(), id_articulo, posicion);


        call.enqueue(
                new Callback<ArrayList<Producto>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Producto>> call, Response<ArrayList<Producto>> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Response Err: ", response.message());
                        } else {
                            productos = response.body();
                            MainActivity.carrito.productos = productos;
                            CarritoActivity.repintarAdapter();
                            MainActivity.repintarAdapter();
                            CarritoActivity.calcularPrecio();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Producto>> call, Throwable t) {
                        Log.e("Throw Err: ", t.getMessage());
                    }
                }
        );
    }
}
