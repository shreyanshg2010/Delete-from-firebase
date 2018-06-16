package co.shrey.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button submit;
    EditText name;
    EditText address;
    EditText age;
    String a;
    String b;
    String c;
    DatabaseReference ref;
    DatabaseReference pushedPostRef;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("TAG", refreshedToken + "");
        submit = (Button) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        age = (EditText) findViewById(R.id.age);
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        ref=database.getReference();

//to display the name,address,age of the first activity by pushing the child to reference
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = name.getText().toString();
                b = address.getText().toString();
                c = age.getText().toString();
                pushedPostRef = ref.push();
                pushedPostRef.child("name").setValue(a);
                pushedPostRef.child("address").setValue(b);
                pushedPostRef.child("age").setValue(c);
                if (a != null && a.length()>0) {
                    name.setText("");
                    address.setText("");
                    age.setText("");


                }
                Toast.makeText(MainActivity.this, "Data Successfully Updated!", Toast.LENGTH_SHORT).show();
            }

        });

    }
    //for new activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_android:
                Intent intent = new Intent(MainActivity.this,ListItemActivity.class);
                startActivity(intent);
                // User chose the "Settings" item, show the app settings UI...
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
