package ru.arnis.producthuntdemoapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.arnis.producthuntdemoapp.R;
import ru.arnis.producthuntdemoapp.adapter.RecyclerViewAdapter;
import ru.arnis.producthuntdemoapp.model.Post;
import ru.arnis.producthuntdemoapp.service.UpdateDataService;

/**
 * Created by arnis on 26/01/2017.
 */

public class CategoryFragment extends Fragment{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_items_placeholder)
    View noItemsPlaceholder;
    RecyclerViewAdapter adapter;
    private List<Post> posts;
    private String categoryName;

    @BindColor(R.color.colorAccent)
    int colorAccent;

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("happy", "onCreateView: ");
        View view = inflater.inflate(R.layout.category_fragment,container,false);
        ButterKnife.bind(this,view);
        adapter = new RecyclerViewAdapter(getContext(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.resetLastAnimated();
                UpdateDataService.startInApp(getContext(),((MainActivity)getActivity()).receiver,categoryName);
            }
        });
        checkIfEmpty();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("happy", "onViewCreated: ");
    }

    public void stopRefresh(){
        if (swipeRefreshLayout!=null && swipeRefreshLayout.isRefreshing()){
            adapter.notifyDataSetChanged();
            checkIfEmpty();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void checkIfEmpty(){
        if (getPosts().size()==0){
            noItemsPlaceholder.setVisibility(View.VISIBLE);
        } else noItemsPlaceholder.setVisibility(View.GONE);
    }
}
