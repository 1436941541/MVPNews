package yang.com.news.news.model;

/**
 * Created by 杨云杰 on 2018/5/31.
 */

public interface INewsModel {
    void loadNews(String url,int Type,OnLoadNewsListListener onLoadNewsListListener);
    void loadNewsDetail(String docid, OnLoadNewsDetailListener listener);
}
