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

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormBuilder

/**
 * Constructs a validation form for an FragmentActivity (which includes AppCompatActivity).
 *
 * @author Aidan Follestad (@afollestad)
 */
fun FragmentActivity.form(builder: FormBuilder): Form {
  val activity = this
  val container = object : ValidationContainer(activity) {
    override fun <T : View> findViewById(id: Int): T? = activity.findViewById(id)
  }
  return Form(container)
      .apply(builder)
      .also { lifecycle.addObserver(DestroyLifecycleObserver(it)) }
      .start()
}

/**
 * Constructs a validation form for a Fragment.
 *
 * @author Aidan Follestad (@afollestad)
 */
fun Fragment.form(builder: FormBuilder): Form {
  val activity = this.activity ?: error("Fragment is not attached.")
  val container = object : ValidationContainer(activity) {
    override fun <T : View> findViewById(id: Int): T? = view?.findViewById(id)
  }
  return Form(container)
      .apply(builder)
      .also { lifecycle.addObserver(DestroyLifecycleObserver(it)) }
      .start()
}
