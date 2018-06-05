package yang.com.news.news.model;

import java.util.List;
import java.util.StringTokenizer;

import yang.com.news.beans.NewsBean;
import yang.com.news.beans.NewsDetailBean;
import yang.com.news.commons.Urls;
import yang.com.news.news.NewsJsonUtils;
import yang.com.news.news.widget.NewsFragment;
import yang.com.news.utils.OkHttpUtils;

/**
 * 创建人： 杨云杰
 * 创建时间 ： 2018/6/1
 * 描述：
 */

public class NewsModelImp implements INewsModel {
    /**
     *
     * @param url 请求地址
     *            因为网络工具的封装导致的ResultCallback的onSuccess和onFailure
     *            实在子线程中由handler.post传递到主线程中执行的
     * @param Type 根据类型是笑话 新闻还是啥的
     * @param onLoadNewsListListener 回调监听
     */
    @Override
    public void loadNews(String url, final int Type, final OnLoadNewsListListener onLoadNewsListListener) {
    OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
        @Override
        public void onSuccess(String response) {
            List<NewsBean> newsBeanList = NewsJsonUtils.readJsonNewsBeans(response,getID(Type));
            onLoadNewsListListener.onSuccess(newsBeanList);
        }

        @Override
        public void onFailure(Exception e) {
            onLoadNewsListListener.onFailure("load news list failure.", e);
        }
    };
        OkHttpUtils.get(url,loadNewsCallback);
    }

    /**
     *
     * @param docid
     * @param listener
     * 新闻详情回调
     */
    @Override
    public void loadNewsDetail(final String docid, final OnLoadNewsDetailListener listener) {
        String url = getDetailUrl(docid);
        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response,docid);
                listener.onSuccess(newsDetailBean);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news detail info failure.", e);
            }
        };
        OkHttpUtils.get(url,resultCallback);
    }

    /**
     * 获取ID 更据类型找到对应的id
     * @param type
     * @return
     */
    private String getID(int type) {
        String id;
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                id = Urls.TOP_ID;
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                id = Urls.NBA_ID;
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                id = Urls.CAR_ID;
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                id = Urls.JOKE_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }
    private String getDetailUrl(String docId) {
        StringBuffer sb = new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docId).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }
}

