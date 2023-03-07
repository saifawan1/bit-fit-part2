package com.example.bitfit2

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.NotificationChannel
import android.app.NotificationManager
import android.view.View
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.bitfit2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "MainActivity/"

class MainActivity : AppCompatActivity() {
    private val channelId = "com.example.bitfitpart1"
    private lateinit var binding: ActivityMainBinding
    private val description = "I am Going good for the day"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val context = view.context

        val addBtn = view.findViewById<View>(R.id.addBtn)
        // TODO: Redirect to add page on click of button
        addBtn.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            startActivity(intent)
        }


        val fragmentManager: FragmentManager = supportFragmentManager

        // define your fragments here
        val listFragment: Fragment = ListFragment()
        val summaryFragment: Fragment = SummaryFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_list -> fragment = listFragment
                R.id.nav_summary -> fragment = summaryFragment
            }
            replaceFragment(fragment)
            true
        }
        createNotificationChannel()

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.nav_list

        var builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time To Add New Food For THE DAY!")
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)


        var notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifyThread = Thread (Runnable {
            while (true) {
                notificationManager.notify(1234, builder.build())
                Thread.sleep(86400 * 24)
            }
        })
        notifyThread.start()

    }
    private fun replaceFragment(articleListFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, articleListFragment)
        fragmentTransaction.commit()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}