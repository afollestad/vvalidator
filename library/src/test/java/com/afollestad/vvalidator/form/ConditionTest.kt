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
package com.afollestad.vvalidator.form

import com.afollestad.vvalidator.testutil.assertEmpty
import com.afollestad.vvalidator.testutil.assertFalse
import com.afollestad.vvalidator.testutil.assertSameAs
import com.afollestad.vvalidator.testutil.assertSize
import com.afollestad.vvalidator.testutil.assertTrue
import com.afollestad.vvalidator.testutil.second
import org.junit.Test

/** @author Aidan Follestad (@afollestad) */
class ConditionTest {

  @Test fun conditionStack() {
    val condition1: Condition = { true }
    val condition2: Condition = { false }
    val stack = ConditionStack().apply {
      push(condition1)
      push(condition2)
    }

    stack.asList()
        .run {
          assertSize(2)
          first().assertSameAs(condition1)
          second().assertSameAs(condition2)
        }
    stack.pop()
        .assertSameAs(condition2)
    stack.pop()
        .assertSameAs(condition1)
    stack.asList()
        .assertEmpty()
  }

  @Test fun isAllMet() {
    val condition1: Condition = { true }
    val condition2: Condition = { false }
    val stack = ConditionStack().apply {
      push(condition1)
      push(condition2)
    }

    stack.asList()
        .isAllMet()
        .assertFalse()
    stack.pop()
    stack.asList()
        .isAllMet()
        .assertTrue()
  }
}
