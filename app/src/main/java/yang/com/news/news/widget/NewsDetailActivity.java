package yang.com.news.news.widget;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.sufficientlysecure.htmltextview.HtmlTextView;


import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import yang.com.news.R;
import yang.com.news.beans.NewsBean;
import yang.com.news.news.presenter.INewsDetailPresenter;
import yang.com.news.news.presenter.NewsDetailPresenterImp;
import yang.com.news.news.view.INewsDetailView;
import yang.com.news.utils.ImageLoaderUtils;
import yang.com.news.utils.ToolsUtil;

public class NewsDetailActivity extends SwipeBackActivity implements INewsDetailView{
    private NewsBean mNews;
    private HtmlTextView mTVNewsContent;
    private INewsDetailPresenter mNewsDetailPresenter;
    private ProgressBar mProgressBar;
    private SwipeBackLayout mSwipeBackLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mTVNewsContent = (HtmlTextView)findViewById(R.id.htNewsContent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Log.d("yyj", "onClick: ");
            }
        });
        setSwipeBackEnable(true);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);//使用哪种模式来关闭activity
        mNews = (NewsBean) getIntent().getSerializableExtra("news");
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(mNews.getTitle());
        ImageLoaderUtils.display(getApplicationContext(),(ImageView)findViewById(R.id.ivImage),mNews.getImgsrc());
        mNewsDetailPresenter = new NewsDetailPresenterImp(getApplication(),this);
        mNewsDetailPresenter.loadNewsDetail(mNews.getDocid());
    }

    @Override
    public void showNewsDetialContent(String newsDetailContent) {
        mTVNewsContent.setHtmlFromString(newsDetailContent,new HtmlTextView.LocalImageGetter());
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
