package ec.edu.ute.tomafoto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Usar camara del sistema
 * https://developer.android.com/training/camera/photobasics
 */
public class MainActivity extends AppCompatActivity {
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     Recupera un pequeño preview de la foto tomada
     */
    public void tomarPreview(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    /**
     Toma foto y la guarda en Public storage
     (tambin recupera el preview)
     */
    public void guardarFoto(View view) {
        URI picturesDirUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toURI();
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        String mimeType = "image/jpg";
        String filename = "JPG_"+System.currentTimeMillis() + ".jpg";
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, picturesDirUri);
        startActivityForResult(intent, 2);
    }

    /**
     Aqui se recupera el preview y se toma la foto
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 1) {
            //recupera un "preview" de la foto
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bmp);
        }

        if(resultCode == RESULT_OK && requestCode == 2) {
            //Toma la foto tamaño completo
            Uri uri = data.getData();
            System.out.println(uri);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, 3);
        }
    }

}

