package yang.com.news.news.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import yang.com.news.R;

/**
 * 所有的新闻的那个界面的fragment
 * Created by 杨云杰 on 2018/5/30.
 */

public class NewsFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPage;
    /**
     * 分别代表的头条 nba 汽车 和笑话
     */
    public static final int NEWS_TYPE_TOP = 0;
    public static final int NEWS_TYPE_NBA = 1;
    public static final int NEWS_TYPE_CARS = 2;
    public static final int NEWS_TYPE_JOKES = 3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPage = view.findViewById(R.id.view_pager);
        PageAdapter adapter = new PageAdapter(getChildFragmentManager());
        adapter.add(NewsListFragment.newInstance(NEWS_TYPE_TOP),"新闻");
        adapter.add(NewsListFragment.newInstance(NEWS_TYPE_NBA),"NBA");
        adapter.add(NewsListFragment.newInstance(NEWS_TYPE_CARS),"汽车");
        adapter.add(NewsListFragment.newInstance(NEWS_TYPE_JOKES),"笑话");
        mViewPage.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPage);//将viewpage和tablayout连接起来
        return view;
    }

    /**
     * 适配器类 每一个fragment可以自带一个标题，这是自定义的，也可以不自带，就要在前面mTabLayout里面加
     */
    public class PageAdapter extends FragmentPagerAdapter {
        ArrayList<String> mFragmentTitle = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }
        private void add(Fragment fragment,String title) {
            mFragments.add(fragment);
            mFragmentTitle.add(title);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitle.get(position);
        }

    }
}
