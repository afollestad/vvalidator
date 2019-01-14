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

package com.afollestad.vvalidator.field.seeker

import android.widget.AbsSeekBar
import androidx.annotation.IdRes
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.CustomViewAssertion
import com.afollestad.vvalidator.assertion.SeekBarAssertions.ProgressAssertion
import com.afollestad.vvalidator.field.FormField

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

  override val view = container.getViewOrThrow<AbsSeekBar>(id)

  /** Asserts on the seeker's progress. */
  fun progress() = assert(ProgressAssertion())

  /** Adds a custom inline assertion for the seek bar. */
  fun assert(
    description: String,
    matcher: (AbsSeekBar) -> Boolean
  ) = assert(CustomViewAssertion(description, matcher))
}
