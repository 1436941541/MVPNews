package yang.com.news.news.presenter;

import android.util.Log;

import java.util.List;

import yang.com.news.beans.NewsBean;
import yang.com.news.commons.Urls;
import yang.com.news.news.model.INewsModel;
import yang.com.news.news.model.NewsModelImp;
import yang.com.news.news.model.OnLoadNewsListListener;
import yang.com.news.news.view.INewsView;
import yang.com.news.news.widget.NewsFragment;

/**
 * 创建人： 杨云杰
 * 创建时间 ： 2018/6/1
 * 描述：
 */

public class NewsPresentImp implements INewsPresenter,OnLoadNewsListListener {
    INewsView iNewsView;
    INewsModel iNewsModel;
    public NewsPresentImp(INewsView iNewsView) {
        this.iNewsView = iNewsView;
        this.iNewsModel = new NewsModelImp();
    }

    @Override
    public void loadNews(int type, int pageIndex) {
        String url = getUrl(type, pageIndex);
        //只有第一页的或者刷新的时候才显示刷新进度条



        //注
        if(pageIndex == 0) {
            iNewsView.showProgress();
        }
        iNewsModel.loadNews(url, type, this);
    }

    /**
     * 获取最终的url地址，然后返回数据解析出来
     * @param type
     * @param pageIndex
     * @return
     */
    private String getUrl(int type, int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }

    @Override
    public void onSuccess(List<NewsBean> list) {
        iNewsView.hideProgress();
        iNewsView.addNews(list);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        iNewsView.hideProgress();
        iNewsView.showLoadFailMsg();
    }
}
