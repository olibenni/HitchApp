package com.myapp.hitch3;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.myapp.hitch3.util.API;

import java.io.IOException;

/**
 * Created by olafurma 3.3.2016
 *
 * This activity gets created when clicking a ride from Driver activity
 * It gets sent the id of the user that owns the requested ride from Driver activity.
 */
public class CreateMessage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_message);

        // Getting the display size of the device monitor
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width  = dm.widthPixels;
        int height = dm.heightPixels;

        // Making this activity look as a pop up window only covering 50% of the activity under it
        getWindow().setLayout((int) (width * 0.5), (int) (height * 0.5));
    }

    /**
     * Closes the activity
     */
    public void onCancel(View view) {
        finish();
    }

    /**
     * Sends the message from the text box to the user that was clicked on
     * when opening this activity
     * @throws IOException
     */
    public void onSend(View view) throws IOException {
        EditText inputText = (EditText) findViewById(R.id.inputText);

        // Get the id of the user that was included when creating the activity
        int id = getIntent().getIntExtra("ID", 0);
        API.sendMessage(inputText.getText().toString(), id);
    }

}
