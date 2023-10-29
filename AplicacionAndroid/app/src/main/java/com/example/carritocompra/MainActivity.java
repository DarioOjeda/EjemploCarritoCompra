package com.example.carritocompra;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carritocompra.interfaces.CRUDInterface;
import com.example.carritocompra.models.Carrito;
import com.example.carritocompra.models.Producto;
import com.example.carritocompra.utils.constants.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carritocompra.databinding.ActivityMainBinding;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    static ListView list_articulos;
    static Button boton_carrito;
    public static List<Producto> productos = new ArrayList<>();
    CRUDInterface crudInterface;

    public static Carrito carrito;

    public static void repintarAdapter() {
        boton_carrito.setText(String.valueOf(carrito.getProductos().size()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pintamos la vista principal
        setContentView(R.layout.activity_main);

        // list_articulos muestra todos los artículos del catálogo:
        list_articulos = (ListView) findViewById(R.id.list_articulos);

        // Como es un listview, necesita un adapter que pinte cada elemento. Como es muy
        // Simple en este ejemplo, el adapter está definido en este mismo archivo:
        list_articulos.setAdapter(new list_itemsAdapter(this.getApplicationContext()));

        boton_carrito = (Button) findViewById(R.id.boton_carrito);


        boton_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(v.getContext(), CarritoActivity.class);
                i.putExtra("carrito", carrito);
                startActivityForResult(i, 1);
            }
        });

        getAll();

    }

    private void getAll() {
        // Hay que modificar el archivo Constanst.java para indicar la ip del equipo actual, de lo contrario, no funcionará
        // la aplicación
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        crudInterface = retrofit.create(CRUDInterface.class);
        Call<List<Producto>> call = crudInterface.obtenerListaCompletaProductos();

        // Utilizo enqueue para llamar el call porque es asíncrona (no sobrecarga tanto el sistema)
        call.enqueue(
                new Callback<List<Producto>>() {
                    @Override
                    public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Response Err: ", response.message());
                        } else {
                            productos = response.body();
                            ((BaseAdapter) list_articulos.getAdapter()).notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Producto>> call, Throwable t) {
                        Log.e("Throw Err: ", t.getMessage());
                    }
                }
        );

        Call<Carrito> call2 = crudInterface.obtenerListaCarritoPorUsuario("1");

        // Utilizo enqueue para llamar el call porque es asíncrona (no sobrecarga tanto el sistema)
        call2.enqueue(
                new Callback<Carrito>() {
                    @Override
                    public void onResponse(Call<Carrito> call, Response<Carrito> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Response Err: ", response.message());
                        } else {
                            carrito = response.body();
                            //Log.i("Carrito: ", carrito.getProductos().get(0).toString());
                            boton_carrito.setText(String.valueOf(carrito.getProductos().size()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Carrito> call, Throwable t) {
                        Log.e("Throw Err: ", t.getMessage());
                    }
                }
        );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            repintarAdapter();
        } catch (Exception ex) {
        }

    }



    public class list_itemsAdapter extends BaseAdapter
    {
        private Context mContext;
        public list_itemsAdapter(Context c) {
            mContext = c;
        }

        // El adapter depende de productos, que, a su vez, se carga con la Api (ver archivo CRUDInterface):
        public int getCount() {
            return productos.size();
        }
        public Object getItem(int position)
        {
            return position;
        }
        public long getItemId(int position)
        {
            return position;
        }
        public class ViewHolder
        {
            TextView nombre_precio;
            ImageView imagen_articulo;
            Button boton_anadir;
        }

        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            try{
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.articulo_item, null);

                    holder = new ViewHolder();
                    holder.nombre_precio = (TextView) convertView.findViewById(R.id.tv_nombre_precio);
                    holder.imagen_articulo = (ImageView) convertView.findViewById(R.id.imagen_articulo);
                    holder.boton_anadir = (Button) convertView.findViewById(R.id.boton_anadir);

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.nombre_precio.setText(productos.get(position).getNombre() + " " + String.format("%.2f", productos.get(position).getPrecio()) + "€");

                InputStream ims = getAssets().open(productos.get(position).getImagen());
                Drawable d = Drawable.createFromStream(ims, null);
                holder.imagen_articulo.setImageDrawable(d);
                ims .close();

                holder.boton_anadir.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                carrito.añadirArticulo(productos.get(position).getId());
                            }
                        }
                );

            } catch (Exception e){
                Log.e("tag", e.getMessage());
            }
            return convertView;
        }
    }
}