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
package com.afollestad.vvalidator.assertion.spinner

import android.widget.Spinner
import com.afollestad.vvalidator.assertion.spinner.SpinnerAssertions.SelectionAssertion
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertFalse
import com.afollestad.vvalidator.testutil.assertTrue
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

/** @author Aidan Follestad (@afollestad) */
class SpinnerAssertionsTest {
  private val view = mock<Spinner>()

  @Test fun exactly() {
    val assertion = SelectionAssertion().apply {
      exactly(5)
    }

    whenever(view.selectedItemPosition).doReturn(6)
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("selection must equal 5")

    whenever(view.selectedItemPosition).doReturn(5)
    assertion.isValid(view)
        .assertTrue()
  }

  @Test fun lessThan() {
    val assertion = SelectionAssertion().apply {
      lessThan(5)
    }

    whenever(view.selectedItemPosition).doReturn(6)
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("selection must be less than 5")

    whenever(view.selectedItemPosition).doReturn(5)
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("selection must be less than 5")

    whenever(view.selectedItemPosition).doReturn(4)
    assertion.isValid(view)
        .assertTrue()
  }

  @Test fun atMost() {
    val assertion = SelectionAssertion().apply {
      atMost(5)
    }

    whenever(view.selectedItemPosition).doReturn(6)
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("selection must be at most 5")

    whenever(view.selectedItemPosition).doReturn(5)
    assertion.isValid(view)
        .assertTrue()

    whenever(view.selectedItemPosition).doReturn(4)
    assertion.isValid(view)
        .assertTrue()
  }

  @Test fun atLeast() {
    val assertion = SelectionAssertion().apply {
      atLeast(5)
    }

    whenever(view.selectedItemPosition).doReturn(4)
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("selection must be at least 5")

    whenever(view.selectedItemPosition).doReturn(5)
    assertion.isValid(view)
        .assertTrue()

    whenever(view.selectedItemPosition).doReturn(6)
    assertion.isValid(view)
        .assertTrue()
  }

  @Test fun greaterThan() {
    val assertion = SelectionAssertion().apply {
      greaterThan(5)
    }

    whenever(view.selectedItemPosition).doReturn(4)
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("selection must be greater than 5")

    whenever(view.selectedItemPosition).doReturn(5)
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("selection must be greater than 5")

    whenever(view.selectedItemPosition).doReturn(6)
    assertion.isValid(view)
        .assertTrue()
  }
}
