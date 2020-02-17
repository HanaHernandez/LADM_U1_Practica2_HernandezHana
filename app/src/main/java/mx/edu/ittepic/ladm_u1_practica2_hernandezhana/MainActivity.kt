package mx.edu.ittepic.ladm_u1_practica2_hernandezhana

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //PERMISOS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){ //preguntar si esta solicitado el servicio Manifest android
            //ENTRA SI EL PERMISO ESTA DENEGADO
            //El siguiente codigo los solicita
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),0)

        }else
            mensaje("PERMISOS YA OTORGADOS")

        //Boton Guardar
        button3.setOnClickListener {
            if(rbInt.isChecked == true){
                guardarArchivoSD()

            }
            if(rbSD.isChecked == true){
                guardarArchivo()
            }
        }

        //Boton abrir
        button4.setOnClickListener {
            if (rbInt.isChecked == true){
                leerArchivo()
            }
            if (rbSD.isChecked == true){
                leerArchivoSD()
            }
        }
    }

    //Funcion Mensaje
    fun mensaje (m : String){
        AlertDialog.Builder(this)
            .setTitle("Atencion").setMessage(m).setPositiveButton("OK"){d, i ->}.show()
    }
//**********************EXTERNO**********************************************************
    //No SD
    fun noSD() : Boolean{
        //ENVIROMENT ES ESTATICA Y SOLO ESTA DEDICADA A MEMORIA EXTERNA
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    //Guardar En Archivo Externo
    fun guardarArchivoSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }

        try {

            var rutaSD = Environment.getExternalStorageDirectory()
            var nDirectorio = editText2.text.toString()
            var datosArchivo = File(rutaSD.absolutePath, nDirectorio)

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = editText.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("¡EXITO! Se guardo correctamente ")
            ponerTextos("")
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }

    //Leer SD
    fun leerArchivoSD(){
        if (noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var nDirectorio = editText2.text.toString()
            var datosArchivo = File(rutaSD.absolutePath,nDirectorio)


            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data = flujoEntrada.readLine()  //lee linia completa hsat q encuentra un enter o
            var vector = data.split("&") //lee hasta que encuentra &

            ponerTextos(vector[0])
            flujoEntrada.close()

        }catch (error:IOException){
            mensaje(error.message.toString())
        }
    }

//****************************Interno****************************************************
    fun guardarArchivo(){
        try {
            var nDirectorio = editText2.text.toString()
            var flujoSalida = OutputStreamWriter(openFileOutput(nDirectorio, Context.MODE_PRIVATE))

            var data = editText.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

             mensaje("¡EXITO! Se guardo correctamente ")
             ponerTextos("")
        }catch (error : IOException){
             mensaje(error.message.toString())
        }
    }

    fun leerArchivo(){
        try {
            var nDirectorio = editText2.text.toString()
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(nDirectorio)))

            var data = flujoEntrada.readLine()  //lee linia completa hsat q encuentra un enter o
            var vector = data.split("&") //lee hasta que encuentra &

            ponerTextos(vector[0])
            flujoEntrada.close()

        }catch (error:IOException){
            mensaje(error.message.toString())
        }
    }
//***************************************************************************************
    //Poner txt
    fun ponerTextos(t1:String){
        editText.setText(t1)

    }

}
