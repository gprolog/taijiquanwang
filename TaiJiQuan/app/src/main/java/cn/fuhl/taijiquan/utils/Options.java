package cn.fuhl.taijiquan.utils;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import cn.fuhl.taijiquan.R;

public class Options {
	/**
	 * 新闻列表中用到的图片加载配置
	 *
	 */
	public static DisplayImageOptions getListOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.small_image_holder_listpage)// 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.small_image_holder_listpage)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.small_image_holder_listpage)// 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))
				.build();
		return options;
	}
}
