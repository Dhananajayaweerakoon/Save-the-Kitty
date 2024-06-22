package com.example.savethekitty
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
class Gameview (var c : Context,var gameTask: Gametask):View(c){
    private var myPaint:Paint?=null
    private var speed= 1
    private var time = 0
    private var score = 0
    private var myCatPosition = 0
    private val bombs = ArrayList<HashMap<String,Any>>()

    var viewWidth = 0
    var viewHeight = 0
    init{
        myPaint = Paint()

    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if(time%700<10 +speed){
            val map = HashMap<String,Any>()
            map["lane"]=(0..2).random()
            map["startTime"] = time
            bombs.add(map)
        }
        time = time+10+speed
        val catWidth = viewWidth/5
        val catHeight = catWidth+10
        myPaint !!. style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.kit,null)
        d.setBounds(
            myCatPosition * viewWidth / 3 +viewWidth/15+25,
            viewHeight-2 - catHeight,
            myCatPosition*viewWidth/3+ viewWidth/15+catWidth-25,
            viewHeight-2
        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0
        for (i in bombs.indices){
            try{
                val bomX = bombs[i]["lane"] as Int *viewWidth / 3 +viewWidth /15
                var bomY = time- bombs[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.explode,null)


                d2.setBounds(
                    bomX + 25 , bomY - catHeight , bomX + catWidth - 25 , bomY

                )
                d2.draw(canvas)
                if(bombs[i]["lane"] as Int == myCatPosition){
                    if(bomY> viewHeight - 2 - catHeight && bomY < viewHeight - 2){

                        gameTask.closeGame(score)
                    }
                }
                if(bomY > viewHeight + catHeight)
                {
                    bombs.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score /8)
                    if(score > highScore){
                        highScore = score
                    }
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score",80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed",380f, 80f, myPaint!!)
        invalidate()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
                val x1 = event.x
                if(x1< viewWidth/2){
                    if(myCatPosition>0){
                        myCatPosition--
                    }
                }
                if(x1>viewWidth / 2){
                    if(myCatPosition<2){
                        myCatPosition++
                    }
                }
               invalidate()
            }
            MotionEvent.ACTION_UP ->{

            }

        }
        return true
    }
}