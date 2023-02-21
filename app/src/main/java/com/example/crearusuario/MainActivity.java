package com.example.crearusuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText etId,etAsunto,etActividad,etFecha;
    Button btnGuardar,btnConsultar;
    TextView tvId,tvFecha,tvAsunto,tvActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = findViewById(R.id.etid);
        etAsunto = findViewById(R.id.etAsunto);
        etActividad = findViewById(R.id.etActividad);
        etFecha = findViewById(R.id.etfecha);
        tvId = findViewById(R.id.tvid);
        tvFecha = findViewById(R.id.tvfecha);
        tvAsunto = findViewById(R.id.tvasunto);
        tvActividad = findViewById(R.id.tvactividad);
        btnGuardar = findViewById(R.id.btnGuarda);
        btnConsultar = findViewById(R.id.btnConsultar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Dato Guardado", Toast.LENGTH_SHORT).show();
                new CargarDatos().execute("http://10.0.2.2/WebService/GuardarDatos.php?fecha="
                        + etFecha.getText().toString() + "&asunto=" + etAsunto.getText().toString()
                        + "&actividad=" + etActividad.getText().toString());
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConsultarDatos().execute("http://10.0.2.2/WebService/MostrarDatos.php?id="+ etId.getText().toString());
            }
        });

    }

    public String descargarUrl(String myUrl) throws IOException{
        InputStream is = null;
        int len = 500;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000);
            conn.setConnectTimeout(1500);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = readIt(is,len);
            return contentAsString;

        }finally {
            if (is !=null){
                is.close();
            }
        }
    }

    public String readIt(InputStream stream,int len) throws IOException{
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);

        return new String(buffer);
    }

    private class CargarDatos extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return descargarUrl(urls[0]);
            }catch (IOException e){
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private class ConsultarDatos extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return descargarUrl(urls[0]);
            }catch (IOException e){
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            JSONArray ja = null;
            try {
                ja = new JSONArray(result);
                tvId.setText("id: " + ja.getString(0));
                tvFecha.setText("fecha: " + ja.getString(1));
                tvAsunto.setText("asunto: " + ja.getString(2));
                tvActividad.setText("actividad: " + ja.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}