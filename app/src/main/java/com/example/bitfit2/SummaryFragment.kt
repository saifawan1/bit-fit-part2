package com.example.bitfit2

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SummaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clearBtn : Button = view.findViewById(R.id.clear_btn)
        val avgCalTV : TextView = view.findViewById(R.id.avg_cal_val)
        val minCalTV : TextView = view.findViewById(R.id.min_cal_val)
        val maxCalTV : TextView = view.findViewById(R.id.max_cal_val)

        // TODO: Add the Average Calories

        var avg = 0
        var counter = 0
        var min = 0
        var max = 0

        val db = DBHelper(view.context, null)
//        db.populateDB()
        val cursor = db.getFood()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val calCount = cursor.getInt(cursor.getColumnIndex("food_calories"))

                if (min == 0 || calCount < min) min = calCount
                if (max == 0 || calCount > max) max = calCount

                avg += calCount
                counter ++
            }
        }

        if(avg > 0) {
            avg /= counter
        }

        avgCalTV.text = avg.toString()
        minCalTV.text = min.toString()
        maxCalTV.text = max.toString()

        // TODO: On click of button clear the database
        clearBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                (activity?.application as FitApplication).db.FitDao().deleteAll()
            }
        }
    }
}