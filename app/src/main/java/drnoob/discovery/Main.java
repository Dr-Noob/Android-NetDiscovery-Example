package drnoob.discovery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnStartRegister;
        Button btnStopRegister;
        Button btnStartDiscover;
        Button btnStopDiscover;
        Button btnRecycler;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartRegister = findViewById(R.id.btnStartRegister);
        btnStopRegister  = findViewById(R.id.btnStopRegister);
        btnStartDiscover = findViewById(R.id.btnStartDiscover);
        btnStopDiscover  = findViewById(R.id.btnStopDiscover);
        btnRecycler      = findViewById(R.id.btnRecycler);

        btnStartRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Discovery di = Discovery.getOnlyInstance(getApplicationContext());
                String msg;

                if(di.registerServiceUp())
                    msg = getString(R.string.register_ko);
                else {
                    msg = getString(R.string.register_ok);
                    di.register();
                }

                Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                t.show();
            }
        });

        btnStopRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Discovery di = Discovery.getOnlyInstance(getApplicationContext());
                String msg;

                if(di.registerServiceUp()) {
                    msg = getString(R.string.un_register_ok);
                    di.unregister();
                }
                else
                    msg = getString(R.string.un_register_ko);

                Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                t.show();
            }
        });

        btnStartDiscover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Discovery di = Discovery.getOnlyInstance(getApplicationContext());
                String msg;

                if(di.discoverServiceUp())
                    msg = getString(R.string.discover_ko);
                else {
                    msg = getString(R.string.discover_ok);
                    di.startDiscover();
                }

                Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                t.show();
            }
        });

        btnStopDiscover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Discovery di = Discovery.getOnlyInstance(getApplicationContext());
                String msg;

                if(di.discoverServiceUp()) {
                    msg = getString(R.string.un_discovery_ok);
                    di.stopDiscover();
                }
                else
                    msg = getString(R.string.un_discovery_ko);

                Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                t.show();
            }
        });

        btnRecycler.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Main.this, Recycler.class);
                startActivity(myIntent);
            }
        });
    }
}
