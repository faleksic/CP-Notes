package hr.faleksic.android.cpnotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.cpnotes.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyCategoryViewHolder> {

    private List<Category> categoryList;

    public class MyCategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView name, count;

        public MyCategoryViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            count = (TextView) view.findViewById(R.id.category_count);
        }
    }

    public CategoriesAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public MyCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_row, parent, false);
        return new MyCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyCategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.name.setText(category.getName());
        holder.count.setText(String.valueOf(category.getCount()));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
