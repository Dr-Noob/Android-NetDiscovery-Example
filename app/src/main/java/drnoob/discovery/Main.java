package drnoob.discovery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {
    private Button btnRegister;
    private Button btnDiscover;
    private Button btnRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnRegister = findViewById(R.id.btnRegister);
        this.btnDiscover = findViewById(R.id.btnDiscover);
        this.btnRecycler = findViewById(R.id.btnRecycler);

        this.btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Discovery di = Discovery.getOnlyInstance(getApplicationContext());
                if(di.register()) {
                    Toast t = Toast.makeText(getApplicationContext(), "Register succeeded", Toast.LENGTH_SHORT);
                    t.show();
                }
                else {
                    Toast t = Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        this.btnDiscover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Discovery di = Discovery.getOnlyInstance(getApplicationContext());
                if(di.discover()) {
                    Toast t = Toast.makeText(getApplicationContext(), "Discover succeeded", Toast.LENGTH_SHORT);
                    t.show();
                }
                else {
                    Toast t = Toast.makeText(getApplicationContext(), "Discover failed", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        this.btnRecycler.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Main.this, Recycler.class);
                startActivity(myIntent);
            }
        });
    }
}
