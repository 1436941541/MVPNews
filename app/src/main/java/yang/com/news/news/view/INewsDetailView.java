package yang.com.news.news.view;

/**
 * 创建人： 杨云杰
 * 创建时间 ： 2018/6/3
 * 描述：
 */

public interface INewsDetailView {
    void showNewsDetialContent(String newsDetailContent);

    void showProgress();

    void hideProgress();
}
