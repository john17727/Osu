package com.example.shared.cache

import com.example.retrofit_extensions.cache.CacheErrors.CACHE_DATA_NULL
import com.example.retrofit_extensions.cache.CacheResult
import com.john.mvi.domain.message.MessageType
import com.john.mvi.domain.message.StateMessage
import com.john.mvi.domain.message.UIComponentType
import com.john.mvi.domain.state.DataState
import com.john.mvi.domain.state.StateEvent

abstract class CacheResponseHandler<ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?
) {

    suspend fun getResult(): DataState<ViewState>? {
        return when (response) {
            is CacheResult.GenericError -> {
                DataState.error(
                    message = StateMessage(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is CacheResult.Success -> {
                response.value?.let {
                    handleSuccess(resultObj = it)
                } ?: DataState.error(
                    message = StateMessage(
                        message = "${stateEvent?.errorInfo()}\n\nReason: $CACHE_DATA_NULL",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }
        }
    }

    abstract fun handleSuccess(resultObj: Data): DataState<ViewState>
}