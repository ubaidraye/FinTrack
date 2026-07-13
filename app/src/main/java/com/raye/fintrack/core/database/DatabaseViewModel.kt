package com.raye.fintrack.core.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.raye.fintrack.core.database.FinTrackDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val initializer: DatabaseInitializer,
    private val database: FinTrackDatabase
) : ViewModel() {

    init {
        viewModelScope.launch {
            initializer.initialize()
        }
    }

    fun resetDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            database.clearAllData()
            initializer.initialize()
        }
    }
}
