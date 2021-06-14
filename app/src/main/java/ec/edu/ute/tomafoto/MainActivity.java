package ec.edu.ute.tomafoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
     https://developer.android.com/training/data-storage/shared/documents-files
     */
    public void guardarFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        String mimeType = "image/jpg";
        intent.setType(mimeType);

        String filename = "JPG_"+System.currentTimeMillis() + ".jpg";
        intent.putExtra(Intent.EXTRA_TITLE, filename);

        Uri picturesDirUri = Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
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
            if(data.getExtras() != null) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(bmp);
            } else {
                Toast.makeText(this, "RESULT_OK pero Intent.Bundle == null", Toast.LENGTH_LONG);
            }
        }

        if(resultCode == RESULT_OK && requestCode == 2) {
            //Toma la foto tamaño completo
            Uri uri = data.getData();
            System.out.println(uri);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, 1);
        }
    }

}

