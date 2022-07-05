package com.example.noteappwithkmm.common.utils

sealed class UIComponentType {
    object Dialog: UIComponentType()
    object SnackBar: UIComponentType()
    object None: UIComponentType()
}