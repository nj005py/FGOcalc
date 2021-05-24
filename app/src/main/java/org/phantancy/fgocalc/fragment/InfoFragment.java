package org.phantancy.fgocalc.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.InfoAdapter;
import org.phantancy.fgocalc.databinding.FragInfoBinding;
import org.phantancy.fgocalc.entity.InfoEntity;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;

import java.util.List;

public class InfoFragment extends LazyFragment {
    private FragInfoBinding binding;
    private CalcViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragInfoBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    protected void init() {
        vm = new ViewModelProvider(mActy).get(CalcViewModel.class);

        ServantEntity svt = vm.getServant();
        if (svt.avatarRes != -1) {
            Glide.with(ctx)
                    .load(svt.avatarRes)
                    .placeholder(R.drawable.loading)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            scheduleStartPostonedTransition(binding.ivInfoAvatar);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            scheduleStartPostonedTransition(binding.ivInfoAvatar);
                            return false;
                        }
                    })
                    .into(binding.ivInfoAvatar);
        } else {
            Glide.with(ctx)
                    .load(svt.avatarUrl)
                    .placeholder(R.drawable.loading)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            scheduleStartPostonedTransition(binding.ivInfoAvatar);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            scheduleStartPostonedTransition(binding.ivInfoAvatar);
                            return false;
                        }
                    })
                    .into(binding.ivInfoAvatar);
        }
        binding.tvInfoName.setText(svt.name);
        binding.tvInfoNickname.setText(svt.nickname);

        initInfoList();
    }

    private void initInfoList() {
        InfoAdapter adapter = new InfoAdapter();
        binding.rvInfo.setAdapter(adapter);
        vm.getInfoList().observe(this, new Observer<List<InfoEntity>>() {
            @Override
            public void onChanged(List<InfoEntity> x) {
                adapter.submitList(x);
            }
        });
        vm.initInfoList();
    }

    private void scheduleStartPostonedTransition(final View x){
        x.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw() {
                x.getViewTreeObserver().removeOnPreDrawListener(this);
                mActy.startPostponedEnterTransition();
                return true;
            }
        });

    }
}
