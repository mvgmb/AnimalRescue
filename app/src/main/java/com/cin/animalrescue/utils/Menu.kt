package com.cin.animalrescue.utils

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import com.cin.animalrescue.R
import com.cin.animalrescue.ui.component.animal_add.AnimalAddActivity
import com.cin.animalrescue.ui.component.animal_list.AnimalListActivity
import com.cin.animalrescue.ui.component.signin.AuthActivity

fun handleMenuItemClick(ctx: Activity, item: MenuItem): Boolean =
    when (item.itemId) {
        R.id.animal_add -> {
            ctx.startActivity(Intent(ctx, AnimalAddActivity::class.java))
            true
        }
        R.id.animal_list -> {
            ctx.startActivity(Intent(ctx, AnimalListActivity::class.java))
            ctx.finish()
            true
        }
        R.id.my_animal_list -> {
            Toast.makeText(ctx, "Not implemented", Toast.LENGTH_SHORT).show()
//            ctx.finish()
            true
        }
        R.id.user -> {
            ctx.startActivity(Intent(ctx, AuthActivity::class.java))
            ctx.finish()
            true
        }
        else -> false
    }
