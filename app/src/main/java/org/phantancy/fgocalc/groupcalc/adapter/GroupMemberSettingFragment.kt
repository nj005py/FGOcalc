package org.phantancy.fgocalc.groupcalc.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.phantancy.fgocalc.databinding.FragmentGroupMemberSettingBinding
import org.phantancy.fgocalc.fragment.LazyFragment

/**
 * 编队从者条件
 */
class GroupMemberSettingFragment : LazyFragment(){
    private lateinit var  binding:FragmentGroupMemberSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGroupMemberSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun init() {
        super.init()

    }
}