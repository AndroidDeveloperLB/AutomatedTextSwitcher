package com.example.user.automated_text_switcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lb.automated_text_switcher.library.AutomatedTextSwitcher
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        automatedTextSwitcher.setTextsToShow(false, "ABC", "DEF")

        singleTextButton.setOnClickListener {
            (automatedTextSwitcher as AutomatedTextSwitcher).setCurrentText("Single")

//            automatedTextSwitcher.setTextsToShow(true, "Single")
        }
        multipleTextButton.setOnClickListener {
            automatedTextSwitcher.setTextsToShow(true, "multi 1", "multi 2")
        }
    }
}
