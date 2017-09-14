package hr.faleksic.android.cpnotes;

import android.content.ClipboardManager;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.cpnotes.R;

import java.util.ArrayList;
import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {
    private MyDBHelper myDBHelper;
    private int id = 0;
    private EditText title, content;
    private AutoCompleteTextView autoCompleteTextView;
    private ImageButton pasteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        myDBHelper = new MyDBHelper(this);

        title = (EditText) findViewById(R.id.title_edittext);
        content = (EditText) findViewById(R.id.content_edittext);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_complete_category);
        pasteButton = (ImageButton) findViewById(R.id.paste_button);

        Cursor cursor = myDBHelper.getAllCategories();
        ArrayList<String> countries = new ArrayList<>();
        while(cursor.moveToNext()) {
            countries.add(cursor.getString(1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.category_autocomplete, countries);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });

        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasteContent();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getInt("id");
            cursor = myDBHelper.getNote(id);
            while(cursor.moveToNext()) {
                title.setText(cursor.getString(1));
                content.setText(cursor.getString(2));
                autoCompleteTextView.setText(cursor.getString(3));
            }
        }
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_note_menu, menu);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNote();
                return true;
            case R.id.action_delete:
                myDBHelper.deleteNote(id);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void saveNote() {
        if(!Objects.equals(content.getText().toString(), "")) {
            String category = autoCompleteTextView.getText().toString();
            if(!Objects.equals(category, "")) {
                if(myDBHelper.getCategoryId(category) == -1) {
                    myDBHelper.insertCategory(category);
                }
                if(id > 0) {
                    myDBHelper.updateNote(id, title.getText().toString(), content.getText().toString(), myDBHelper.getCategoryId(category));
                } else {
                    myDBHelper.insertNote(title.getText().toString(), content.getText().toString(), myDBHelper.getCategoryId(category));
                }
            } else {
                myDBHelper.insertNote(title.getText().toString(), content.getText().toString(), 1);
            }
        } else {
            Toast.makeText(this, getString(R.string.note_content_empty), Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void pasteContent() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        content.setText(clipboard.getPrimaryClip().getItemAt(0).getText());
        content.setSelection(content.getText().length());
    }
}

