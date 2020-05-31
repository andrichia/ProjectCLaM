package id.ac.umn.projectclam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity";
    private static final int REQUEST_CODE = 101;
    private static FirebaseFirestore db;
//    private static FusedLocationProviderClient fusedLocationProviderClient;
    private static boolean gps_enabled;
    private static boolean network_enabled;
    private static Location currentlocation;
    private static double longitude;
    private static double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Log.d(TAG, "pre-db");
        db = FirebaseFirestore.getInstance();
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public void warning(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Map<String, Object> report = new HashMap<>();

        final Context context = this;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = false;
        network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(context)
                    .setMessage("Location service is not enabled")
                    .setPositiveButton("Turn on Location", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null){
//                    currentlocation = location;
//                    longitude = currentlocation.getLongitude();
//                    latitude = currentlocation.getLatitude();
//                }
//            }
//        });
//        GeoPoint curr = new GeoPoint(latitude, longitude);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        assert telephonyManager != null;
        String phone = telephonyManager.getDeviceId();
        report.put("latitude", latitude);
        report.put("longitude", longitude);
//        report.put("location", curr);
        report.put("phone", phone);
        report.put("status", "yellow");

        if (db.collection("report").document(phone).get().equals(null)) {
            Toast.makeText(getApplicationContext(), "Laporan gagal di masukkan", Toast.LENGTH_SHORT).show();
        } else {
            db.collection("report").document(phone).set(report);
            Toast.makeText(getApplicationContext(), "Laporan berhasil di masukkan", Toast.LENGTH_SHORT).show();
        }
    }

}
