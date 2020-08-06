package com.rekhansh.colorpicker

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View.inflate
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider

class ColorPickerDialog(private val selectedColor : Int,private val colorPickerDialogListener: ColorPickerDialogListener) :
    DialogFragment() {
    private var alpha = 255f
    private var red = 0f
    private var blue = 0f
    private var green = 0f


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            alpha = (selectedColor shr 24 and 0xff).toFloat()
            red = (selectedColor shr 16 and 0xff).toFloat()
            green = (selectedColor shr 8 and 0xff).toFloat()
            blue = (selectedColor and 0xff).toFloat()

            val builder = MaterialAlertDialogBuilder(it)
            val view = inflate(context, R.layout.dialog_color_picker, null)
            val coloImage = view.findViewById<ImageView>(R.id.dialog_color_picker_color)
            coloImage.setBackgroundColor(
                Color.argb(
                    alpha.toInt(),
                    red.toInt(),
                    green.toInt(),
                    blue.toInt()
                )
            )
            view.findViewById<Slider>(R.id.dialog_color_picker_alpha).value = alpha
            view.findViewById<Slider>(R.id.dialog_color_picker_alpha).addOnChangeListener { _, value, _ ->
                    alpha = value
                    coloImage.setBackgroundColor(
                        Color.argb(
                            alpha.toInt(),
                            red.toInt(),
                            green.toInt(),
                            blue.toInt()
                        )
                    )
                }
            view.findViewById<Slider>(R.id.dialog_color_picker_red).value = red
            view.findViewById<Slider>(R.id.dialog_color_picker_red).addOnChangeListener { _, value, _ ->
                    red = value
                    coloImage.setBackgroundColor(
                        Color.argb(
                            alpha.toInt(),
                            red.toInt(),
                            green.toInt(),
                            blue.toInt()
                        )
                    )
                }
            view.findViewById<Slider>(R.id.dialog_color_picker_green).value = green
            view.findViewById<Slider>(R.id.dialog_color_picker_green).addOnChangeListener { _, value, _ ->
                        green = value
                        coloImage.setBackgroundColor(
                            Color.argb(
                                alpha.toInt(),
                                red.toInt(),
                                green.toInt(),
                                blue.toInt()
                            )
                        )
                    }
            view.findViewById<Slider>(R.id.dialog_color_picker_blue).value = blue
            view.findViewById<Slider>(R.id.dialog_color_picker_blue).addOnChangeListener { _, value, _ ->
                    blue = value
                    coloImage.setBackgroundColor(
                        Color.argb(
                            alpha.toInt(),
                            red.toInt(),
                            green.toInt(),
                            blue.toInt()
                        )
                    )
                }
            builder.setView(view)
                .setPositiveButton("Ok") { dialog, _ ->
                    colorPickerDialogListener.onColorChange(red, green, blue, alpha)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // User cancelled the dialog
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface ColorPickerDialogListener {
        fun onColorChange(red: Float, green: Float, blue: Float, alpha: Float)
    }

}