package hr.faleksic.android.cpnotes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.cpnotes.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements View.OnLongClickListener{
    public boolean showCheckboxes = false;
    private List<Category> categoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CategoriesAdapter categoriesAdapter;
    private MyDBHelper myDBHelper;

    public CategoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_category);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutCategory);
        myDBHelper = new MyDBHelper(getActivity());
        categoriesAdapter = new CategoriesAdapter(categoryList, this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(categoriesAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        categoryList.clear();
        categoriesAdapter.notifyItemRangeRemoved(0, categoriesAdapter.getItemCount());
        prepareCategoryData();
    }

    private void prepareCategoryData() {

        Cursor cursor = myDBHelper.getAllCategories();

        categoryList.add(new Category(0, "All notes", myDBHelper.countAllCategories()));

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int count = cursor.getInt(2);
            categoryList.add(new Category(id, name, count));
        }
        cursor.close();

        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onLongClick(View view) {
        ((MainActivity)getActivity()).changeMenu(R.menu.delete_menu);
        showCheckboxes = true;
        categoriesAdapter.notifyDataSetChanged();
        return true;
    }

    public void stopDelete() {
        showCheckboxes = false;
        for(Category category : categoryList) {
            if(category.isSelected()) {
                category.setSelected(false);
            }
        }
        if(categoriesAdapter != null) {
            categoriesAdapter.notifyDataSetChanged();
        }
    }

    public void deleteAll() {
        for(Category category : categoryList) {
            if(category.isSelected()) {
                myDBHelper.deleteCategory(category.getId());
            }
        }
    }
}
