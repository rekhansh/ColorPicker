package com.rekhansh.colorpickerexample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rekhansh.colorpicker.ColorPickerDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selectedColor = Color.argb(255, 0, 0, 255)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        colorView.setCardBackgroundColor(selectedColor)
        colorView.setOnClickListener {
            ColorPickerDialog(selectedColor, object : ColorPickerDialog.ColorPickerDialogListener {
                override fun onSelectColor(color: Int) {
                    selectedColor = color
                    colorView.setCardBackgroundColor(color)
                }
            }).show(supportFragmentManager, "colorPicker")
        }
    }
}