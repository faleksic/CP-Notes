package hr.faleksic.android.cpnotes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
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
    private SwipeRefreshLayout swipeRefreshLayout;

    public NoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutNote);

        myDBHelper = new MyDBHelper(getActivity());

        notesAdapter = new NotesAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(notesAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        noteList.clear();
        notesAdapter.notifyItemRangeRemoved(0, notesAdapter.getItemCount());
        prepareNoteData();
    }

    private void prepareNoteData() {
        Cursor cursor;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");
            if (id > 0) {
                cursor = myDBHelper.getAllNotesWithCategory(id);
            } else {
                cursor = myDBHelper.getAllNotes();
            }
        } else {
            cursor = myDBHelper.getAllNotes();
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            String category = cursor.getString(3);
            noteList.add(new Note(id, title, content, category));
        }

        cursor.close();

        notesAdapter.notifyDataSetChanged();
    }
}
