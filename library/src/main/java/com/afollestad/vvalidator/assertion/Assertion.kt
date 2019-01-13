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

import android.view.View
import com.afollestad.vvalidator.field.Condition

/** @author Aidan Follestad (@afollestad) */
abstract class Assertion<in T> {
  private var condition: Condition? = null

  /** Returns true if the given view passes the assertion. */
  abstract fun isValid(view: T): Boolean

  /** A short description of what the assertion tests. */
  abstract fun description(): String

  /** Sets a condition delegate which must return true for the assertion to be executed. */
  fun condition(condition: Condition?) {
    this.condition = condition
  }

  /** Returns true if the assertion's condition returns true, or if there is no condition. */
  internal fun isConditionMet() = condition?.invoke() ?: true
}

/** @author Aidan Follestad (@afollestad) */
class CustomViewAssertion<in T>(
  private val description: String,
  private val assertion: (T) -> Boolean
) : Assertion<T>() where T : View {

  init {
    if (description.trim().isEmpty()) {
      throw IllegalArgumentException("Custom assertion descriptions should not be empty.")
    }
  }

  override fun isValid(view: T) = assertion(view)

  override fun description(): String {
    return description
  }
}
