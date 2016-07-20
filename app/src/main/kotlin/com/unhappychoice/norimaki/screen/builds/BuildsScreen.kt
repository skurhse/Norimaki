package com.unhappychoice.norimaki.screen.builds

import com.github.unhappychoice.circleci.CircleCIAPIClient
import com.github.unhappychoice.circleci.response.Build
import com.unhappychoice.norimaki.MainActivity
import com.unhappychoice.norimaki.R
import com.unhappychoice.norimaki.extension.Variable
import com.unhappychoice.norimaki.extension.bindTo
import com.unhappychoice.norimaki.extension.subscribeOnIoObserveOnUI
import com.unhappychoice.norimaki.extension.withLog
import com.unhappychoice.norimaki.preference.APITokenPreference
import com.unhappychoice.norimaki.scope.ViewScope
import com.unhappychoice.norimaki.screen.Screen
import mortar.MortarScope
import mortar.ViewPresenter
import javax.inject.Inject

class BuildsScreen : Screen() {
  override fun getLayoutResource() = R.layout.builds_view

  @dagger.Component(dependencies = arrayOf(MainActivity.Component::class))
  @ViewScope
  interface Component {
    fun inject(view: BuildsView)
  }

  @ViewScope
  class Presenter @Inject constructor() : ViewPresenter<BuildsView>() {
    @Inject lateinit var activity: MainActivity
    private val token by lazy { APITokenPreference(activity).token }
    private val api by lazy { CircleCIAPIClient(token) }
    private val builds = Variable<List<Build>>(listOf())

    override fun onEnterScope(scope: MortarScope?) {
      super.onEnterScope(scope)
      getBuilds()
    }

    override fun onExitScope() {
      super.onExitScope()
    }

    fun getBuilds() {
      api.client().getRecentBuilds().subscribeOnIoObserveOnUI().withLog().bindTo(builds)
    }
  }
}