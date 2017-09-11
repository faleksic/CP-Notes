package hr.faleksic.android.cpnotes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.cpnotes.R;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {

    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private MyDBHelper myDBHelper;
    private FloatingActionButton floatingActionButton;

    public NoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        myDBHelper = new MyDBHelper(getActivity());

        notesAdapter = new NotesAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(notesAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CreateNoteActivity.class);
                startActivity(i);
            }
        } );

        noteList.clear();
        notesAdapter.notifyItemRangeRemoved(0, notesAdapter.getItemCount());
        prepareNoteData();
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