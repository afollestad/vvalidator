package com.afollestad.vvalidator

import android.content.Context
import android.view.View

/** @author Aidan Follestad (@afollestad) */
abstract class ValidationContainer(private val context: Context) {

    /** Retrieves a view from the container view by its ID, which can be null.. */
    abstract fun <T : View> findViewById(@IdRes id: Int): T?

    /** Returns the result [findViewById] or throws with a useful exception if it's null. */
    fun <T : View> getViewOrThrow(@IdRes id: Int): T {
        return findViewById(id) ?: throw IllegalStateException(
            "Unable to find a view by ID ${getFieldName(id)} in the container."
        )
    }

    /** Retrieves the value of a string resource. */
    internal fun getString(@StringRes res: Int?): String? {
        return if (res == null) {
            null
        } else {
            context.getString(res)
        }
    }

    /** Returns the name of the resource ID. */
    internal fun getFieldName(@IdRes id: Int): String {
        val res = context.resources
        return res.getResourceEntryName(id)
    }
}