package hr.faleksic.android.cpnotes;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.android.cpnotes.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyCategoryViewHolder> {

    private List<Category> categoryList;
    private CategoryFragment categoryFragment;

    public class MyCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int id;
        public TextView name, count;
        public CheckBox checkBox;

        public MyCategoryViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            count = (TextView) view.findViewById(R.id.category_count);
            checkBox = (CheckBox) view.findViewById(R.id.delete_checkbox);
            view.findViewById(R.id.note_image).setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (id > 1) {
                        CategoryDialog categoryDialog = new CategoryDialog();
                        Bundle args = new Bundle();
                        args.putString("name", name.getText().toString());
                        args.putInt("id", id);
                        categoryDialog.setArguments(args);
                        categoryDialog.show(((Activity) (view.getContext())).getFragmentManager(), "category");
                        categoryDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                categoryFragment.refresh();
                            }
                        });
                    }
                }
            });

            view.setOnLongClickListener(categoryFragment);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("id", id);
            view.getContext().startActivity(intent);
        }
    }

    public CategoriesAdapter(List<Category> categoryList, CategoryFragment fragment) {
        this.categoryList = categoryList;
        this.categoryFragment = fragment;
    }

    @Override
    public MyCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_row, parent, false);
        return new MyCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyCategoryViewHolder holder, int position) {
        final Category category = categoryList.get(position);
        holder.id = category.getId();
        holder.name.setText(category.getName());
        holder.count.setText(String.valueOf(category.getCount()));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category.setSelected(!category.isSelected());
            }
        });
        if(!categoryFragment.showCheckboxes) {
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            if(holder.checkBox.isChecked()) {
                holder.checkBox.toggle();
            }
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
