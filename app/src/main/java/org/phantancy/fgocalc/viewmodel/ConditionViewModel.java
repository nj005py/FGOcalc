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
    private MutableLiveData<List<NoblePhantasmEntity>> npEntities = new MutableLiveData<>();

    public ConditionViewModel(@NonNull Application application) {
        super(application);
        npRepository = new NoblePhantasmRepository(application);
    }

    public LiveData<List<NoblePhantasmEntity>> getNPEntities(int svtId) {
        return npRepository.getNoblePhantasmEntities(svtId);
    }
}
