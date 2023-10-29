package com.example.carritocompra;

import static com.example.carritocompra.MainActivity.productos;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carritocompra.models.Carrito;
import com.example.carritocompra.models.Producto;

import java.io.IOException;
import java.io.InputStream;

public class CarritoActivity extends AppCompatActivity {

    static RecyclerView lista_carritos_rv;

    static TextView texto_dinero_calculado;
    static Carrito carrito;

    public static void repintarAdapter() {
        ((CustomAdapter) lista_carritos_rv.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito_layout);
        // Uso ahora un RecyclerView para poder poner la lista del catálogo horizontal:
        lista_carritos_rv = (RecyclerView) findViewById(R.id.lista_carritos_rv);

        // El carrito viene por el intent, para eso he hecho la clase serializable:
        carrito = (Carrito) getIntent().getSerializableExtra("carrito");

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        lista_carritos_rv.setLayoutManager(layoutManager);
        lista_carritos_rv.setAdapter( new CustomAdapter());

        texto_dinero_calculado = (TextView) findViewById(R.id.texto_dinero_calculado);

        calcularPrecio();

    }

    public static void calcularPrecio() {
        float dinero = 0;

        for(Producto pro: carrito.getProductos()) {
            dinero += pro.getPrecio();
        }
        texto_dinero_calculado.setText("Dinero: " + String.format("%.2f", dinero) + "€");
    }


    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {



        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView tv_nombre;
            private final ImageView imagen_articulo;
            private final Button boton_borrar;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                tv_nombre = (TextView) view.findViewById(R.id.tv_nombre);
                imagen_articulo = (ImageView) view.findViewById(R.id.imagen_articulo);
                boton_borrar = (Button) view.findViewById(R.id.boton_borrar);
            }

            public TextView getTextView() {
                return tv_nombre;
            }

            public ImageView getImagenView() {
                return imagen_articulo;
            }

            public Button getButtonView() {
                return boton_borrar;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.carrito_item, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            viewHolder.getTextView().setText(carrito.getProductos().get(position).getDescripcion());

            InputStream ims = null;
            try {
                ims = getAssets().open(carrito.getProductos().get(position).getImagen());
            } catch (IOException e) {
                //throw new RuntimeException(e);
            }
            Drawable d = Drawable.createFromStream(ims, null);
            viewHolder.getImagenView().setImageDrawable(d);

            viewHolder.getButtonView().setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            carrito.borrarArticuloDelCarrito(carrito.getProductos().get(position).getId(), position);
                        }
                    }
            );
        }
        @Override
        public int getItemCount() {
            return carrito.getProductos().size();
        }
    }
}