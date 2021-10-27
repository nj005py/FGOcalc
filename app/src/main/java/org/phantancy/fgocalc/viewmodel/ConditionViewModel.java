package org.phantancy.fgocalc.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.phantancy.fgocalc.data.repository.NoblePhantasmRepository;
import org.phantancy.fgocalc.entity.NoblePhantasmEntity;

import java.util.List;

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
