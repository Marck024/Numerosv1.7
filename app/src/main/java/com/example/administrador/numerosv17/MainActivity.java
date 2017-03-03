package com.example.administrador.numerosv17;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import static java.lang.Math.abs;
import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private Button btnVer,btnLimpiar,btnSalir;
    private EditText numero,vista;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numero=(EditText)findViewById(R.id.editText);
        vista=(EditText)findViewById(R.id.editText2);

        tts=new TextToSpeech(this,this);

        //boton de pasar numeros a letras
        btnVer=(Button)findViewById(R.id.button);
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logica de negocio***

                String num = numero.getText().toString();
                String palabra = "";
                String res="";
                float numero;

                if(!num.equals("") && !num.equals(".")){
                    numero=Float.parseFloat(num);
                    if(numero>=0 && numero<1000){
                        int entero=0,decimal;
                        String dec;
                        entero=(int)numero;

                        if(num.indexOf('.')>=0){
                            dec=num.substring(num.indexOf('.')+1,num.length());
                            //Toast.makeText(MainActivity.this,,Toast.LENGTH_SHORT).show();
                            if(dec.length()<=2){
                                decimal=(int)round((numero-entero)*100);
                                palabra=traduceNum(entero)+" punto "+traduceNum(decimal);
                            }else{
                                Toast.makeText(MainActivity.this,"Ingrese maximo a 2 decimales",Toast.LENGTH_SHORT).show();
                                palabra="Ingrese maximo a dos decimales";
                            }
                        }else{
                            palabra=traduceNum(entero);
                        }
                        vista.setText(palabra);
                    }else{
                        Toast.makeText(MainActivity.this,"Ingrese numero mayor a 0 y menor a 1000",Toast.LENGTH_SHORT).show();
                        vista.setText("Ingrese numero mayor a cero o menor a mil");
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Ingrese un valor",Toast.LENGTH_SHORT).show();
                    vista.setText("Ingrese un valor");
                }

                tts.setLanguage(new Locale("SPA"));
                if(!vista.getText().toString().equals("")){
                    hablar(vista.getText().toString());
                }
            }

        });

        //boton de limpiar
        btnLimpiar=(Button)findViewById(R.id.button3);
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numero.setText("");
                vista.setText("");
            }
        });

        //boton de salida
        btnSalir=(Button)findViewById(R.id.button2);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String traduceNum(int s) {
        int n = s;
        int ban = 0;
        String palabra = "";
        String[] unidades = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};
        String[] decenas = {"diez", "once", "doce", "trece", "catorce", "quince", "deciseis", "decisiete", "deciocho", "decinueve"};
        String[] Decenas = {"", "diez", "venti", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa"};
        String[] Centenas = {"", "ciento", "docientos", "trecientos", "cuatrocientos", "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"};

        if (n == 0) {
            palabra=unidades[n];
        } else {
            if (n == 100) {
                palabra = "cien ";
                n = abs(n - 100);
            } else if (n > 100) {
                palabra = Centenas[(int) (n / 100)] + " ";
                n = abs(n - (((int)(n/100))*100));
            }

            if (n >= 30 && n < 100) {
                int x=n-((n/10)*10);
                if(x==0){
                    palabra = palabra + Decenas[(int) (n / 10)] + " ";
                }else{
                    palabra = palabra + Decenas[(int) (n / 10)] + " y ";
                }
                n = abs(n - (((int) (n / 10)) * 10));
            } else if (n >= 21 && n < 30) {
                palabra = palabra + "veinti";
                n = abs(n - 20);
            } else if (n == 20) {
                palabra = palabra + "veinte ";
                n = abs(n - 20);
            } else if (n > 10 && n < 20) {
                palabra = palabra + decenas[(int)(n-10)];
            }else if(n==10){
                palabra=palabra+"Diez ";
            }

            if(n>0 && n<10){
                palabra=palabra+unidades[n];
            }
        }

        /*if (n > 0 && n < 1000) {
            if (n > 100) {
                if (n == 100) {
                    palabra = "cien";
                } else {
                    palabra = palabra + Centenas[n / 100] + " ";
                }
                n = n - (n/100)*100;
            }
            if (n > 20&& n<100) {
                palabra = palabra + Decenas[n / 10] + " ";
                if ((n % 10) != 0) {
                    if(n<=20&&n>=30){
                        palabra = palabra + "y ";
                    }
                }else{
                    ban=1;
                }
                n = n - (n / 10)*10;
            }
            if (n == 20) {
                palabra = palabra + "veinte" + " ";
            }else if (n >= 10 && n < 20) {
                palabra = palabra + decenas[n - 10] + " ";
            } else {
                if(ban!=1 && n<10){
                    palabra = palabra + unidades[n];
                }
            }
        } else if (n == 0) {
            palabra = "cero";
        }*/

        return palabra;
    }

    private void hablar(String s) {
        tts.speak(s,TextToSpeech.QUEUE_FLUSH,null);
        tts.setSpeechRate(0.0f);
        tts.setPitch(0.0f);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED){
            Toast.makeText(MainActivity.this,"Problemas con el hardware de sonido o perdida de datos",Toast.LENGTH_SHORT).show();
            Log.e("TTS_Numero","Problemas con el hardware de sonido o perdida de datos");
        }
    }

    @Override
    protected void onDestroy(){
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
