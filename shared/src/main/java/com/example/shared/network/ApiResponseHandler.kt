package com.example.shared.network

import com.john.mvi.domain.message.MessageType
import com.john.mvi.domain.message.StateMessage
import com.john.mvi.domain.message.UIComponentType
import com.example.retrofit_extensions.network.ApiResult
import com.example.retrofit_extensions.network.NetworkErrors.NETWORK_DATA_NULL
import com.example.retrofit_extensions.network.NetworkErrors.NETWORK_ERROR
import com.john.mvi.domain.state.DataState
import com.john.mvi.domain.state.StateEvent

abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
) {

    suspend fun getResult(): DataState<ViewState> {
        return when (response) {
            is ApiResult.GenericError -> {
                DataState.error(
                    message = StateMessage(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )

            }
            is ApiResult.NetworkError -> {
                DataState.error(
                    message = StateMessage(
                        message = "${stateEvent?.errorInfo()}\n\nReason: $NETWORK_ERROR",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }
            is ApiResult.Success -> {
                response.value?.let {
                    handleSuccess(resultObj = it)
                }?: DataState.error(
                    message = StateMessage(
                        message = "${stateEvent?.errorInfo()}\n\nReason: $NETWORK_DATA_NULL",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }
        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>
}