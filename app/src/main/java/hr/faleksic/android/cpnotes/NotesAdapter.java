package hr.faleksic.android.cpnotes;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.cpnotes.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private List<Note> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, category;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            category = (TextView) view.findViewById(R.id.category);
        }
    }

    public NotesAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
        holder.category.setText(note.getCategory());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
