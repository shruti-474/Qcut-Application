package com.mountreachsolution.qcut;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.io.OutputStream;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private ImageView userImage;
    private Button btnUpload, btnStyle1, btnStyle2, btnSave;
    private Bitmap selectedBitmap;
    private Bitmap finalBitmap;

    private ActivityResultLauncher<Intent> imagePicker;

    private static final int STORAGE_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        userImage = findViewById(R.id.userImage);
        btnUpload = findViewById(R.id.btnUpload);
        btnStyle1 = findViewById(R.id.btnStyle1);
        btnStyle2 = findViewById(R.id.btnStyle2);
        btnSave = findViewById(R.id.btnSave);

        // Image picker
        imagePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            userImage.setImageBitmap(selectedBitmap);
                            finalBitmap = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePicker.launch(intent);
        });

        btnStyle1.setOnClickListener(v -> applyHairStyle(R.drawable.hair45));
        btnStyle2.setOnClickListener(v -> applyHairStyle(R.drawable.hair50));

        btnSave.setOnClickListener(v -> {
            if (finalBitmap != null) {
                checkPermissionAndSave(finalBitmap);
            } else {
                Toast.makeText(this, "Apply a hairstyle first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyHairStyle(int hairResId) {
        if (selectedBitmap != null) {
            detectFaceAndApply(selectedBitmap, hairResId);
        } else {
            Toast.makeText(this, "Upload an image first", Toast.LENGTH_SHORT).show();
        }
    }

    private void detectFaceAndApply(Bitmap bitmap, int hairResId) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .build();

        FaceDetector detector = FaceDetection.getClient(options);

        detector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        finalBitmap = overlayHair(bitmap, faces, hairResId);
                        userImage.setImageBitmap(finalBitmap);
                        Toast.makeText(this, "Hairstyle applied", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No face detected", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(this, "Face detection failed", Toast.LENGTH_SHORT).show();
                });
    }

    private Bitmap overlayHair(Bitmap original, List<Face> faces, int hairResId) {
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(original, 0, 0, null);

        Bitmap hairBitmap = BitmapFactory.decodeResource(getResources(), hairResId);

        for (Face face : faces) {
            PointF leftEye = getLandmark(face, FaceLandmark.LEFT_EYE);
            PointF rightEye = getLandmark(face, FaceLandmark.RIGHT_EYE);
            PointF noseBase = getLandmark(face, FaceLandmark.NOSE_BASE);

            float angle = 0f;
            if (leftEye != null && rightEye != null) {
                angle = (float) Math.toDegrees(Math.atan2(rightEye.y - leftEye.y, rightEye.x - leftEye.x));
            }

            // Scale hair image larger to cover full head
            float faceHeight = face.getBoundingBox().height();
            float faceWidth = face.getBoundingBox().width();
            float scaleFactor = (faceWidth + faceHeight) / 2f / hairBitmap.getWidth() * 2.2f; // Slightly reduced for better fit

            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor, scaleFactor);
            matrix.postRotate(angle);

            Bitmap scaledHair = Bitmap.createBitmap(hairBitmap, 0, 0, hairBitmap.getWidth(), hairBitmap.getHeight(), matrix, true);

            // Position hair just above forehead
            int hairX = face.getBoundingBox().centerX() - scaledHair.getWidth() / 2;
            int hairY;
            if (leftEye != null && rightEye != null) {
                float eyeMidY = (leftEye.y + rightEye.y) / 2;
                hairY = (int) (eyeMidY - scaledHair.getHeight() * 0.65f); // Perfect forehead alignment
            } else if (noseBase != null) {
                hairY = (int) (noseBase.y - scaledHair.getHeight() * 0.95f);
            } else {
                hairY = face.getBoundingBox().top - scaledHair.getHeight() / 2;
            }

            // Apply hairstyle with full opacity
            Paint paint = new Paint();
            paint.setAlpha(255);
            canvas.drawBitmap(scaledHair, hairX, hairY, paint);
        }

        return result;
    }


    private PointF getLandmark(Face face, int landmarkType) {
        if (face.getLandmark(landmarkType) != null) {
            return face.getLandmark(landmarkType).getPosition();
        }
        return null;
    }

    private void checkPermissionAndSave(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                saveImage(bitmap);
            }
        } else {
            saveImage(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (finalBitmap != null) saveImage(finalBitmap);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImage(Bitmap bitmap) {
        try {
            String filename = "Haircut_" + System.currentTimeMillis() + ".png";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/HaircutApp");

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                OutputStream out = getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                if (out != null) out.close();
                Toast.makeText(this, "Saved to gallery", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }
}
