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
package com.afollestad.vvalidator.form

import android.view.MenuItem
import android.view.View

/** @author Aidan Follestad (@afollestad) */
internal class SubmitWrapper(
  private var menuItem: MenuItem? = null,
  private var view: View? = null
) {
  init {
    check(menuItem != null || view != null) {
      "SubmitWrapper needs a MenuItem OR View."
    }
  }

  var isEnabled: Boolean
    get() = (menuItem?.isEnabled ?: view?.isEnabled) ?: false
    set(value) {
      menuItem?.isEnabled = value
      view?.isEnabled = value
    }
}
