package com.udacoding.pos.ui.profile.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacoding.pos.repository.RepositoryUser
import com.udacoding.pos.ui.profile.model.ResponseProfit

class ProfileViewModel : ViewModel() {

    private val repository = RepositoryUser()

    private val _profit = MutableLiveData<ResponseProfit>()
    val profit: LiveData<ResponseProfit> = _profit

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getProfit(id_user: Int){
        _loading.value = true
        repository.repoProfit(id_user,
            {
                _loading.value = false
                if (it.isSuccess == true)
                    _profit.value = it
            }, {
                _loading.value = false
                _error.value = it
            }
        )
    }
}