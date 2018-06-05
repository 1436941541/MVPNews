package yang.com.news.main.presenter;

import yang.com.news.R;
import yang.com.news.main.view.IMainView;

/**
 * presenter层 与view 和model 层交互
 * Created by 杨云杰 on 2018/5/30.
 */

public class MainPresenterImpl implements IMainPresenter {
    private IMainView mIMainView;
    public MainPresenterImpl(IMainView iMainView){
        mIMainView = iMainView;
    }
    @Override
    public void switchNavigation(int id) {
        switch (id) {
            case R.id.navigation_item_news:
                mIMainView.switchNews();
                break;
            case R.id.navigation_item_images:
                mIMainView.switchImage();
                break;
            case R.id.navigation_item_weather:
                mIMainView.switchWeather();
                break;
            case R.id.navigation_item_about:
                mIMainView.switchAbout();
                break;
            default:
                break;
        }
    }
}
