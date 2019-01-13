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
import androidx.fragment.app.Fragment
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormBuilder

/** @author Aidan Follestad (@afollestad) */
interface ValidationContainer {
  fun context(): Context

  fun <T : View> findViewById(@IdRes id: Int): T?
}

/**
 * Constructs a validation form for an Activity.
 *
 * @author Aidan Follestad (@afollestad)
 */
fun Activity.form(
  builder: FormBuilder
): Form {
  val container = object : ValidationContainer {
    override fun context(): Context = this@form

    override fun <T : View> findViewById(id: Int): T? = this@form.findViewById(id)
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
  val container = object : ValidationContainer {
    override fun context(): Context {
      return this@form.activity ?: throw IllegalStateException("Fragment is not attached.")
    }

    override fun <T : View> findViewById(id: Int): T? = this@form.view?.findViewById(id)
  }
  val newForm = Form(container)
  builder(newForm)
  return newForm
}
