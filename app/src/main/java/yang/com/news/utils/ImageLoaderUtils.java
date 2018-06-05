package yang.com.news.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import yang.com.news.R;

/**
 * Description : 图片加载工具类
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if(imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(placeholder)
                .error(error).crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if(imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        /**
         * error异位错位符，你可以将图片的url地址修改成一个不存在的图片地址，或者干脆直接将手机的网络给关了，就会显示
         * placeholder(R.drawable.ic_image_loading)占位符代表当图片没有加载出来的时候默认显示这张图片
         */
        Glide.with(context).load(url).placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loadfail).crossFade().into(imageView);
    }


}
