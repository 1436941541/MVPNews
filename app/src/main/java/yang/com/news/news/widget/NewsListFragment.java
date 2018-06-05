package yang.com.news.news.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import yang.com.news.R;
import yang.com.news.beans.NewsBean;
import yang.com.news.commons.Urls;
import yang.com.news.news.NewsAdapter;
import yang.com.news.news.presenter.INewsPresenter;
import yang.com.news.news.presenter.NewsPresentImp;
import yang.com.news.news.view.INewsView;

/**
 * 根据每次的type来确定到底是哪个具体的新闻模块
 * Created by 杨云杰 on 2018/5/31.
 */

public class NewsListFragment extends Fragment implements INewsView,SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsBean> mData;
    private INewsPresenter mNewsPresenter;
    private LinearLayoutManager linearLayoutManager;
    private int mType = NewsFragment.NEWS_TYPE_TOP;//默认打开的时候显示的是top
    private int pageIndex = 0;//url所需要的具体的参数

    public static NewsListFragment newInstance(int Type) {
        //将type是第几个类型存进去
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        args.putInt("type", Type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsPresenter = new NewsPresentImp(this);
        mType = getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newslist, container,false);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary,
                R.color.primary_dark, R.color.primary_light,
                R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);//当确定recyclerview的item的变化不会影响recyclerview的宽高是设置为true
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        newsAdapter = new NewsAdapter(getActivity().getApplicationContext());
        newsAdapter.setOnItemclickListener(mOnItemClickListener);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.addOnScrollListener(mOnScrollListener);
        onRefresh();
        return view;
    }

    /**
     * 设置滑动监听
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;//当前屏幕中的最后一项的position

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE  //静止时没有滚动
                    && lastVisibleItem + 1 == newsAdapter.getItemCount()
                    && newsAdapter.isShowFooter()) {
                //加载更多
                mNewsPresenter.loadNews(mType, pageIndex);
            }
        }
    };
    @Override
    public void showLoadFailMsg() {
        if(pageIndex == 0) {
            newsAdapter.isShowFooter(false);
            newsAdapter.notifyDataSetChanged();
        }
        View view = getActivity() == null ? recyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, "加载失败", Snackbar.LENGTH_SHORT).show();
    }
    private NewsAdapter.OnItemClickListener mOnItemClickListener = new NewsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size()<=0) {
                return;
            }
            NewsBean newsBean = newsAdapter.getItem(position);
            Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
            intent.putExtra("news",newsBean);
            startActivity(intent);
//            View transitionView = view.findViewById(R.id.ivNews);
//            ActivityOptionsCompat options =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                            transitionView, "transition_news_img");
//            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    };
    @Override
    public void addNews(List<NewsBean> newsList) {
        newsAdapter.isShowFooter(true);//相当于现在的加载item是存在的
        if(mData == null) {
            mData = new ArrayList<NewsBean>();
        }
        mData.addAll(newsList);
        if(pageIndex == 0) {
            newsAdapter.setmData(mData);
        } else {
            //如果没有更多数据了,则隐藏footer布局
            if(newsList == null || newsList.size() == 0) {
                newsAdapter.isShowFooter(false);
            }
            else {
                newsAdapter.notifyDataSetChanged();
            }
        }

        pageIndex += Urls.PAZE_SIZE;
    }

    @Override
    public void hideProgress() {
    swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
            pageIndex = 0;
            if(mData != null) {
                mData.clear();
            }
            mNewsPresenter.loadNews(mType, pageIndex);
    }
}
