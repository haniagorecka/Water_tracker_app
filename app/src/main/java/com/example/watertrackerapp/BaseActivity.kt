package com.example.watertrackerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
open class BaseActivity (): AppCompatActivity()
{
    fun showErrorSnackBar(message: String, errorMessage: Boolean)
    {

        val snackbar =
            Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view

        if(errorMessage==true) //Jesli jest to error message ustawiamy specjalny kolor
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,
                R.color.colorSnackBarError))
        }
        else //Jesli to nie jest error message ustawiamy inny kolor
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,
                R.color.colorSnackBarSuccess))
        }

        snackbar.show()
    }
}


