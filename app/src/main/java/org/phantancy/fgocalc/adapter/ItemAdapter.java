package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.item.Item;
import org.phantancy.fgocalc.itemview.ItemView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ItemAdapter extends BaseAdapter {
	private String TAG = "ItemAdapter";	

	/**
	 * 默认子类类型数量上限
	 */
	protected static final int DEFAULT_MAX_VIEW_TYPE_COUNT = 30;

	/**
	 * 对于底层实例统计
	 * 
	 * @author ww
	 */
	private static class TypeInfo {
		/**
		 * 某类item数量
		 */
		int count;
		/**
		 * item的具体类型
		 */
		int type;
	}

	/** items */
	private List<Item> mItems;
	/** 类型统计 */
	private HashMap<Class<? extends Item>, TypeInfo> mTypes;
	protected Context mContext;
	/**
	 * 刷新开关
	 */
	private boolean mNotifyOnChange = false;
	/**
	 * 子类类型数量上限
	 */
	private int mMaxViewTypeCount;

	/**
	 * 是否及时刷新
	 */
	private boolean isTimelyRefresh = true;
	public boolean isTimelyRefresh() {
		return isTimelyRefresh;
	}
	public void setTimelyRefresh(boolean isTimelyRefresh) {
		this.isTimelyRefresh = isTimelyRefresh;
	}

	public ItemAdapter(Context context) {
		this(context, new ArrayList<Item>());
	}
	public ItemAdapter(Context context, boolean isTimelyRefresh) {
		this(context, new ArrayList<Item>());
		this.isTimelyRefresh = isTimelyRefresh;
	}

	public ItemAdapter(Context context, Item[] items) {
		this(context, Arrays.asList(items), DEFAULT_MAX_VIEW_TYPE_COUNT);
	}

	public ItemAdapter(Context context, List<Item> items) {
		this(context, items, DEFAULT_MAX_VIEW_TYPE_COUNT);
	}

	public ItemAdapter(Context context, Item[] items, int maxViewTypeCount) {
		this(context, Arrays.asList(items), maxViewTypeCount);
	}

	public ItemAdapter(Context context, List<Item> items, int maxViewTypeCount) {
		mContext = context;
		mItems = items;
		mTypes = new HashMap<Class<? extends Item>, TypeInfo>();
		mMaxViewTypeCount = Integer.MAX_VALUE;

		for (Item item : mItems) {
			addItemType(item);
		}

		mMaxViewTypeCount = Math.max(1, Math.max(mTypes.size(), maxViewTypeCount));
	}

	/**
	 * 添加item类型，并不添加到list中
	 * 
	 * @param item
	 */
	private void addItemType(Item item) {
		final Class<? extends Item> klass = item.getClass();
		TypeInfo info = mTypes.get(klass);

		if (info == null) {
			final int type = mTypes.size();
			if (type >= mMaxViewTypeCount) {
				throw new RuntimeException("This ItemAdapter may handle only " + mMaxViewTypeCount
						+ " different view types.");
			}
			final TypeInfo newInfo = new TypeInfo();
			newInfo.count = 1;
			newInfo.type = type;
			mTypes.put(klass, newInfo);
		} else {
			info.count++;
		}
	}

	/**
	 * 删除item类型，并不删除list中item
	 * 
	 * @param item
	 */
	private void removeItemType(Item item) {

		final Class<? extends Item> klass = item.getClass();
		TypeInfo info = mTypes.get(klass);

		if (info != null) {
			info.count--;
			if (info.count == 0) {
				mTypes.remove(klass);
			}
		}
	}

	/**
	 * 重新注入List数据，更新数据入口<br>
	 * 1、清除所有item<br>
	 * 2、重新添加Items<br>
	 * 3、notifyDataSetChanged()<br>
	 * 
	 * @param items
	 */
	public void setItems(List<Item> items) {
		if (mItems != items) {
			mItems.clear();
			mItems = items;
		}
//		mTypes.clear();
//		for (Item item : items) {
//			addItemType(item);
//		}
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	/**
	 * 在原有基础上添加
	 * 
	 * @param items
	 */
	public void addItems(List<Item> items) {
		mItems.addAll(items);
//		for (Item item : items) {
//			addItemType(item);
//		}
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}
	
	public void addItem(int index, Item item) {
		mItems.add(index, item);
//		addItemType(item);
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}
	
	public void addItems(int index, List<Item> items) {
		mItems.addAll(index, items);
//		for (Item item : items) {
//			addItemType(item);
//		}
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	public void add(Item item) {
		mItems.add(item);
//		addItemType(item);
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	public void insert(Item item, int index) {
		mItems.add(index, item);
//		addItemType(item);
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}
	
	public void remove(int itemIndex) {
		mItems.remove(itemIndex);
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	public void remove(Item item) {
		if (mItems.remove(item)) {
			removeItemType(item);
			if (mNotifyOnChange) {
				notifyDataSetChanged();
			}
		}else {
			Log.e(TAG, "remove方法中--删除失败------>：item为：" + item);
		}
	}

	public void clear() {
		mItems.clear();
		mTypes.clear();
		this.notifyDataSetChanged();
	}

	/**
	 * 排序
	 */
	public void sort(Comparator<? super Item> comparator) {
		Collections.sort(mItems, comparator);
		if (mNotifyOnChange) {
			notifyDataSetChanged();
		}
	}

	public boolean ismNotifyOnChange() {
		return mNotifyOnChange;
	}

	public void setmNotifyOnChange(boolean mNotifyOnChange) {
		this.mNotifyOnChange = mNotifyOnChange;
	}

	public int getmMaxViewTypeCount() {
		return mMaxViewTypeCount;
	}

	public void setmMaxViewTypeCount(int mMaxViewTypeCount) {
		this.mMaxViewTypeCount = mMaxViewTypeCount;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Item item = (Item) getItem(position);
		ItemView itemView;
		if (convertView != null && convertView.getTag() != null
				&& Integer.valueOf(convertView.getTag().toString()) == item.getItemLayoutId()) {
			itemView = (ItemView) convertView;
		} else {
			itemView = item.newView(mContext, null);
			itemView.findViewsByIds();
			convertView = (View) itemView;
			convertView.setTag(item.getItemLayoutId());
		}
		
//		Log.e(TAG, "getView方法中-------->：旧item为-->" + ((View)itemView).getTag(R.id.itemadapter_position_id) + ";\n 新item为-->" + item);
//		// 如果当前view绑定的item与将要绑定的item不同，重新绑定新的item
		if (isTimelyRefresh || !item.equals(((View)itemView).getTag(R.id.itemadapter_position_id))) {
//			Log.e(TAG, "getView方法中-------->：新item为-->：" + item);
			// 绑定数据
			itemView.setObject(item, position, onViewClickListener);
			((View) itemView).setTag(R.id.itemadapter_position_id, item);
		}

		return (View) itemView;
	}

	protected OnViewClickListener onViewClickListener;

	public OnViewClickListener getOnViewClickListener() {
		return onViewClickListener;
	}

	public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
		this.onViewClickListener = onViewClickListener;
	}

	public interface OnViewClickListener {
		void onViewClick(View view, int position);
	}

}
