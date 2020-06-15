package com.example.nasamarsrovers

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.nasamarsrovers.ui.gallery.GalleryViewModel
import com.example.nasamarsrovers.ui.gallery.SolPicker
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.OPPORTUNITY
import com.example.nasamarsrovers.utils.SPIRIT
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val galleryViewModel: GalleryViewModel = get()
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.galleryFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sol_drawer_item -> {
                SolPicker().show(supportFragmentManager, "SOL_PICKER")
            }
            R.id.curiosity_drawer_item -> {
                galleryViewModel.setCurrentRover(CURIOSITY)
            }
            R.id.opportunity_drawer_item -> {
                galleryViewModel.setCurrentRover(OPPORTUNITY)
            }
            R.id.spirit_drawer_item -> {
                galleryViewModel.setCurrentRover(SPIRIT)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}