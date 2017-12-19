package org.phantancy.fgocalc.itemview;

import org.phantancy.fgocalc.adapter.ItemAdapter;
import org.phantancy.fgocalc.item.Item;

public interface ItemView {

	/**
     */
	void findViewsByIds();

	/**
     */
	void setObject(Item item, int position, ItemAdapter.OnViewClickListener onViewClickListener);

}
