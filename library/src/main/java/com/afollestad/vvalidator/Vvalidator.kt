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
@file:Suppress("unused")

package com.afollestad.vvalidator

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormBuilder
import com.afollestad.vvalidator.util.resName

/** @author Aidan Follestad (@afollestad) */
abstract class ValidationContainer(val context: Context) {

  /** Retrieves a view from the container view by its ID, which can be null.. */
  abstract fun <T : View> findViewById(@IdRes id: Int): T?

  /** Returns the result [findViewById] or throws with a useful exception if it's null. */
  fun <T : View> getViewOrThrow(@IdRes id: Int): T {
    return findViewById(id) ?: throw IllegalStateException(
        "Unable to find a view by ID ${id.resName(context)} in the container."
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

/**
 * Constructs a validation form for an Activity.
 *
 * @author Aidan Follestad (@afollestad)
 */
fun Activity.form(
  builder: FormBuilder
): Form {
  val activity = this
  val container = object : ValidationContainer(activity) {
    override fun <T : View> findViewById(id: Int): T? = activity.findViewById(id)
  }
  val newForm = Form(container)
  builder(newForm)
  return newForm
}

/**
 * Constructs a validation form for a Fragment.
 *
 * @author Aidan Follestad (@afollestad)
 */
fun Fragment.form(
  builder: FormBuilder
): Form {
  val activity = this.activity ?: throw IllegalStateException("Fragment is not attached.")
  val container = object : ValidationContainer(activity) {
    override fun <T : View> findViewById(id: Int): T? = view?.findViewById(id)
  }
  val newForm = Form(container)
  builder(newForm)
  return newForm
}
