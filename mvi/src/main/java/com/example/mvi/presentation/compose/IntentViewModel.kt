package com.john.mvi.presentation.compose

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john.mvi.data.DataChannelManager
import com.john.mvi.domain.message.MessageType
import com.john.mvi.domain.message.StateMessage
import com.john.mvi.domain.message.UIComponentType
import com.john.mvi.domain.state.DataState
import com.john.mvi.domain.state.StateEvent
import com.john.mvi.domain.state.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

abstract class IntentViewModel<Event : StateEvent, UiState : ViewState> : ViewModel() {

    private val dataChannelManager: DataChannelManager<UiState> =
        object : DataChannelManager<UiState>() {
            override fun handleNewData(data: UiState) {
                _isLoading.value = false
                handleNewState(data)
            }
        }

    private val initialState: UiState by lazy { setInitialState() }
    abstract fun setInitialState(): UiState

    private val _viewState: MutableState<UiState> = mutableStateOf(initialState)
    val viewState: State<UiState> = _viewState

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    init {
        subscribeToEvents()
    }

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    private fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                val job = handleEvents(it)
                launchJob(it, job)
            }
        }
    }

    open fun handleNewState(data: UiState) {
        setState { data }
    }

    abstract fun handleEvents(event: Event): Flow<DataState<UiState>>

    private fun launchJob(stateEvent: StateEvent, jobFunction: Flow<DataState<UiState>?>) {
        _isLoading.value = stateEvent.shouldDisplayProgressBar()
        dataChannelManager.launchJob(stateEvent, jobFunction)
    }

    fun emitInvalidStateEvent(stateEvent: Event, message: String) = flow<DataState<UiState>> {
        emit(
            DataState.error(
                message = StateMessage(
                    message = message,
                    uiComponentType = UIComponentType.None,
                    messageType = MessageType.Error
                ),
                stateEvent = stateEvent
            )
        )
    }
}