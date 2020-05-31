package id.ac.umn.projectclam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteActivity extends AppCompatActivity {

    private static final String TAG = "DeleteActivity";
    private static final int REQUEST_CODE = 101;
    private static FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        db = FirebaseFirestore.getInstance();
        Context mContext = this;


    }

    public void delete(View v){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        assert telephonyManager != null;
        String phone = telephonyManager.getDeviceId();

        if(db.collection("report").document(phone).get().equals(null)){
            Toast.makeText(getApplicationContext(), "Laporan belum ada", Toast.LENGTH_SHORT).show();
        }
        else {
            db.collection("report").document(phone).delete();
            Toast.makeText(getApplicationContext(), "Laporan berhasil di hapus", Toast.LENGTH_SHORT).show();
        }

    }
}
