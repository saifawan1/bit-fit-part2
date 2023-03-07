package com.example.bitfit2

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ListFragment: Fragment()  {
    private val bitFit = mutableListOf<DisplayFit>()
    private lateinit var fitRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_list)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Change this statement to store the view in a variable instead of a return statement
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        // TODO: Defining activity_main items
        fitRV = view.findViewById(R.id.FitView)
        return view
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = view.context
        // TODO: Set up BitFit Adapter with fits
        val bitFitAdapter = FitAdapter(context, bitFit)
        fitRV.adapter = bitFitAdapter
        fitRV.layoutManager = LinearLayoutManager(context).also {
            val dividerItemDecoration = DividerItemDecoration(context, it.orientation)
            fitRV.addItemDecoration(dividerItemDecoration)
        }

        // TODO: Get and display the data
        lifecycleScope.launch {
            (activity?.application as FitApplication).db.FitDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    DisplayFit(
                        entity.food_name,
                        entity.food_calories,
                    )
                }.also { mappedList ->
                    bitFit.clear()
                    bitFit.addAll(mappedList)
                    bitFitAdapter.notifyDataSetChanged()
                }
            }
        }

    }
}