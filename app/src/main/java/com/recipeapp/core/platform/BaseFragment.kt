/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.recipeapp.core.platform

//import com.recipeapp.core.extension.viewContainer
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.haroldadmin.vector.VectorFragment

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseMVIFragment : VectorFragment() {

//    abstract fun layoutId(): Int

    lateinit var viewModelFactory: ViewModelProvider.Factory
    var navigator: Navigator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let { navigator = Navigator(it) }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View =
//        inflater.inflate(layoutId(), container, false)

    open fun onBackPressed() {}

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null
}

abstract class BaseFragment : Fragment() {

    lateinit var viewModelFactory: ViewModelProvider.Factory
    var navigator: Navigator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let { navigator = Navigator(it) }
    }

    open fun onBackPressed() {}

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

//    internal fun showProgress() = progressStatus(View.VISIBLE)

//    internal fun hideProgress() = progressStatus(View.GONE)

//    private fun progressStatus(viewStatus: Int) = with(activity) { if (this is BaseActivity) this.progress.visibility = viewStatus }

//    internal fun notify(@StringRes message: Int) =
//            Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()

//    internal fun notifyWithAction(@StringRes message: Int, @StringRes actionText: Int, action: () -> Any) {
//        val snackBar = Snackbar.make(viewContainer, message, Snackbar.LENGTH_INDEFINITE)
//        snackBar.setAction(actionText) { _ -> action.invoke() }
//        snackBar.setActionTextColor(
//            ContextCompat.getColor(appContext,
//                color.colorAccent))
//        snackBar.show()
//    }
}
