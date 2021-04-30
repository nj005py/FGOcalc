package org.phantancy.fgocalc.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.phantancy.fgocalc.data.NoblePhantasmRepository;
import org.phantancy.fgocalc.entity.NoblePhantasmEntity;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConditionViewModel extends AndroidViewModel {

    private NoblePhantasmRepository npRepository;
    private MutableLiveData<NoblePhantasmEntity> npEntity = new MutableLiveData<>();
    public LiveData<NoblePhantasmEntity> getNpEntity() {
        return npEntity;
    }

    private MutableLiveData<List<NoblePhantasmEntity>> npEntities = new MutableLiveData<>();

    public ConditionViewModel(@NonNull Application application) {
        super(application);
        npRepository = new NoblePhantasmRepository(application);
    }

    public void getNoblePhantasmEntity(int svtId) {
        Flowable.create(new FlowableOnSubscribe<NoblePhantasmEntity>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull FlowableEmitter<NoblePhantasmEntity> emitter) throws Exception {
                NoblePhantasmEntity en = npRepository.getNoblePhantasmEntity(svtId);
                emitter.onNext(en);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<NoblePhantasmEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(NoblePhantasmEntity noblePhantasmEntity) {
                        //更新
                        npEntity.setValue(noblePhantasmEntity);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public LiveData<List<NoblePhantasmEntity>> getNPEntities(int svtId) {
        return npRepository.getNoblePhantasmEntities(svtId);
    }
}
