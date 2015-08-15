package apfelin.qrbeer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private Button generateCode, scanCode;
    private String scanContent;
    private TextView scanQRText;
    private ArrayList<User> users;
    private ArrayList<String> QRCodes;
    private ArrayList<Integer> ages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanQRText = (TextView) findViewById(R.id.scan_qrtext);
        QRCodes = new ArrayList<>();
        ages = new ArrayList<>();

        users = new ArrayList<>();
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("participants", null);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        users = gson.fromJson(json, type);

        generateCode = (Button) findViewById(R.id.generate_code);
        generateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent generateIntent = new Intent(MainActivity.this, GeneratePage.class);

                startActivityForResult(generateIntent, 100);
            }
        });

        scanCode = (Button) findViewById(R.id.scan_code);
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);

                scanIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 100) {

            if (resultCode == 1) {

                QRCodes = intent.getStringArrayListExtra("usersQR");
                ages = intent.getIntegerArrayListExtra("age");

                addUser(QRCodes, ages);
            }
        }
        else {

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode,
                    resultCode, intent);

            if (scanningResult.getContents() != null) {

                boolean scanned = false;

                scanContent = scanningResult.getContents();

                scanQRText.setText("QR CODE: " + scanContent);

                for (int i = 0; i < users.size(); i++) {

                    if(scanContent.matches(users.get(i).QRCode)) {

                        scanned = true;

                        if(users.get(i).beers >= 0) {

                            users.get(i).beers--;

                            Toast.makeText(getApplicationContext(),
                                    "Utilizatorul cu codul " + users.get(i).QRCode +
                                            " mai are " + users.get(i).beers + " ramase.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if (users.get(i).beers == 0) {

                            Toast.makeText(getApplicationContext(),
                                    "Utilizatorul cu codul " + users.get(i).QRCode +
                                            " nu mai are beri in cont!", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            Toast.makeText(getApplicationContext(),
                                    "Utilizatorul cu codul " + users.get(i).QRCode +
                                            " nu are numarul de beri setat corect!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if (!scanned) {

                    Toast.makeText(getApplicationContext(),
                            "Utilizatorul cu codul " + scanContent + " nu exista!",
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(getApplicationContext(),
                        "Nu a fost scanat nimic!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addUser(ArrayList<String> _QRCode, ArrayList<Integer> _age) {

        for (int i = 0; i < _QRCode.size(); i++) {

            User user = new User(_age.get(i));

            user.QRCode = _QRCode.get(i);
            user.age = _age.get(i);

            users.add(user);
        }
    }

    /*private void saveUsers(Context ctx) {

        SharedPreferences sharedPrefs = ctx.getSharedPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(users);

        editor.putString("participants", json);
        editor.commit();
    }

    private void retrieveUsers(Context ctx) {

        SharedPreferences sharedPrefs = ctx.getSharedPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("participants", null);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        users = gson.fromJson(json, type);
    }*/

    @Override
    public void onStart ()
    {
        super.onStart();
    }

    @Override
    public void onRestart ()
    {
        super.onRestart();
    }

    @Override
    public void onResume ()
    {
        super.onResume();
    }

    @Override
    public void onPause ()
    {
        super.onPause();
    }

    @Override
    public void onStop ()
    {
        super.onStop();
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(users);

        editor.putString("participants", json);
        editor.commit();
    }

    @Override
    public void onDestroy ()
    {
        super.onDestroy();
    }

    // functii folosite pentru salvarea si restaurarea starii

    @Override
    public void onSaveInstanceState (Bundle outState)
    {
        // apelarea functiei din activitatea parinte este recomandata, dar nu obligatorie
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState (Bundle inState)
    {
        // apelarea functiei din activitatea parinte este recomandata, dar nu obligatorie
        super.onRestoreInstanceState(inState);
    }
}
