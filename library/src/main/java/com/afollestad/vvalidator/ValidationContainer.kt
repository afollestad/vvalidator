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

  /** Retrieves the value of a string resource from the container context. */
  fun getString(
    @StringRes res: Int?,
    vararg formatArgs: Any
  ): String? {
    return if (res == null) {
      null
    } else {
      context.getString(res, *formatArgs)
    }
  }

  /** Returns the name of the resource ID. */
  internal fun getFieldName(@IdRes id: Int): String {
    return if (id == 0) {
      "(no ID)"
    } else {
      context.resources.getResourceEntryName(id)
    }
  }
}

/** Returns the result [ValidationContainer.findViewById] or throws with a useful exception if it's null. */
fun <T : View> ValidationContainer?.getViewOrThrow(@IdRes id: Int): T {
  return if (this == null) {
    error("Form has been destroyed.")
  } else {
    findViewById(id) ?: error(
        "Unable to find a view by ID ${getFieldName(id)} in the container."
    )
  }
}

/** Returns the container if it's attached, else throws an [IllegalStateException]. */
fun ValidationContainer?.checkAttached(): ValidationContainer {
  return this ?: error("Not attached, form has been destroyed.")
}
