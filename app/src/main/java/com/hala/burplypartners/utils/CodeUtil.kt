package com.hala.burplypartners.utils

import android.content.Context
import android.widget.Toast

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2018-01-16
 */
class CodeUtil {

    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        }
    }


}