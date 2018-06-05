package yang.com.news.news.model;


import java.util.List;

import yang.com.news.beans.NewsBean;

/**
 * Description : 新闻列表加载回调
 */
public interface OnLoadNewsListListener {

    void onSuccess(List<NewsBean> list);

    void onFailure(String msg, Exception e);
}
