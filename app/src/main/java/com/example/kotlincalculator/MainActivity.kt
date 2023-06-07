package com.example.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.kotlincalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate((layoutInflater))
        setContentView(binding.root)
    }

    fun onEqualClick(view: View) {

        onEqual()
        binding.dataTv.text = binding.resultTv.text.toString().drop(1)
    }

    fun onDigitClick(view: View) {

        if (stateError) {
            binding.dataTv.text = (view as Button).text
            stateError = false
        } else {
            binding.dataTv.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }

    fun onAllcLearClick(view: View) {
        binding.dataTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.resultTv.visibility = View.GONE
    }

    fun onOperatorClick(view: View) {
        if (!stateError && lastNumeric) {
            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()

        }
    }

    fun onBackClick(view: View) {
        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)

        try {
            var lastChar = binding.dataTv.text.toString().last()
            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("last char error", e.toString())
        }

    }

    fun onClearClick(view: View) {
        binding.dataTv.text = ""
        lastNumeric = false
    }

    fun onEqual() {

        if (lastNumeric && !stateError) {
            var txt = binding.dataTv.text.toString()

            expression = ExpressionBuilder(txt).build()
            try {
                var result = expression.evaluate()

                binding.resultTv.visibility = View.VISIBLE
                binding.resultTv.text = "=" + result.toString()
            } catch (ex: ArithmeticException) {
                Log.e("evaluate", ex.toString())
                binding.resultTv.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}