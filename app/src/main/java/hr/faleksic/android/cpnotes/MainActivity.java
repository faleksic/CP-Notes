package hr.faleksic.android.cpnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.cpnotes.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private MyDBHelper myDBHelper;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        myDBHelper = new MyDBHelper(this);

        notesAdapter = new NotesAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(notesAdapter);

        prepareNoteData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivity(i);
            }
        } );
    }

    private void prepareNoteData() {

        Cursor cursor = myDBHelper.getAllNotes();

        if(cursor.getCount() == 0) {
            myDBHelper.insertCategory("None");
            myDBHelper.insertNote("Title", "Content", 1);

            cursor = myDBHelper.getAllNotes();
        }

        while(cursor.moveToNext()) {
            String title = cursor.getString(0);
            String content = cursor.getString(1);
            String category =  cursor.getString(2);
            noteList.add(new Note(title, content, category));
        }

        cursor.close();

        notesAdapter.notifyDataSetChanged();
    }
}
