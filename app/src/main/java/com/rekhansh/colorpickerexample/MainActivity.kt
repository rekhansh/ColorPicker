package com.rekhansh.colorpickerexample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rekhansh.colorpicker.ColorPickerDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selectedColor = Color.argb(255,0,0,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_color_picker.setOnClickListener{
            ColorPickerDialog(selectedColor,object :ColorPickerDialog.ColorPickerDialogListener{
                override fun onColorChange(red: Float, green: Float, blue: Float, alpha: Float) {
                    selectedColor = Color.argb(alpha.toInt(),red.toInt(),green.toInt(), blue.toInt())
                    colorView.setCardBackgroundColor(
                        Color.argb(
                            alpha.toInt(),
                            red.toInt(),
                            green.toInt(),
                            blue.toInt()
                        )
                    )
                }
            }).show(supportFragmentManager,"colorPicker")
        }
    }
}