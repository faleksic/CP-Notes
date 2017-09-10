package hr.faleksic.android.cpnotes;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.cpnotes.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private MyDBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        myDBHelper = new MyDBHelper(this);

        notesAdapter = new NotesAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(notesAdapter);

        prepareNoteData();
    }

    private void prepareNoteData() {
        myDBHelper.insertNote("Naslov1", "Sadržaj1", 0);
        myDBHelper.insertNote("Naslov2", "Sadržaj2", 0);
        myDBHelper.insertNote("Naslov3", "Sadržaj3", 0);

        Cursor cursor = myDBHelper.getAllNotes();

        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.NOTE_COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.NOTE_COLUMN_CONTENT));
            String category =  cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.NOTE_COLUMN_CATEGORY));
            noteList.add(new Note(title, content, category));
        }
        cursor.close();

        notesAdapter.notifyDataSetChanged();
    }
}
