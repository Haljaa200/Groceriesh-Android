package com.haljaa200.groceriesh.util

import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentActivity
import com.haljaa200.groceriesh.R
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern


object Tools {

    fun <T> handleRetrofitResponse(response: Response<T>): Resource<T> {
        if (response.isSuccessful) {
            response.body()?.let { resultReponse ->
                return Resource.Success(resultReponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun isPhoneValid(phoneNumber: String): Boolean {
        val phone = phoneNumber.replace(" ", "").replace("-", "")
        return if (phone.isNotEmpty()) {
            val isValid: Boolean
            val expression = "^[+(0)][0-9]{6,14}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(phone)
            isValid = matcher.matches()

            isValid

        } else false
    }

    fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun View.showKeyboard() {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        clearFocus()
        requestFocus()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun View.setMargins(
        left: Int = this.marginLeft,
        top: Int = this.marginTop,
        right: Int = this.marginRight,
        bottom: Int = this.marginBottom,
    ) {
        layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(left, top, right, bottom)
        }
    }

    fun setNavbarColor(activity: FragmentActivity, color: Int) {
        activity.window.navigationBarColor = ResourcesCompat.getColor(activity.resources, color, activity.theme)
    }

    fun setStatusbarColor(activity: FragmentActivity, color: Int) {
        activity.window.statusBarColor = ResourcesCompat.getColor(activity.resources, color, activity.theme)
    }

    fun getDrawable(context: Context, id: Int): Drawable {
        return ResourcesCompat.getDrawable(context.resources, id, context.theme)!!
    }

    fun handleAPIResponse(success: Boolean, onSuccess: () -> Unit, onFailure: () -> Unit) {
        when (success) {
            true -> onSuccess()
            else -> onFailure()
        }
    }

    fun generateHash(size: Int): String {
        val possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_1234567890abcdefghijklmnopqrstuvwxyz@#*"
        val min = 0
        val max = possible.length - 1
        var hash = ""
        for (i in 0 until size) { hash += possible[Random().nextInt(max - min + 1) + min] }

        return hash
    }

    fun Long.formatPrice() = NumberFormat.getIntegerInstance().format(this)

    fun shakeView(view: View) {
        val shake = AnimationUtils.loadAnimation(view.context, R.anim.shake)
        view.startAnimation(shake)
    }

    fun String.formatPrice(unit: String = ""): String {
        val end = if (unit.isEmpty()) "" else " $unit"
        if (this.endsWith(".00")) {
            return NumberFormat.getIntegerInstance().format(this.dropLast(3).toInt()) + end
        } else {
            val decimalValue = this
            return NumberFormat.getIntegerInstance().format(decimalValue.toFloat()) + end
        }
    }

    fun String.removeRedundantZeros(): Long {
        if (this.endsWith(".00")) {
            return this.dropLast(3).toLong()
        } else {
            return this.toLong()
        }
    }

    fun getColorStateList(context: Context, color: Int): ColorStateList? {
        return ResourcesCompat.getColorStateList(context.resources, color, context.theme)
    }

    fun getColor(context: Context, color: Int): Int {
        return ResourcesCompat.getColor(context.resources, color, context.theme)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun loadingDialog(context: Context, cancelable: Boolean = false): Dialog {
        val dialog = Dialog(context)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_loading)
        val params = dialog.window!!.attributes
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params
        dialog.setCancelable(cancelable)

        return dialog
    }


}