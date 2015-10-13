package xeaphii.com.smsalert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
    EditText UserName, Password;
    Button BtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        UserName= (EditText) findViewById(R.id.user_name);
        Password = (EditText) findViewById(R.id.password);
        BtLogin = (Button) findViewById(R.id.bt_login);
        BtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),UserName.getText()+":"+Password.getText(),Toast.LENGTH_LONG).show();
                if(UserName.getText().toString().trim().equals("admin")&&Password.getText().toString().trim().equals("123GNthenge!1234")){
                    Intent i = new Intent(MainActivity.this,AdminUser.class);
                    startActivity(i);
                    finish();
                }else  if(UserName.getText().toString().trim().equals("user")&&Password.getText().toString().trim().equals("user123")){
                    Intent i = new Intent(MainActivity.this,NormalUser.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Authentication failure",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
