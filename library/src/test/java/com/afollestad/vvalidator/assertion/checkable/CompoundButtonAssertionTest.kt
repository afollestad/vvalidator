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
package com.afollestad.vvalidator.assertion.checkable

import android.widget.CompoundButton
import com.afollestad.vvalidator.assertion.checkable.CompoundButtonAssertions.CheckedStateAssertion
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertFalse
import com.afollestad.vvalidator.testutil.assertTrue
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

/** @author Aidan Follestad (@afollestad) */
class CompoundButtonAssertionTest {
  private val view = mock<CompoundButton>()

  @Test fun assertIsChecked() {
    val assertion = CheckedStateAssertion(true)

    whenever(view.isChecked).doReturn(false)
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("should be checked")

    whenever(view.isChecked).doReturn(true)
    assertion.isValid(view)
        .assertTrue()
  }

  @Test fun assertNotChecked() {
    val assertion = CheckedStateAssertion(false)

    whenever(view.isChecked).doReturn(false)
    assertion.isValid(view)
        .assertTrue()

    whenever(view.isChecked).doReturn(true)
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("should not be checked")
  }
}
