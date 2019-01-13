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

import android.widget.Spinner

/** @author Aidan Follestad (@afollestad) */
sealed class SpinnerAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class SelectionAssertion : Assertion<Spinner>() {
    private var exactly: Int? = null
    private var lessThan: Int? = null
    private var atMost: Int? = null
    private var atLeast: Int? = null
    private var greaterThan: Int? = null
    private var description: String? = null

    /** Asserts the spinner selection is an exact (=) value. */
    fun exactly(
      length: Int,
      description: String? = null
    ) {
      exactly = length
      this.description = description
    }

    /** Asserts the spinner selection is less than (<) a value. */
    fun lessThan(
      length: Int,
      description: String? = null
    ) {
      lessThan = length
      this.description = description
    }

    /** Asserts the spinner selection is at most (<=) a value. */
    fun atMost(
      length: Int,
      description: String? = null
    ) {
      atMost = length
      this.description = description
    }

    /** Asserts the spinner selection is at least (>=) a value. */
    fun atLeast(
      length: Int,
      description: String? = null
    ) {
      atLeast = length
      this.description = description
    }

    /** Asserts the spinner selection is greater (>) than a value. */
    fun greaterThan(
      length: Int,
      description: String? = null
    ) {
      greaterThan = length
      this.description = description
    }

    override fun isValid(view: Spinner): Boolean {
      val position = view.selectedItemPosition
      if (exactly != null && position != exactly!!) {
        return false
      } else if (lessThan != null && position >= lessThan!!) {
        return false
      } else if (atMost != null && position > atMost!!) {
        return false
      } else if (atLeast != null && position < atLeast!!) {
        return false
      } else if (greaterThan != null && position <= greaterThan!!) {
        return false
      }
      return true
    }

    override fun description(): String {
      return description ?: when {
        exactly != null -> "selection must equal $exactly"
        lessThan != null -> "selection must be less than $lessThan"
        atMost != null -> "selection must be at most $atMost"
        atLeast != null -> "selection must be at least $atLeast"
        greaterThan != null -> "selection must be greater than $greaterThan"
        else -> "selection bound not set"
      }
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class PositionLessThanAssertion(
    private val ceil: Int,
    private val description: String?
  ) : Assertion<Spinner>() {
    override fun isValid(view: Spinner): Boolean {
      return view.selectedItemPosition < ceil
    }

    override fun description(): String {
      return description ?: "selection should be less than $ceil"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class PositionAtMostAssertion(
    private val ceil: Int,
    private val description: String?
  ) : Assertion<Spinner>() {
    override fun isValid(view: Spinner): Boolean {
      return view.selectedItemPosition <= ceil
    }

    override fun description(): String {
      return description ?: "selection should be at most $ceil"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class PositionAtLeastAssertion(
    private val floor: Int,
    private val description: String?
  ) : Assertion<Spinner>() {
    override fun isValid(view: Spinner): Boolean {
      return view.selectedItemPosition >= floor
    }

    override fun description(): String {
      return description ?: "selection should be at least $floor"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class PositionGreaterThanAssertion(
    private val floor: Int,
    private val description: String?
  ) : Assertion<Spinner>() {
    override fun isValid(view: Spinner): Boolean {
      return view.selectedItemPosition > floor
    }

    override fun description(): String {
      return description ?: "selection should be greater than $floor"
    }
  }
}
