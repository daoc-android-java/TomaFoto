package ec.edu.ute.tomafoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Usar cámara del sistema
 * https://developer.android.com/training/camera/photobasics
 */
public class MainActivity extends AppCompatActivity {
    static final int REQ_FILE_LOCATION = 1;
    static final int REQ_TAKE_PICT = 2;
    static Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     Toma foto y la guarda en una ubicación definida por el usuario
     https://developer.android.com/training/data-storage/shared/documents-files
     */
    public void guardarFoto(View view) {
        //Intent para solicitar creación de un nuevo documento para foto
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        String mimeType = "image/jpg";
        intent.setType(mimeType);

        String filename = "JPG_"+System.currentTimeMillis() + ".jpg";
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        //Pide confirmación al usuario sobre dónde guardar la foto
        startActivityForResult(intent, REQ_FILE_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == REQ_FILE_LOCATION) {
                //Recupera la ubicación que el usuario confirmó para guardar ahí la foto
                imageUri = data.getData();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //OJO: segunda llamada para tomar la foto
                startActivityForResult(intent, REQ_TAKE_PICT);
            } else if (requestCode == REQ_TAKE_PICT) {
                //Presenta la imagen tomada en la pantalla
                try {
                    Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show();
                    System.out.println("(Ubicación de la imagen) " + imageUri);
                    ImageView imageView = findViewById(R.id.imageView);
                    //Recupera la imagen del disco
                    //imageView.setImageURI(imageUri); //Esto es lo más directo, pero... si la imagen es muy grande NO carga
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    //Redimensiona la imagen para que cargue sin problemas
                    int scrWidth = Resources.getSystem().getDisplayMetrics().widthPixels - 20; //20 es un padding cualquiera
                    float imgRate = (float) bmp.getWidth() / bmp.getHeight();
                    Bitmap resizedBmp = Bitmap.createScaledBitmap(bmp, scrWidth, (int) (scrWidth / imgRate), false);
                    imageView.setImageBitmap(resizedBmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

