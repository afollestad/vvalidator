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

package com.afollestad.vvalidator.field.spinner

import android.widget.Spinner
import androidx.annotation.IdRes
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.CustomViewAssertion
import com.afollestad.vvalidator.assertion.SpinnerAssertions.PositionAtLeastAssertion
import com.afollestad.vvalidator.assertion.SpinnerAssertions.PositionAtMostAssertion
import com.afollestad.vvalidator.assertion.SpinnerAssertions.PositionExactlyAssertion
import com.afollestad.vvalidator.assertion.SpinnerAssertions.PositionGreaterThanAssertion
import com.afollestad.vvalidator.assertion.SpinnerAssertions.PositionLessThanAssertion
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.util.resName

/**
 * Represents a spinner (dropdown) field.
 *
 * @author Aidan Follestad (@afollestad)
 */
class SpinnerField internal constructor(
  container: ValidationContainer,
  @IdRes override val id: Int,
  override val name: String
) : FormField<SpinnerField, Spinner>() {

  override val view = container.findViewById<Spinner>(id) ?: throw IllegalArgumentException(
      "Didn't find view by ID ${id.resName(container.context())} in $container"
  )

  /** Asserts the spinner's position is at an index. */
  fun selectedPositionExactly(
    index: Int,
    description: String? = null
  ) = assert(PositionExactlyAssertion(index, description))

  /** Asserts the spinner's position is less than an index. */
  fun selectedPositionLessThan(
    index: Int,
    description: String? = null
  ) = assert(PositionLessThanAssertion(index, description))

  /** Asserts the spinner's position is at most an index. */
  fun selectedPositionAtMost(
    index: Int,
    description: String? = null
  ) = assert(PositionAtMostAssertion(index, description))

  /** Asserts the spinner's position is at least an index. */
  fun selectedPositionAtLeast(
    index: Int,
    description: String? = null
  ) = assert(PositionAtLeastAssertion(index, description))

  /** Asserts the spinner's position is greater than an index. */
  fun selectedPositionGreaterThan(
    index: Int,
    description: String? = null
  ) = assert(PositionGreaterThanAssertion(index, description))

  /** Adds a custom inline assertion for the spinner. */
  fun assert(
    description: String,
    matcher: (Spinner) -> Boolean
  ) = assert(CustomViewAssertion(description, matcher))
}
