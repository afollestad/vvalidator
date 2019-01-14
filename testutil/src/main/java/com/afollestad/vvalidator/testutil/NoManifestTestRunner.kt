package com.afollestad.vvalidator.testutil

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Method

@Config(manifest = Config.NONE, sdk = [28])
class NoManifestTestRunner(testClass: Class<*>) : RobolectricTestRunner(testClass) {

  override fun getConfig(method: Method): Config {
    return NoManifestTestRunner::class.java.getAnnotation(Config::class.java)!!
  }
}
