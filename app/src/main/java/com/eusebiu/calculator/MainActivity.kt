package com.eusebiu.calculator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.BLACK
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        fun View.animateOpacity(from: Float, to: Float, duration: Long = 200) {
            ObjectAnimator.ofFloat(this, "alpha", from , to).apply {
                this.duration = duration
                start()
            }
        }
        fun animateOnPressed(event: MotionEvent, v: View): Boolean {
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    v.animateOpacity(0.7f, 1.0f)
                    true
                }
                MotionEvent.ACTION_DOWN -> {
                    v.animateOpacity(1.0f, 0.7f)
                    true
                }
                else -> false
            }
           return false
        }
 val sharedPref = getSharedPreferences("AppData", MODE_PRIVATE)

        fun eval(expression: String): Double {
            try {
                return ExpressionBuilder(expression.replace("x", "*").replace("÷", "/")).build().evaluate()
            } catch (e: Throwable) {
                Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show()
            }
          return 0.0
        }

        val expressionInput = findViewById<TextView>(R.id.expression)
        val resultInput = findViewById<TextView>(R.id.result)
       val storedExpr = sharedPref.getString("expr", "0")
        val storedRes = sharedPref.getString("res", "")
        val expression = Expression(expressionInput.text)
        fun getResult() {
            try {
                if (expression.isNumber(expressionInput.text.last())) {
                    resultInput.visibility = View.VISIBLE
                    resultInput.text = eval(expressionInput.text.toString()).toString().removeSuffix(".0")
                    expressionInput.textSize = 21f
                }
            } catch (e: NumberFormatException) {
                expressionInput.text = "0"
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
        if (!storedExpr.isNullOrEmpty()) {
            expressionInput.text = storedExpr
        }
        if (!storedRes.isNullOrEmpty()) {
            resultInput.text = storedRes
            getResult()
        }
        expressionInput.addTextChangedListener {
            sharedPref.edit().putString("expr", it.toString()).apply()
        }
        resultInput.addTextChangedListener {
            sharedPref.edit().putString("res", it.toString()).apply()
        }

        fun clearResult() {
            if (resultInput.isVisible) {
                expressionInput.text = resultInput.text
                resultInput.visibility = View.GONE
                expressionInput.textSize = 65f
                resultInput.text = "";
            }
        }
        fun addValue(newValue: String) {
            val endBracket = if (expressionInput.text.endsWith(')')) ")" else ""
             clearResult()
            if (expressionInput.text.toString() == "0" && newValue != ".") {
                expressionInput.text = newValue
            } else {
                expressionInput.text = "${expressionInput.text.removeSuffix(")")}${newValue}${endBracket}"
            }

        }
        fun addOperator(newOperator: String) {
            val expr = Expression(expressionInput.text)
            clearResult()
            if (expr.isNumber(expressionInput.text.last())) {
                expressionInput.text = "${expressionInput.text}${newOperator}"
            }
        }

        val acBtn = findViewById<ImageButton>(R.id.acBtn)
        val toggleNegativeBtn = findViewById<ImageButton>(R.id.toggleNegativeBtn)
        val percentageBtn = findViewById<ImageButton>(R.id.percentageBtn)
        val decimalBtn = findViewById<ImageButton>(R.id.decimalBtn)
        val additionBtn = findViewById<ImageButton>(R.id.additionBtn)
        val subtractionBtn = findViewById<ImageButton>(R.id.subtractionBtn)
        val multiplyBtn = findViewById<ImageButton>(R.id.multiplyBtn)
        val equalBtn = findViewById<ImageButton>(R.id.equalBtn)
        val dividerBtn = findViewById<ImageButton>(R.id.dividerBtn)
        val zeroBtn = findViewById<ImageButton>(R.id.zeroBtn)
        val oneBtn = findViewById<ImageButton>(R.id.oneBtn)
        val twoBtn = findViewById<ImageButton>(R.id.twoBtn)
        val threeBtn = findViewById<ImageButton>(R.id.threeBtn)
        val fourBtn = findViewById<ImageButton>(R.id.fourBtn)
        val fiveBtn = findViewById<ImageButton>(R.id.fiveBtn)
        val sixBtn = findViewById<ImageButton>(R.id.sixBtn)
        val sevenBtn = findViewById<ImageButton>(R.id.sevenBtn)
        val eightBtn = findViewById<ImageButton>(R.id.eightBtn)
        val nineBtn = findViewById<ImageButton>(R.id.nineBtn)
        acBtn.setOnTouchListener { v, event ->
           animateOnPressed(event, v)

        }
        acBtn.setOnClickListener {
            clearResult()
            expressionInput.text = "0"
        }
        toggleNegativeBtn.setOnClickListener {
            clearResult()
            val expr = Expression(expressionInput.text)
            val lastNumber = expr.getLastNumber()

            if (!expr.isNumber(expressionInput.text.last()) && lastNumber != ".") {
                return@setOnClickListener
            }
            expressionInput.text = expr.toggleNegative()
        }
        toggleNegativeBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)

        }
        percentageBtn.setOnClickListener {
            addOperator("%")

        }
        percentageBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)

        }
        dividerBtn.setOnClickListener {
           addOperator("÷")
        }
        acBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        percentageBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        multiplyBtn.setOnClickListener {
            addOperator("x")
        }
        multiplyBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        subtractionBtn.setOnClickListener {
            addOperator("-")
        }
        subtractionBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        additionBtn.setOnClickListener {
            addOperator("+")
        }
        additionBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        decimalBtn.setOnClickListener {
            if (!resultInput.text.contains('.')) {
                clearResult()
            }

            val expr = Expression(expressionInput.text)
            val lastNumber = expr.getLastNumber()
            println(lastNumber)
            if (!lastNumber.contains('.') && expr.isNumber(expressionInput.text.last())) {
                addValue(".")
            }
        }
        decimalBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        oneBtn.setOnClickListener {
            addValue("1")
        }
        oneBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        twoBtn.setOnClickListener {
            addValue("2")
        }
        twoBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        threeBtn.setOnClickListener {
            addValue("3")
        }
        threeBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        fourBtn.setOnClickListener {
            addValue("4")
        }
        fourBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        fiveBtn.setOnClickListener {
            addValue("5")
        }
        fiveBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        sixBtn.setOnClickListener {
            addValue("6")
        }
        sixBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        sevenBtn.setOnClickListener {
            addValue("7")
        }
        sevenBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        eightBtn.setOnClickListener {
            addValue("8")
        }
        eightBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        nineBtn.setOnClickListener {
            addValue("9")
        }
        nineBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        zeroBtn.setOnClickListener {
            val expr = Expression(expressionInput.text)
            val lastNumber = expr.getLastNumber()
            if (expressionInput.text.toString() != "0" && lastNumber != "0") {
                addValue("0")
            }
        }
        zeroBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
        equalBtn.setOnClickListener {
           getResult()
        }
        equalBtn.setOnTouchListener { v, event ->
            animateOnPressed(event, v)
        }
    }
}