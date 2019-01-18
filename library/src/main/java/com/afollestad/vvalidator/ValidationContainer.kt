/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.afollestad.vvalidator

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes

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
        if (id == 0) return "(no ID)"
        val res = context.resources
        return res.getResourceEntryName(id)
    }
}
