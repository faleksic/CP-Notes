package hr.faleksic.android.cpnotes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class CategoryFragment extends Fragment {

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
        categoriesAdapter = new CategoriesAdapter(categoryList);

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

    private void refresh() {
        categoryList.clear();
        categoriesAdapter.notifyItemRangeRemoved(0, categoriesAdapter.getItemCount());
        prepareCategoryData();
    }

    private void prepareCategoryData() {

        Cursor cursor = myDBHelper.getAllCategories();

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            int count = cursor.getInt(1);
            categoryList.add(new Category(name, count));
        }
        cursor.close();

        categoriesAdapter.notifyDataSetChanged();
    }


}
