package hr.faleksic.android.cpnotes;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.android.cpnotes.R;

import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {
    private MyDBHelper myDBHelper;
    private EditText title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        myDBHelper = new MyDBHelper(this);

        title = (EditText) findViewById(R.id.title_edittext);
        content = (EditText) findViewById(R.id.content_edittext);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!Objects.equals(content.getText().toString(), "") || !Objects.equals(title.getText().toString(), "")) {
            myDBHelper.insertNote(title.getText().toString(), content.getText().toString(), 1);
        }
    }
}

