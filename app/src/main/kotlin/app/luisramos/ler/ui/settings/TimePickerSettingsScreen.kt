package app.luisramos.ler.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.lifecycle.MutableLiveData
import app.luisramos.ler.R
import app.luisramos.ler.ui.screen.Screen
import app.luisramos.ler.ui.screen.goBack
import com.squareup.contour.ContourLayout

data class TimeUiModel(
    val hour: Int,
    val minute: Int
)

class TimePickerSettingsScreen(
    private val timeLiveData: MutableLiveData<TimeUiModel>
) : Screen() {
    override fun createView(container: ViewGroup): View =
        TimePickerSettingsView(container.context).apply {
            timeLiveData.value?.let { model ->
                val (hour, minute) = model
                timePicker.hour = hour
                timePicker.minute = minute
            }

            button.setOnClickListener {
                timeLiveData.value = TimeUiModel(timePicker.hour, timePicker.minute)
                goBack()
            }
        }
}

class TimePickerSettingsView(
    context: Context,
    attrs: AttributeSet? = null
) : ContourLayout(context, attrs) {

    val timePicker = TimePicker(context)

    val button = Button(context).apply {
        setText(R.string.settings_save_button)
    }

    init {
        setBackgroundResource(R.color.white)

        timePicker.layoutBy(
            x = matchParentX(),
            y = topTo { 0.ydip }.heightOf { timePicker.preferredHeight() }
        )
        button.layoutBy(
            x = matchParentX(marginLeft = 16.dip, marginRight = 16.dip),
            y = topTo { timePicker.bottom() + 16.dip }.heightOf { button.preferredHeight() }
        )
    }
}