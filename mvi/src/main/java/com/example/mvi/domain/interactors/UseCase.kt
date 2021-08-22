package com.example.mvi.domain.interactors

import com.john.mvi.domain.state.DataState
import com.john.mvi.domain.state.StateEvent
import com.john.mvi.domain.state.ViewState
import kotlinx.coroutines.flow.Flow

interface UseCase<Event : StateEvent, UiState : ViewState> {
    fun execute(stateEvent: Event): Flow<DataState<UiState>>
}