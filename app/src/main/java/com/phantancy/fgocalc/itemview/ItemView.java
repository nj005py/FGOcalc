package com.phantancy.fgocalc.itemview;

import com.phantancy.fgocalc.adapter.ItemAdapter;
import com.phantancy.fgocalc.item.Item;

public interface ItemView {

	/**
     */
	void findViewsByIds();

	/**
     */
	void setObject(Item item, int position, ItemAdapter.OnViewClickListener onViewClickListener);

}
