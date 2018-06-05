package yang.com.news.news.presenter;

import android.content.Context;

import yang.com.news.beans.NewsDetailBean;
import yang.com.news.news.model.INewsModel;
import yang.com.news.news.model.NewsModelImp;
import yang.com.news.news.model.OnLoadNewsDetailListener;
import yang.com.news.news.view.INewsDetailView;
import yang.com.news.news.view.INewsView;

/**
 * 创建人： 杨云杰
 * 创建时间 ： 2018/6/3
 * 描述：
 */

public class NewsDetailPresenterImp implements INewsDetailPresenter,OnLoadNewsDetailListener {
    private INewsDetailView iNewsDetailView;
    private Context context;
    private INewsModel iNewsModel;
    public NewsDetailPresenterImp(Context context,INewsDetailView iNewsDetailView) {
        this.iNewsDetailView = iNewsDetailView;
        this.context = context;
        iNewsModel = new NewsModelImp();
    }
    @Override
    public void loadNewsDetail(String docId) {
        iNewsDetailView.showProgress();
        iNewsModel.loadNewsDetail(docId,this);
    }

    @Override
    public void onSuccess(NewsDetailBean newsDetailBean) {
        if (newsDetailBean != null) {
            iNewsDetailView.showNewsDetialContent(newsDetailBean.getBody());
        }
        iNewsDetailView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        iNewsDetailView.hideProgress();
    }
}
