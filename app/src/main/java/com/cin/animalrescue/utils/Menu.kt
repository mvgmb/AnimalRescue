package com.cin.animalrescue.utils

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import com.cin.animalrescue.R
import com.cin.animalrescue.ui.component.animal_add.AnimalAddActivity
import com.cin.animalrescue.ui.component.animal_list.AnimalListActivity
import com.cin.animalrescue.ui.component.user.UserActivity

fun handleMenuItemClick(ctx: Activity, item: MenuItem): Boolean {
    Logger.logInfo(ctx.toString())
    when (item.itemId) {
        R.id.animal_add -> {
            ctx.startActivity(Intent(ctx, AnimalAddActivity::class.java))
            ctx.finish()
            return true
        }
        R.id.animal_list -> {
            try {
                ctx.startActivity(Intent(ctx, AnimalListActivity::class.java))
            } catch (e: Exception) {
                Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show()
            }
            ctx.finish()
            return true
        }
        R.id.my_animal_list -> {
            Toast.makeText(ctx, "Not implemented", Toast.LENGTH_SHORT).show()
//            ctx.finish()
            return false
        }
        R.id.user -> {
            ctx.startActivity(Intent(ctx, UserActivity::class.java))
            ctx.finish()
            return true
        }
        else -> return false
    }
}