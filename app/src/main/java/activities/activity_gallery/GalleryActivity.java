package activities.activity_gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.windspeeddeductiontool.R;

public class GalleryActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        imageView = findViewById(R.id.image_view_PutImageHereNigga);

        Intent bitmapIntent = getIntent();
        Bitmap bitmap = bitmapIntent.getParcelableExtra("bitmap");
        imageView.setImageBitmap(bitmap);
    }
}
