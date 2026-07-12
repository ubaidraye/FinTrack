package com.raye.fintrack.core.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    initializer: DatabaseInitializer
) : ViewModel() {

    init {
        viewModelScope.launch {
            initializer.initialize()
        }
    }
}