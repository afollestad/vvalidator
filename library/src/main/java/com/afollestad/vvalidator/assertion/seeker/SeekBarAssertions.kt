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

package com.afollestad.vvalidator.assertion.seeker

import android.widget.AbsSeekBar
import com.afollestad.vvalidator.assertion.Assertion

/** @author Aidan Follestad (@afollestad) */
sealed class SeekBarAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class ProgressAssertion internal constructor() : Assertion<AbsSeekBar, ProgressAssertion>() {
    private var exactly: Int? = null
    private var lessThan: Int? = null
    private var atMost: Int? = null
    private var atLeast: Int? = null
    private var greaterThan: Int? = null

    /** Asserts the seeker progress is an exact (=) value. */
    fun exactly(length: Int): ProgressAssertion {
      exactly = length
      return this
    }

    /** Asserts the seeker progress is less than (<) a value. */
    fun lessThan(length: Int): ProgressAssertion {
      lessThan = length
      return this
    }

    /** Asserts the seeker progress is at most (<=) a value. */
    fun atMost(length: Int): ProgressAssertion {
      atMost = length
      return this
    }

    /** Asserts the seeker progress is at least (>=) a value. */
    fun atLeast(length: Int): ProgressAssertion {
      atLeast = length
      return this
    }

    /** Asserts the seeker progress is greater (>) than a value. */
    fun greaterThan(length: Int): ProgressAssertion {
      greaterThan = length
      return this
    }

    override fun isValid(view: AbsSeekBar): Boolean {
      val progress = view.progress
      return when {
        exactly != null && progress != exactly!! -> false
        lessThan != null && progress >= lessThan!! -> false
        atMost != null && progress > atMost!! -> false
        atLeast != null && progress < atLeast!! -> false
        greaterThan != null && progress <= greaterThan!! -> false
        else -> true
      }
    }

    override fun defaultDescription() = when {
      exactly != null -> "progress must equal $exactly"
      lessThan != null -> "progress must be less than $lessThan"
      atMost != null -> "progress must be at most $atMost"
      atLeast != null -> "progress must be at least $atLeast"
      greaterThan != null -> "progress must be greater than $greaterThan"
      else -> "progress bound not set"
    }
  }
}
