package io.github.loveginger.todoclean.util

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, frameId: Int) {
  val transaction = supportFragmentManager.beginTransaction()
  transaction.add(frameId, fragment)
  transaction.commit()
}

fun Fragment.showSnackbar(message: String) {
  if (view != null) {
    Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
  }
}