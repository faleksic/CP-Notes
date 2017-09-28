package hr.faleksic.android.cpnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.example.android.cpnotes.R;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Menu menu;
    private NoteFragment noteFragment;
    private CategoryFragment categoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        noteFragment = new NoteFragment();
        adapter.addFragment(noteFragment, "Notes");
        categoryFragment = new CategoryFragment();
        adapter.addFragment(categoryFragment, "Categories");
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.getCurrentItem();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (viewPager.getCurrentItem() == 0) {
                    Intent i = new Intent(this, CreateNoteActivity.class);
                    startActivity(i);
                } else if (viewPager.getCurrentItem() == 1) {
                    CategoryDialog categoryDialog = new CategoryDialog();
                    categoryDialog.show(getFragmentManager(), "category");
                    categoryDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            ((CategoryFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem())).refresh();
                        }
                    });
                }
                return true;
            case R.id.action_delete_all:
                if(categoryFragment != null) {
                    categoryFragment.deleteAll();
                    onSupportNavigateUp();
                    categoryFragment.refresh();
                }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void changeMenu(int menuId) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuId, menu);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(categoryFragment != null) {
            categoryFragment.stopDelete();
            menu.clear();
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(!categoryFragment.showCheckboxes) {
            super.onBackPressed();
        } else {
            onSupportNavigateUp();
        }
    }
}
