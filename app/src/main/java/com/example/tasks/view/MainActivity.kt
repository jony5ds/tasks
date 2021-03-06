package com.example.tasks.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.tasks.R
import com.example.tasks.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this,TaskFormActivity::class.java))
        }

        // Navegação
        setupNavigation()

        // Observadores
        observe()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.loadUserName()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupNavigation() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_all_tasks, R.id.nav_next_tasks, R.id.nav_expired, R.id.nav_logout),
            drawerLayout
        )

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId ) {
                R.id.nav_logout -> {
                    mViewModel.logout()
                    true
                }
                else -> {
                    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }
                    false
                }
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

    private fun observe() {
        mViewModel.userName.observe(this, Observer {
            val nav = findViewById<NavigationView>(R.id.nav_view)
            val header = nav.getHeaderView(0)

            header.findViewById<TextView>(R.id.text_name).text = it
        })

        mViewModel.logout.observe(this, Observer {
            if (it) {
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        })
    }
}
