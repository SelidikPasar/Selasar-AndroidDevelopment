package com.selasarteam.selidikpasar.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selasarteam.selidikpasar.data.MainRepository
import com.selasarteam.selidikpasar.data.local.datastore.SessionModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: MainRepository) : ViewModel() {
//    fun postLogin(email: String, password: String) {
//        viewModelScope.launch {
//            repo.postLogin(email, password)
//        }
//    }

    fun saveSession(session: SessionModel) {
        viewModelScope.launch {
            repo.saveSession(session)
        }
    }

    fun login() {
        viewModelScope.launch {
            repo.login()
        }
    }
}