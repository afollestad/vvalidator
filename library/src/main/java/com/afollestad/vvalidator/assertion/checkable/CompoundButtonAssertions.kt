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

package com.afollestad.vvalidator.assertion.checkable

import android.widget.CompoundButton
import com.afollestad.vvalidator.assertion.Assertion

/** @author Aidan Follestad (@afollestad) */
sealed class CompoundButtonAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class CheckedStateAssertion internal constructor(
    val checked: Boolean
  ) : Assertion<CompoundButton, CheckedStateAssertion>() {

    override fun isValid(view: CompoundButton) = view.isChecked == checked

    override fun defaultDescription() = if (checked) {
      "should be checked"
    } else {
      "should not be checked"
    }
  }
}
