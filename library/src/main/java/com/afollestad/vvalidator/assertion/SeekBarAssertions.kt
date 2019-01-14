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

package com.afollestad.vvalidator.assertion

import android.widget.AbsSeekBar

/** @author Aidan Follestad (@afollestad) */
sealed class SeekBarAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class ProgressAssertion internal constructor() : Assertion<AbsSeekBar>() {
    private var exactly: Int? = null
    private var lessThan: Int? = null
    private var atMost: Int? = null
    private var atLeast: Int? = null
    private var greaterThan: Int? = null
    private var description: String? = null

    /** Asserts the seeker progress is an exact (=) value. */
    fun exactly(
      length: Int,
      description: String? = null
    ) {
      exactly = length
      this.description = description
    }

    /** Asserts the seeker progress is less than (<) a value. */
    fun lessThan(
      length: Int,
      description: String? = null
    ) {
      lessThan = length
      this.description = description
    }

    /** Asserts the seeker progress is at most (<=) a value. */
    fun atMost(
      length: Int,
      description: String? = null
    ) {
      atMost = length
      this.description = description
    }

    /** Asserts the seeker progress is at least (>=) a value. */
    fun atLeast(
      length: Int,
      description: String? = null
    ) {
      atLeast = length
      this.description = description
    }

    /** Asserts the seeker progress is greater (>) than a value. */
    fun greaterThan(
      length: Int,
      description: String? = null
    ) {
      greaterThan = length
      this.description = description
    }

    override fun isValid(view: AbsSeekBar): Boolean {
      val progress = view.progress
      if (exactly != null && progress != exactly!!) {
        return false
      } else if (lessThan != null && progress >= lessThan!!) {
        return false
      } else if (atMost != null && progress > atMost!!) {
        return false
      } else if (atLeast != null && progress < atLeast!!) {
        return false
      } else if (greaterThan != null && progress <= greaterThan!!) {
        return false
      }
      return true
    }

    override fun description(): String {
      return description ?: when {
        exactly != null -> "progress must equal $exactly"
        lessThan != null -> "progress must be less than $lessThan"
        atMost != null -> "progress must be at most $atMost"
        atLeast != null -> "progress must be at least $atLeast"
        greaterThan != null -> "progress must be greater than $greaterThan"
        else -> "progress bound not set"
      }
    }
  }
}
