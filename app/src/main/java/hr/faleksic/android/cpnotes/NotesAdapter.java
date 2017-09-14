package hr.faleksic.android.cpnotes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cpnotes.R;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;


class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private List<Note> notesList;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int id;
        public TextView title, content, category;
        private ImageButton copy;

        MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            category = (TextView) view.findViewById(R.id.category);
            copy = (ImageButton) view.findViewById(R.id.image_button_copy) ;
            view.setOnClickListener(this);
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setPrimaryClip(ClipData.newPlainText("content", content.getText()));
                    Toast.makeText(view.getContext(), view.getContext().getString(R.string.note_content_added), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), CreateNoteActivity.class);
            intent.putExtra("id", id);
            view.getContext().startActivity(intent);
        }
    }

    NotesAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.id = note.getId();
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
        holder.category.setText(note.getCategory());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
