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
@file:Suppress("MemberVisibilityCanBePrivate")

package com.afollestad.vvalidator.form

typealias Condition = () -> Boolean

internal typealias ConditionList = List<Condition>

/** @author Aidan Follestad (@afollestad) */
internal class ConditionStack {
  private val stack = mutableListOf<Condition>()

  /** Pushes a condition into the stack. */
  fun push(condition: Condition) {
    stack.add(condition)
  }

  /** Pops the stack. */
  fun pop(): Condition {
    val top = peek()
    stack.removeAt(stack.size - 1)
    return top
  }

  /** Peeks the top item on the stack. */
  fun peek() = stack[stack.size - 1]

  /** Converts the current state of the stack to a immutable List. */
  fun asList(): List<Condition> = stack.toList()
}

/** Returns true if all conditions in the List return true. */
internal fun ConditionList?.isAllMet(): Boolean {
  return if (this == null || isEmpty()) {
    true
  } else {
    all { it() }
  }
}
