package yang.com.news.main.widget;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import yang.com.news.R;
import yang.com.news.about.widget.AboutFragment;
import yang.com.news.images.widget.ImagesFragment;
import yang.com.news.main.presenter.IMainPresenter;
import yang.com.news.main.presenter.MainPresenterImpl;
import yang.com.news.main.view.IMainView;
import yang.com.news.news.widget.NewsFragment;
import yang.com.news.weather.WeatherFragment;

public class MainActivity extends AppCompatActivity implements IMainView{
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private IMainPresenter mIMainPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.drawer_open , R.string.drawer_close);
        mDrawerToggle.syncState();//设置显示3条横杠
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mIMainPresent = new MainPresenterImpl(this);
        setupDrawerContent(mNavigationView);//设置侧滑栏的监听
        switchNews();
        getSupportActionBar().setTitle("新闻");
    }

    private void setupDrawerContent(final NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mIMainPresent.switchNavigation(item.getItemId());
                mDrawerLayout.closeDrawers();
                return  true;
            }
        });
    }

    @Override
    public void switchNews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new NewsFragment()).commit();
        mToolbar.setTitle("新闻");
    }

    @Override
    public void switchImage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new ImagesFragment()).commit();
        mToolbar.setTitle("图片");
    }

    @Override
    public void switchWeather() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new WeatherFragment()).commit();
        mToolbar.setTitle("天气");
    }

    @Override
    public void switchAbout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new AboutFragment()).commit();
        mToolbar.setTitle("关于");
    }
}
