package com.task.ui.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.cin.animalrescue.R

abstract class BaseActivity : AppCompatActivity() {
    protected abstract fun initViewBinding()
    abstract fun setBindings()
    abstract fun observeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        setBindings()
        observeViewModel()
    }
}
