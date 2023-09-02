package com.luther.github.util

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.luther.github.Application.Companion.appContext
import com.luther.github.MainActivity
import com.luther.github.R
import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * This util provides extension functions related to views (activity/fragment) or their lifecycle
 */

fun Fragment.navigateTo(navDirections: NavDirections) = findNavController().navigate(navDirections)

fun Fragment.navigateTo(resID: Int) = findNavController().navigate(resID)

fun Fragment.disableUIInteraction() = requiredMainActivity().disableUIInteraction()

fun Fragment.enableUIInteraction() = requiredMainActivity().enableUIInteraction()

private fun Fragment.requiredMainActivity() = requireActivity() as MainActivity

fun Fragment.getColor(color: Int): Int = ContextCompat.getColor(requireContext(), color)

fun getColor(color: Int): Int = ContextCompat.getColor(appContext, color)

fun Fragment.showShortToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun Fragment.showLongToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

fun showShortToast(context: Context, message: String) =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun getCircularProgressDrawable(mContext: Context? = null): CircularProgressDrawable {
    val context = mContext ?: appContext
    return WeakReference(CircularProgressDrawable(context)).apply {
        get()!!.strokeWidth = 7f
        get()!!.centerRadius = 30f
        get()!!.setColorSchemeColors(
            ContextCompat.getColor(
                context,
                R.color.primary_color
            )
        )
        get()!!.start()
    }.get()!!
}

/**
 * Extension function that help to make a variable null upon onDestroy() method is called
 * in a fragment's / activity's lifecycle. Useful for viewBinding implementation in fragment
 * or for any reference that may cause memory leak if not properly handled.
 */
fun <T> LifecycleOwner.onDestroyNullable(): ReadWriteProperty<LifecycleOwner, T> =
    object : ReadWriteProperty<LifecycleOwner, T>, DefaultLifecycleObserver {

        private var value: T? = null

        init {
            this@onDestroyNullable
                .lifecycle
                .addObserver(this)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            value = null
            this@onDestroyNullable
                .lifecycle
                .removeObserver(this)
            super.onDestroy(owner)
        }

        override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
            this.value = value
        }

        override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
            return value!!
        }
    }