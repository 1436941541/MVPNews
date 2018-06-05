package yang.com.news.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import yang.com.news.R;
import yang.com.news.beans.NewsBean;
import yang.com.news.utils.ImageLoaderUtils;

/**
 * Created by 杨云杰 on 2018/5/31.
 */

public class NewsAdapter extends RecyclerView.Adapter {
    private List<NewsBean> mData;
    /**
     * TYPE_FOOTER代表底部的那个item
     *
     */
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private boolean mShowFooter = true;
    private OnItemClickListener onItemClickListener;
    public NewsAdapter(Context context) {
        this.context = context;
    }

    /**
     * 加载数据需要重新执行此方法
     * @param mData
     */
    public void setmData(List<NewsBean> mData) {
        this.mData = mData;
        this.notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(v);
            return itemViewHolder;
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer,parent,false);
            //setLayoutParams重新设置属性
//            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }
    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            NewsBean news = mData.get(position);
            if (news == null) {
                return;
            }
            ((ItemViewHolder) holder).mTitle.setText(news.getTitle());
            ((ItemViewHolder) holder).mDesc.setText(news.getDigest());
            ImageLoaderUtils.display(context,((ItemViewHolder) holder).mNewsImg,news.getImgsrc());
        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter?1:0;
        //之所以这样设置是为了可以将加载进度的那个item给隐藏掉
            if (mData == null) {
                return begin;
            }
        return mData.size() + begin;
    }
    public NewsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }
    // 设置是否显示底部加载提示（将值传递给全局变量）
    public void isShowFooter(boolean showFooter)
    {
        this.mShowFooter = showFooter;
    }

    // 判断是否显示底部，数据来自全局变量
    public boolean isShowFooter()
    {
        return this.mShowFooter;
    }
    @Override
    public int getItemViewType(int position) {
        if (!mShowFooter) {
            return TYPE_ITEM;
        }
        //position + 1 = getItemCount()时证明已经到底了
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public TextView mDesc;
        public ImageView mNewsImg;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            mDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            mNewsImg = (ImageView) itemView.findViewById(R.id.ivNews);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view,this.getPosition());
            }
        }
    }
    public void setOnItemclickListener(OnItemClickListener onItemclickListener) {
        this.onItemClickListener = onItemclickListener;
    }
    /**
     * 设置一个回调的接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view,int position);
    }
}
