package yang.com.news.news.model;

import yang.com.news.beans.NewsDetailBean;

/**
 * 新闻详情页回调
 * 创建人： 杨云杰
 * 创建时间 ： 2018/6/3
 * 描述：
 */

public interface OnLoadNewsDetailListener {
    void onSuccess(NewsDetailBean newsDetailBean);

    void onFailure(String msg, Exception e);
}
