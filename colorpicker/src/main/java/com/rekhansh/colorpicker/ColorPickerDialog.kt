package com.rekhansh.colorpicker

import android.R.attr.label
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.inflate
import android.widget.ImageView
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlin.properties.Delegates


class ColorPickerDialog(
    private val selectedColor: Int,
    private val colorPickerDialogListener: ColorPickerDialogListener
) :
    DialogFragment() {
    private var colorAlpha = 255f
    private val color: MutableLiveData<FloatArray> = MutableLiveData(floatArrayOf(0f, 1f, 0.5f))
    private val stokeColor = Color.LTGRAY
    private var thumbSize by Delegates.notNull<Int>()
    private var strokeSize by Delegates.notNull<Int>()
    private var pickerMode = 1 // 0 = HSL, 1 = RGB

    private lateinit var seekBar1: SeekBar
    private lateinit var seekBar2: SeekBar
    private lateinit var seekBar3: SeekBar
    private lateinit var seekBar4: SeekBar
    private lateinit var text1: TextInputLayout
    private lateinit var text2: TextInputLayout
    private lateinit var text3: TextInputLayout
    private lateinit var text4: TextInputLayout
    private lateinit var modeButton: MaterialButton
    private lateinit var hexCodeTxt: TextInputLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            // Use the Builder class for convenient dialog construction

            thumbSize = (context?.resources?.getDimension(R.dimen.thumbHeight) ?: 20f).toInt()
            strokeSize = (context?.resources?.getDimension(R.dimen.stokeWidth) ?: 1f).toInt()

            val view = inflate(context, R.layout.dialog_color_picker, null)
            val coloImage = view.findViewById<ImageView>(R.id.dialog_color_picker_colorImg)
            hexCodeTxt = view.findViewById<TextInputLayout>(R.id.dialog_color_picker_color)

            hexCodeTxt.setEndIconOnClickListener(View.OnClickListener {
                copyHexCode()
            })

            val builder = MaterialAlertDialogBuilder(it)

            seekBar1 = view.findViewById<SeekBar>(R.id.dialog_color_picker_SeekBar1).apply {
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if (p2) {
                            val s = color.value
                            s?.set(0, p1.toFloat())
                            color.postValue(s)
                        }
                        text1.editText?.setText(p1.toString())
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }

                })
            }
            seekBar2 = view.findViewById<SeekBar>(R.id.dialog_color_picker_SeekBar2).apply {
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if (p2) {
                            val s = color.value
                            if (pickerMode == 0) {
                                s?.set(1, p1 / 100f)
                            } else {
                                s?.set(1, p1.toFloat())
                            }
                            color.postValue(s)
                        }
                        text2.editText?.setText(p1.toString())
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }

                })
            }
            seekBar3 = view.findViewById<SeekBar>(R.id.dialog_color_picker_SeekBar3).apply {
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if (p2) {
                            val s = color.value
                            if (pickerMode == 0) {
                                s?.set(2, p1 / 100f)
                            } else {
                                s?.set(2, p1.toFloat())
                            }
                            color.postValue(s)
                        }
                        text3.editText?.setText(p1.toString())
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }

                })
            }
            seekBar4 = view.findViewById<SeekBar>(R.id.dialog_color_picker_SeekBar4).apply {
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if (p2) {
                            colorAlpha = if (pickerMode == 0) {
                                p1 * 51f / 20f
                            } else {
                                p1.toFloat()
                            }
                        }
                        text4.editText?.setText(p1.toString())
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }

                })
            }

            text1 = view.findViewById(R.id.dialog_color_picker_text1)
            text2 = view.findViewById(R.id.dialog_color_picker_text2)
            text3 = view.findViewById(R.id.dialog_color_picker_text3)
            text4 = view.findViewById(R.id.dialog_color_picker_text4)

            modeButton = view.findViewById<MaterialButton>(R.id.dialog_color_picker_modeBtn).apply {
                setOnClickListener(
                    View.OnClickListener {
                        pickerMode = (pickerMode + 1) % 2
                        val temp = color.value
                        if (temp != null) {
                            if (pickerMode == 0) {
                                //HSL
                                ColorUtils.RGBToHSL(
                                    temp[0].toInt(), temp[1].toInt(),
                                    temp[2].toInt(), temp
                                )
                            } else {
                                //RGB
                                val c = ColorUtils.HSLToColor(temp)
                                temp[0] = Color.red(c).toFloat()
                                temp[1] = Color.green(c).toFloat()
                                temp[2] = Color.blue(c).toFloat()
                            }
                            updateSeekBars(temp)
                            color.postValue(temp)
                        }
                    })
            }

            color.observe(this, {

                val currentColor = if (pickerMode == 0) {
                    ColorUtils.HSLToColor(floatArrayOf(it[0], it[1], it[2]))
                } else {
                    Color.rgb(it[0].toInt(), it[1].toInt(), it[2].toInt())
                }

                //Change Background
                coloImage.setBackgroundColor(currentColor)

                //
                hexCodeTxt.editText?.setText(
                    String.format(
                        "#%02X%02X%02X%02X",
                        Color.red(currentColor),
                        Color.green(currentColor),
                        Color.blue(currentColor),
                        Color.alpha(currentColor)
                    )
                )

                val colors1 = if (pickerMode == 0) {
                    intArrayOf(
                        ColorUtils.HSLToColor(floatArrayOf(0f, it[1], it[2])),
                        ColorUtils.HSLToColor(floatArrayOf(60f, it[1], it[2])),
                        ColorUtils.HSLToColor(floatArrayOf(120f, it[1], it[2])),
                        ColorUtils.HSLToColor(floatArrayOf(180f, it[1], it[2])),
                        ColorUtils.HSLToColor(floatArrayOf(240f, it[1], it[2])),
                        ColorUtils.HSLToColor(floatArrayOf(300f, it[1], it[2])),
                        ColorUtils.HSLToColor(floatArrayOf(360f, it[1], it[2])),
                    )
                } else {
                    intArrayOf(
                        Color.rgb(0, it[1].toInt(), it[2].toInt()),
                        Color.rgb(255, it[1].toInt(), it[2].toInt()),
                    )
                }

                //Update SeekBar1 Gradient

                val gradient1 =
                    GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors1)
                gradient1.cornerRadius = 1000f
                gradient1.setStroke(strokeSize, stokeColor)
                seekBar1.progressDrawable = gradient1

                //Update SeekBar1 Thumb
                val thumb1 = GradientDrawable().apply {
                    setSize(thumbSize, thumbSize)
                    setColor(currentColor)
                    cornerRadius = 1000f
                    setStroke(strokeSize, stokeColor)
                }
                seekBar1.thumb = thumb1


                //Update SeekBar2 Gradient
                val colors2 = if (pickerMode == 0) {
                    intArrayOf(
                        ColorUtils.HSLToColor(floatArrayOf(it[0], 0f, it[2])),
                        ColorUtils.HSLToColor(floatArrayOf(it[0], 0.5f, it[2])),
                        ColorUtils.HSLToColor(floatArrayOf(it[0], 1f, it[2]))
                    )
                } else {
                    intArrayOf(
                        Color.rgb(it[0].toInt(), 0, it[2].toInt()),
                        Color.rgb(it[0].toInt(), 255, it[2].toInt()),
                    )
                }
                val gradient2 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors2)
                gradient2.cornerRadius = 1000f
                gradient2.setStroke(strokeSize, stokeColor)
                seekBar2.progressDrawable = gradient2

                //Update SeekBar2 Thumb
                val thumb2 = GradientDrawable().apply {
                    setSize(thumbSize, thumbSize)
                    setColor(currentColor)
                    cornerRadius = 1000f
                    setStroke(strokeSize, stokeColor)
                }
                seekBar2.thumb = thumb2

                //Update SeekBar3 Gradient
                val colors3 = if (pickerMode == 0) {
                    intArrayOf(
                        ColorUtils.HSLToColor(floatArrayOf(it[0], it[1], 0f)),
                        ColorUtils.HSLToColor(floatArrayOf(it[0], it[1], 0.5f)),
                        ColorUtils.HSLToColor(floatArrayOf(it[0], it[1], 1f))
                    )
                } else {
                    intArrayOf(
                        Color.rgb(it[0].toInt(), it[1].toInt(), 0),
                        Color.rgb(it[0].toInt(), it[1].toInt(), 255),
                    )
                }
                val gradient3 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors3)
                gradient3.cornerRadius = 1000f
                gradient3.setStroke(strokeSize, stokeColor)
                seekBar3.progressDrawable = gradient3

                //Update SeekBar3 Thumb
                val thumb3 = GradientDrawable().apply {
                    setSize(thumbSize, thumbSize)
                    setColor(currentColor)
                    cornerRadius = 1000f
                    setStroke(strokeSize, stokeColor)
                }
                seekBar3.thumb = thumb3


                //Update SeekBar4 Gradient
                val colors4 = intArrayOf(
                    ColorUtils.setAlphaComponent(currentColor, 0),
                    ColorUtils.setAlphaComponent(currentColor, 255)
                )

                val gradient4 = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors4)
                gradient4.cornerRadius = 1000f
                gradient4.setStroke(strokeSize, stokeColor)
                seekBar4.progressDrawable = gradient4

                //Update SeekBar4 Thumb
                val thumb4 = GradientDrawable().apply {
                    setSize(thumbSize, thumbSize)
                    setColor(currentColor)
                    cornerRadius = 1000f
                    setStroke(strokeSize, stokeColor)
                }
                seekBar4.thumb = thumb4
            })

            val temp = floatArrayOf(0f, 0f, 0f)
            temp[0] = Color.red(selectedColor).toFloat()
            temp[1] = Color.green(selectedColor).toFloat()
            temp[2] = Color.blue(selectedColor).toFloat()
            colorAlpha = Color.alpha(selectedColor).toFloat()
            updateSeekBars(temp)
            color.postValue(temp)

            builder.setView(view)
                .setPositiveButton("Ok") { dialog, _ ->
                    val c = color.value
                    if (c != null) {
                        val currentColor = if (pickerMode == 0) {
                            ColorUtils.HSLToColor(floatArrayOf(c[0], c[1], c[2]))
                        } else {
                            Color.rgb(c[0].toInt(), c[1].toInt(), c[2].toInt())
                        }
                        colorPickerDialogListener.onSelectColor(
                            ColorUtils.setAlphaComponent(currentColor, colorAlpha.toInt())
                        )
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // User cancelled the dialog
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun copyHexCode() {
        hexCodeTxt.editText?.text.also {
            val clipboard =
                context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("Hex", it.toString())
            clipboard?.setPrimaryClip(clip)
        }
    }

    private fun updateSeekBars(temp: FloatArray) {
        if (pickerMode == 0) {
            modeButton.setText(R.string.hsl)
            seekBar1.max = 360
            seekBar2.max = 100
            seekBar3.max = 100
            seekBar4.max = 100
            text1.hint = context?.getString(R.string.hue)
            text2.hint = context?.getString(R.string.saturation)
            text3.hint = context?.getString(R.string.lightness)
            seekBar1.progress = temp[0].toInt()
            seekBar2.progress = (temp[1] * 100).toInt()
            seekBar3.progress = (temp[2] * 100).toInt()
            seekBar4.progress = (colorAlpha * 20f / 51f).toInt()
        } else {
            modeButton.setText(R.string.rgb)
            seekBar1.max = 255
            seekBar2.max = 255
            seekBar3.max = 255
            seekBar4.max = 255
            text1.hint = context?.getString(R.string.red)
            text2.hint = context?.getString(R.string.green)
            text3.hint = context?.getString(R.string.blue)
            seekBar1.progress = temp[0].toInt()
            seekBar2.progress = temp[1].toInt()
            seekBar3.progress = temp[2].toInt()
            seekBar4.progress = colorAlpha.toInt()
        }
    }

    interface ColorPickerDialogListener {
        fun onSelectColor(color: Int)
    }

}