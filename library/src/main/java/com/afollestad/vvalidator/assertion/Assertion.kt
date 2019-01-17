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
package com.afollestad.vvalidator.assertion

import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.form.ConditionList
import com.afollestad.vvalidator.form.isAllMet

/** @author Aidan Follestad (@afollestad) */
abstract class Assertion<T, A> where A : Assertion<T, A> {
  lateinit var container: ValidationContainer
  internal var conditions: ConditionList? = null

  @VisibleForTesting(otherwise = PRIVATE)
  internal var description: String? = null

  /** Returns true if the given view passes the assertion. */
  abstract fun isValid(view: T): Boolean

  /** Gets a user set description if any, falls back to the value of [defaultDescription]. **/
  fun description(): String = description ?: defaultDescription()

  /** Sets a custom assertion description that is used in validation errors. */
  @Suppress("UNCHECKED_CAST")
  fun description(value: String?): A {
    this.description = value
    return this as A
  }

  /** Sets a custom assertion description that is used in validation errors. */
  @Suppress("UNCHECKED_CAST")
  fun description(@StringRes value: Int?) = description(container.getString(value))

  /** A short defaultDescription of what the assertion tests. */
  abstract fun defaultDescription(): String

  /** Returns true if the assertion's condition returns true, or if there is no condition. */
  internal fun isConditionMet() = conditions.isAllMet()
}
