package ru.arnis.producthuntdemoapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.arnis.producthuntdemoapp.model.Category;
import ru.arnis.producthuntdemoapp.model.Post;
import ru.arnis.producthuntdemoapp.view.CategoryFragment;

/**
 * Created by arnis on 26/01/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private List<Category> categories;
    private List<CategoryFragment> fragments;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        categories = new ArrayList<>();
        fragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getName();
    }

    @Override
    public int getCount() {
        return categories.size();
    }



    public void addData(List<Category> categories, List<List<Post>> posts) {
        this.categories = categories;
        CategoryFragment categoryFragment;
        for (int i = 0; i < categories.size(); i++) {
            categoryFragment = new CategoryFragment();
            categoryFragment.setPosts(posts.get(i));
            categoryFragment.setCategoryName(categories.get(i).getSlug());
            fragments.add(categoryFragment);
        }
        notifyDataSetChanged();
    }
    public void updateData(String category, List<Post> posts) {
        CategoryFragment categoryFragment = fragments.get(getIndexOfSlug(category));
        categoryFragment.setPosts(posts);
        categoryFragment.stopRefresh();
    }

    private int getIndexOfSlug(String slug){
        for (Category category:categories)
            if (category.getSlug().equals(slug))
                return categories.indexOf(category);
        return -1;
    }
}
