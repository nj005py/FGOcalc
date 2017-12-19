package org.phantancy.fgocalc.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.phantancy.fgocalc.itemview.ItemView;

import java.io.Serializable;
import java.util.HashMap;

public abstract class Item implements Serializable {

	// private SparseArray<Object> mTags;
	private Object mTag;
	private HashMap<Integer, Object> mTags;

	public boolean enabled;

	public Item() {
		enabled = true;
	}

	public Object getTag() {
		return mTag;
	}

	public void setTag(Object tag) {
		mTag = tag;
	}

	public Object getTag(int key) {
		return (mTags == null) ? null : mTags.get(key);
	}

	public void setTag(int key, Object tag) {
		if (mTags == null) {
			// mTags = new SparseArray<Object>();
			mTags = new HashMap<Integer, Object>();
		}
		mTags.put(key, tag);
	}

	/**
	 * 抽象方法，视图的默认创建
	 * 
	 * @param context
	 * @param parent
	 * @return
	 */
	public ItemView newView(Context context, ViewGroup parent) {
		return createCellFromXml(context, getItemLayoutId(), parent);
	};

	/***
	 * 
	 * @return
	 */
	public abstract int getItemLayoutId();

	/**
	 * 对象1对多视图的创建
	 * 
	 * @param context
	 * @param layoutID
	 * @param parent
	 * @return
	 */
	protected static ItemView createCellFromXml(Context context, int layoutID, ViewGroup parent) {
		return (ItemView) LayoutInflater.from(context).inflate(layoutID, parent, false);
	}

	/**
	 * 从xml中填充布局
	 * 
	 * @param r
	 * @param parser
	 * @param attrs
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	// public void inflate(Resources r, XmlPullParser parser, AttributeSet
	// attrs) throws XmlPullParserException, IOException {
	// TypedArray a = r.obtainAttributes(attrs, R.styleable.Item);
	// enabled = a.getBoolean(R.styleable.Item_enabled, enabled);
	// a.recycle();
	// }
	
	/**
	 * 删除
	 */
	public void delete(){
		
	}
	
	/**
	 * 更新
	 */
	public void update(){
		
	}
	
	/**
	 * 创建
	 * @return
	 */
	public int create(){
		return -1;
	}
	
	/**
	 * 插入
	 * @return
	 */
	public int insert(){
		return -1;
	}

}
