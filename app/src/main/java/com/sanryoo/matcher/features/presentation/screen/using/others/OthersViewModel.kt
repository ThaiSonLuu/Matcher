package com.sanryoo.matcher.features.presentation.screen.using.others

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.util.MyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OthersViewModel @Inject constructor(
    @Named(value = MyString.OTHERS)
    private val _others: MutableStateFlow<User>
) : ViewModel() {

    val others = _others.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val evenFlow = _eventFlow.asSharedFlow()

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    sealed class UiEvent {
        object NavigateBack : UiEvent()
    }

}