package cn.fuhl.taijiquan.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.fuhl.taijiquan.R;
import cn.fuhl.taijiquan.adapter.RecyclingPagerAdapter;

public class EmojiViewPagerAdapter extends RecyclingPagerAdapter {

	public interface OnClickEmojiListener {
		void onEmojiClick(Emoji emoji);

		void onDelete();
	}

	private LayoutInflater infalter;
	private int mEmojiHeight;
	private List<List<Emoji>> mPagers = new ArrayList<List<Emoji>>();

	private OnClickEmojiListener mListener;

	public EmojiViewPagerAdapter(Context context, List<List<Emoji>> pager,
			int emojiHeight, OnClickEmojiListener listener) {
		infalter = LayoutInflater.from(context);
		mPagers = pager;
		mEmojiHeight = emojiHeight;
		mListener = listener;
	}

	public void setEmojiHeight(int emojiHeight) {
		mEmojiHeight = emojiHeight;
		notifyDataSetChanged();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = infalter.inflate(R.layout.emoji_pager, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final EmojiAdatper adapter = new EmojiAdatper(mPagers.get(position),
				mEmojiHeight);
		vh.gv.setAdapter(adapter);
		vh.gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mListener == null)
					return;
				if (position == adapter.getCount() - 1) {
					mListener.onDelete();
				} else {
					mListener.onEmojiClick((Emoji) adapter.getItem(position));
				}
			}
		});
		adapter.notifyDataSetChanged();
		return convertView;
	}

	@Override
	public int getCount() {
		return mPagers.size();
	}

	static class ViewHolder {
		GridView gv;

		public ViewHolder(View v) {
			gv = (GridView) v.findViewById(R.id.gv_emoji);
		}
	}
}
