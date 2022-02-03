package com.example.numberguess

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlin.random.Random.Default.nextInt

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    lateinit var editText: EditText
    lateinit var imageButtonReset: ImageButton
    lateinit var imageButtonCheck: ImageButton


    var random: Int = nextInt( 1, 1000)
    var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        editText = findViewById(R.id.editTextNumber)
        imageButtonReset = findViewById(R.id.checkButton)
        imageButtonCheck = findViewById(R.id.resetButton)


        textView.text = "Find R in range 1 - 1000"

        imageButtonCheck.setOnClickListener {

            val number: Int = editText.text.toString().toInt()


            if (number < random) {
                count += 1
                textView.text = "R is greater  count:$count"
                editText.text.clear()


            } else if (number > random) {
                count += 1
                textView.text = "R is lower     count:$count"
                editText.text.clear()

            } else {
                textView.text = "Correct!!  count:  $count"
            }
        }



            imageButtonReset.setOnClickListener {
                reset()
            }

        }
        fun reset() {
            random = nextInt(1, 10000)
            textView.text = "Find R in range 1 - 1000"
            editText.text.clear()
            count = 0


    }

}