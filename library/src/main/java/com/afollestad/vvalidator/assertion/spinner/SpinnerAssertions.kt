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

package com.afollestad.vvalidator.assertion.spinner

import android.widget.Spinner
import com.afollestad.vvalidator.assertion.Assertion

/** @author Aidan Follestad (@afollestad) */
sealed class SpinnerAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class SelectionAssertion internal constructor() : Assertion<Spinner, SelectionAssertion>() {
    private var exactly: Int? = null
    private var lessThan: Int? = null
    private var atMost: Int? = null
    private var atLeast: Int? = null
    private var greaterThan: Int? = null

    /** Asserts the spinner selection is an exact (=) value. */
    fun exactly(length: Int): SelectionAssertion {
      exactly = length
      return this
    }

    /** Asserts the spinner selection is less than (<) a value. */
    fun lessThan(length: Int): SelectionAssertion {
      lessThan = length
      return this
    }

    /** Asserts the spinner selection is at most (<=) a value. */
    fun atMost(length: Int): SelectionAssertion {
      atMost = length
      return this
    }

    /** Asserts the spinner selection is at least (>=) a value. */
    fun atLeast(length: Int): SelectionAssertion {
      atLeast = length
      return this
    }

    /** Asserts the spinner selection is greater (>) than a value. */
    fun greaterThan(length: Int): SelectionAssertion {
      greaterThan = length
      return this
    }

    override fun isValid(view: Spinner): Boolean {
      val position = view.selectedItemPosition
      return when {
        exactly != null && position != exactly!! -> false
        lessThan != null && position >= lessThan!! -> false
        atMost != null && position > atMost!! -> false
        atLeast != null && position < atLeast!! -> false
        greaterThan != null && position <= greaterThan!! -> false
        else -> true
      }
    }

    override fun defaultDescription() = when {
      exactly != null -> "selection must equal $exactly"
      lessThan != null -> "selection must be less than $lessThan"
      atMost != null -> "selection must be at most $atMost"
      atLeast != null -> "selection must be at least $atLeast"
      greaterThan != null -> "selection must be greater than $greaterThan"
      else -> "selection bound not set"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class PositionLessThanAssertion internal constructor(
    private val ceil: Int
  ) : Assertion<Spinner, PositionLessThanAssertion>() {

    override fun isValid(view: Spinner) = view.selectedItemPosition < ceil

    override fun defaultDescription() = "selection should be less than $ceil"
  }

  /** @author Aidan Follestad (@afollestad) */
  class PositionAtMostAssertion internal constructor(
    private val ceil: Int
  ) : Assertion<Spinner, PositionAtMostAssertion>() {

    override fun isValid(view: Spinner) = view.selectedItemPosition <= ceil

    override fun defaultDescription() = "selection should be at most $ceil"
  }

  /** @author Aidan Follestad (@afollestad) */
  class PositionAtLeastAssertion internal constructor(
    private val floor: Int
  ) : Assertion<Spinner, PositionAtLeastAssertion>() {

    override fun isValid(view: Spinner) = view.selectedItemPosition >= floor

    override fun defaultDescription() = "selection should be at least $floor"
  }

  /** @author Aidan Follestad (@afollestad) */
  class PositionGreaterThanAssertion internal constructor(
    private val floor: Int
  ) : Assertion<Spinner, PositionGreaterThanAssertion>() {

    override fun isValid(view: Spinner) = view.selectedItemPosition > floor

    override fun defaultDescription() = "selection should be greater than $floor"
  }
}
