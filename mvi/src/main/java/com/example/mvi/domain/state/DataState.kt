package com.john.mvi.domain.state

import com.john.mvi.domain.message.StateMessage

data class DataState<T>(
    var stateMessage: StateMessage? = null,
    var data: T? = null,
    var stateEvent: StateEvent? = null
) {

    companion object {
        fun <T> error(
            message: StateMessage,
            stateEvent: StateEvent?
        ): DataState<T> {
            return DataState(
                stateMessage = message,
                data = null,
                stateEvent = stateEvent
            )
        }

        fun <T> data(
            message: StateMessage?,
            data: T? = null,
            stateEvent: StateEvent?
        ): DataState<T> {
            return DataState(
                stateMessage = message,
                data = data,
                stateEvent = stateEvent
            )
        }
    }
}
