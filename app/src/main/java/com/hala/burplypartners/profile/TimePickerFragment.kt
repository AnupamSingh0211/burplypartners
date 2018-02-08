package com.hala.burplypartners.profile

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import com.hala.burplypartners.R

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2018-02-08
 */
class TimePickerFragment : DialogFragment(), TimePicker.OnTimeChangedListener {
    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {

        Log.e("time", "" + hourOfDay + minute)
    }

    private lateinit var timePicker: TimePicker

    companion object {
        fun newInstance(): TimePickerFragment {
            return TimePickerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_picker, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        timePicker = view.findViewById(R.id.simpleTimePicker)
        timePicker.setOnTimeChangedListener(this)
    }
}