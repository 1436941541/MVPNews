package yang.com.news.news.view;

import java.util.List;

import yang.com.news.beans.NewsBean;

/**
 * view层的接口，NewsListFragment来继承一些方法
 * Created by 杨云杰 on 2018/5/31.
 */

public interface INewsView {
    void showLoadFailMsg();/*加载数据失败*/
    void addNews(List<NewsBean> newsList);
    void hideProgress();
    void showProgress();//加载进度
}
