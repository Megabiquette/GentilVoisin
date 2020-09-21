package com.albanfontaine.gentilvoisin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.albanfontaine.gentilvoisin.auth.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_menu_header.view.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureToolbar()
        drawerLayout = drawer_layout
        navController = findNavController(R.id.navHostFragment)
        findViewById<NavigationView>(R.id.navView).apply {
            setupWithNavController(navController)
            headerView = this.getHeaderView(0)
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

        configureDrawerHeader()
    }

    private fun configureDrawerHeader() {
        val user = FirebaseAuth.getInstance().currentUser!!

        headerView.drawerUsername.text = user.displayName
        headerView.drawerEmail.text = user.email

        Glide.with(this)
            .load(user.photoUrl)
            .centerCrop()
            .circleCrop()
            .placeholder(ContextCompat.getDrawable(this, R.drawable.ic_person_white))
            .into(headerView.drawerImage)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuLogout) {
            showLogoutDialog()
        } else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        return onOptionsItemSelected(item)
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this, R.style.DialogTheme)
            .setMessage(R.string.menu_logout_question)
            .setPositiveButton(R.string.menu_logout_yes) { _, _ ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton(R.string.common_cancel) { _, _ ->
                // Cancel
            }
            .create()
            .show()
    }

    private fun configureToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        val ab : ActionBar? = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(false)
    }
}
