package io.github.loveginger.todoclean.tasks

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View


class ScrollChildSwipeRefreshLayout(context: Context, attrs: AttributeSet?) :
  SwipeRefreshLayout(context, attrs) {

  var scrollUpChild: View? = null

  constructor(context: Context) : this(context, null)

  override fun canChildScrollUp(): Boolean {
    if (scrollUpChild != null) {
      return ViewCompat.canScrollVertically(scrollUpChild, -1);
    }
    return super.canChildScrollUp()
  }
}