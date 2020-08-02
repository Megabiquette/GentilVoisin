package com.albanfontaine.gentilvoisin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.albanfontaine.gentilvoisin.auth.login.LoginActivity
import com.albanfontaine.gentilvoisin.helper.Extensions.Companion.toast
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = drawer_layout
        navController = findNavController(R.id.navHostFragment)
        findViewById<NavigationView>(R.id.navView).apply {
            setupWithNavController(navController)
            setNavigationItemSelectedListener(this@MainActivity)
        }

        val topDestinations = setOf(
            R.id.menuLastJobsList,
            R.id.menuOffersJobsList,
            R.id.menuDemandsJobsList,
            R.id.menuAddJob,
            R.id.menuMyJobsList,
            R.id.menuMyMessages,
            R.id.menuProfile
        )
        appBarConfiguration = AppBarConfiguration(topDestinations, drawerLayout)
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)
    }

    private fun showLogoutDialog() {
        val dialog = AlertDialog.Builder(this, R.style.DialogTheme)
            .setMessage(R.string.menu_logout_question)
            .setPositiveButton(R.string.menu_logout_yes) { _, _ ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton(R.string.menu_logout_no) { _, _ ->
                // Cancel
            }
            .create()
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuLogout) {
            showLogoutDialog()
        }
        return true
    }
}
