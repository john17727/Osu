package com.john.mvi.domain.message

sealed class UIComponentType {

    object Toast : UIComponentType()

    object Dialog : UIComponentType()

    object None : UIComponentType()
}
