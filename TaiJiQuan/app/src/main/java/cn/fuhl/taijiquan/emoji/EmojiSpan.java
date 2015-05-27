package cn.fuhl.taijiquan.emoji;

import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import cn.fuhl.taijiquan.app.AppContext;


public class EmojiSpan extends DynamicDrawableSpan {
	private String value;
	private Drawable mDrawable;
	private final int mSize;
	private int type;

	public EmojiSpan(String value, int size, int type) {
		this.value = value;
		this.mSize = size;
		this.type = type;
	}

	@Override
	public Drawable getDrawable() {
		if (mDrawable == null) {
			try {
				Emoji emoji = null;
				if (type == 0) {
					emoji = EmojiHelper.getEmoji(value);
				} else {
					emoji = EmojiHelper.getEmojiByNumber(value);
				}
				if (emoji != null) {
					mDrawable = AppContext.instance().getResources()
							.getDrawable(emoji.getResId());
					int size = mSize;
					mDrawable.setBounds(0, 0, 40, 40);
				}
			} catch (Exception e) {
				// swallow
			}
		}
		return mDrawable;
	}
}
