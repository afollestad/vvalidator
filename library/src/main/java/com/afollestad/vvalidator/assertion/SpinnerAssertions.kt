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
  class PositionExactlyAssertion(
    private val index: Int,
    private val description: String?
  ) : Assertion<Spinner>() {
    override fun isValid(view: Spinner): Boolean {
      return view.selectedItemPosition == index
    }

    override fun description(): String {
      return description ?: "position $index should be selected"
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
