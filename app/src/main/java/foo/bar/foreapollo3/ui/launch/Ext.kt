package foo.bar.foreapollo3.ui.launch

import android.content.Context
import android.widget.Toast
import foo.bar.foreapollo3.message.ErrorMessage

fun Context.showToast(message: String?) {
    message?.let {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

fun Context.showToast(message: ErrorMessage?) {
    message?.let {
        Toast.makeText(this, message.localisedMessage, Toast.LENGTH_LONG).show()
    }
}