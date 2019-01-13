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

package com.afollestad.vvalidator.field.input

import android.widget.AbsSeekBar
import androidx.annotation.IdRes
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.CustomAssertion
import com.afollestad.vvalidator.assertion.SeekProgressAtLeastAssertion
import com.afollestad.vvalidator.assertion.SeekProgressAtMostAssertion
import com.afollestad.vvalidator.assertion.SeekProgressExactlyAssertion
import com.afollestad.vvalidator.assertion.SeekProgressGreaterThanAssertion
import com.afollestad.vvalidator.assertion.SeekProgressLessThanAssertion
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.util.resName

/**
 * Represents an AbsSeekBar field.
 *
 * @author Aidan Follestad (@afollestad)
 */
class SeekField internal constructor(
  container: ValidationContainer,
  @IdRes override val id: Int,
  override val name: String
) : FormField<SeekField, AbsSeekBar>() {

  override val view = container.findViewById<AbsSeekBar>(id) ?: throw IllegalArgumentException(
      "Didn't find view by ID ${id.resName(container.context())} in $container"
  )

  /** Asserts the seek bar's progress is at an exact position. */
  fun progressExactly(index: Int) = assert(SeekProgressExactlyAssertion(index))

  /** Asserts the seek bar's progress is less than a position. */
  fun progressLessThan(index: Int) = assert(SeekProgressLessThanAssertion(index))

  /** Asserts the seek bar's progress is at most a position. */
  fun progressAtMost(index: Int) = assert(SeekProgressAtMostAssertion(index))

  /** Asserts the seek bar's progress is at least a position. */
  fun progressAtLeast(index: Int) = assert(SeekProgressAtLeastAssertion(index))

  /** Asserts the seek bar's progress is greater than a position. */
  fun progressGreaterThan(index: Int) = assert(SeekProgressGreaterThanAssertion(index))

  /** Adds a custom inline assertion for the seek bar. */
  fun assert(
    description: String,
    matcher: (AbsSeekBar) -> Boolean
  ) = assert(CustomAssertion(description, matcher))
}
