package mateusz.krawczynski.kbrowser

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import mateusz.krawczynski.kbrowser.databinding.ActivityViewTabsBinding
import java.io.File


private class Content : AsyncTask<Void?, Void?, Void?>() {
    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg voids: Void?): Void? {
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
    }
}
class viewTabs : AppCompatActivity() {
    lateinit var binding: ActivityViewTabsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTabsBinding.inflate(layoutInflater)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(binding.root)
        binding.root.post {
            binding.normaltabs.layoutParams.width = (binding.root.width/2.3).toInt()
            binding.doubletabs.layoutParams.width = (binding.root.width/2.3).toInt()
            binding.normaltabs.requestLayout()
            binding.doubletabs.requestLayout()
            binding.scroll.layoutParams.height = (binding.root.height*0.75).toInt()
            binding.scroll.requestLayout()
        }

        fun loadNormalTabs(){
            binding.linear.removeAllViews()
            var list = Any()
            var listtmp = File(applicationContext.filesDir,"openTabsNormal.txt").readText().split("\n")
            if (listtmp.size > 50){
                list = ArrayList<String>()
                var Sbuilder = ""
                var cropCounter = 1
                for (el in listtmp){
                    if (cropCounter > 50){ break }
                    else{ list.add(el); Sbuilder+=el+"\n"}
                    cropCounter++
                }
                File(applicationContext.filesDir,"openTabsNormal.txt").writeText(Sbuilder)
            }
            else {
                list = listtmp
            }
            var counter = 0
            list.forEachIndexed{ index, rec ->
                if (rec.isNotBlank()){

                    val button =  Button(applicationContext)
                    val url = rec.split("|/%:(&):%/|")[0]
                    val title = rec.split("|/%:(&):%/|")[1]
                    button.setText(title)
                    val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 20, 0, 0)
                    button.setLayoutParams(params)
                    button.setBackgroundResource(R.drawable.button_rounded)
                    button.setTextColor(Color.BLACK)
                   button.setOnClickListener {
                       var Sbuilder = ""
                       var insideCounter = 0
                       for (el in list){
                           if (el.isNotBlank()){
                           if (insideCounter !=   index)  {
                               Sbuilder += el + "\n"
                           }}


                           insideCounter++
                       }
                       println(Sbuilder)
                       File(applicationContext.filesDir,"openTabsNormal.txt").writeText(Sbuilder)
                       val intent = Intent(applicationContext,normalTab::class.java)
                       intent.putExtra("url",url)
                       startActivity(intent)
                       }


                    binding.linear.addView(button)

                }
                counter++



            }
            binding.linear.requestLayout()

        }
        fun loadDoubleTabs(){
            binding.linear.removeAllViews()
            var list = Any()
            var listtmp = File(applicationContext.filesDir,"openTabsDouble.txt").readText().split("\n")
            if (listtmp.size > 50){
                list = ArrayList<String>()
                var Sbuilder = ""
                var cropCounter = 1
                for (el in listtmp){
                    if (cropCounter > 50){ break }
                    else{ list.add(el); Sbuilder+=el+"\n"}
                    cropCounter++
                }
                File(applicationContext.filesDir,"openTabsDouble.txt").writeText(Sbuilder)
            }
            else {
                list = listtmp
            }
            var counter = 0
            list.forEachIndexed{ index, rec ->
                if (rec.isNotBlank()){
                    println(rec)
                    val button =  Button(applicationContext)
                    val url1 = rec.split("|/%:(&):%/|")[0]
                    val title1 = rec.split("|/%:(&):%/|")[1]
                    val url2 = rec.split("|/%:(&):%/|")[2]
                    val title2 = rec.split("|/%:(&):%/|")[3]
                    val text = title1 + "  |  " + title2
                    button.setText(text)
                    val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 20, 0, 0)
                    button.setLayoutParams(params)
                    button.setBackgroundResource(R.drawable.button_rounded)
                    button.setTextColor(Color.BLACK)
                    button.setOnClickListener {
                        var Sbuilder = ""
                        var insideCounter = 0
                        for (el in list){
                            if (el.isNotBlank()){
                                if (insideCounter !=   index)  {
                                    Sbuilder += el + "\n"
                                }}


                            insideCounter++
                        }
                        println(Sbuilder)
                        File(applicationContext.filesDir,"openTabsDouble.txt").writeText(Sbuilder)
                        val intent = Intent(applicationContext,doubleTab::class.java)
                        intent.putExtra("url1",url1)
                        intent.putExtra("url2",url2)
                        startActivity(intent)
                    }


                    binding.linear.addView(button)

                }
                counter++



            }
            binding.linear.requestLayout()

        }
        loadNormalTabs()


        binding.normaltabs.setOnClickListener { loadNormalTabs() }
        binding.doubletabs.setOnClickListener { loadDoubleTabs() }

        binding.goback.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }


    }
}