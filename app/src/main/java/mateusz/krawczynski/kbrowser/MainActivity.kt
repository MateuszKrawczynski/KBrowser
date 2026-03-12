package mateusz.krawczynski.kbrowser

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import mateusz.krawczynski.kbrowser.databinding.ActivityMainBinding
import java.io.File



class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(binding.root)
        binding.root.post() {
            binding.favScrollLayout.layoutParams.height = (binding.root.height*0.7).toInt()
            binding.favScrollLayout.requestLayout()
        }

        fun makeNecessaryFiles(){ // seperators: |/%:(&):%/| and \n
            if (!File(applicationContext.filesDir,"openTabsNormal.txt").exists()){
                File(applicationContext.filesDir,"openTabsNormal.txt").createNewFile()
            }
            if (!File(applicationContext.filesDir,"favs.txt").exists()){
                File(applicationContext.filesDir,"favs.txt").createNewFile()
            }
            if (!File(applicationContext.filesDir,"openTabsDouble.txt").exists()){
                File(applicationContext.filesDir,"openTabsDouble.txt").createNewFile();
            }
        }
        makeNecessaryFiles()

        binding.newRegularTab.setOnClickListener {
            val intent = Intent(applicationContext, normalTab::class.java)
            intent.putExtra("url","https://google.com")
            startActivity(intent)
        }
        binding.newDoubleTab.setOnClickListener {
            val intent = Intent(applicationContext, doubleTab::class.java)
            intent.putExtra("url1","https://google.com")
            intent.putExtra("url2","https://google.com")
            startActivity(intent)
        }
        binding.showTabs.setOnClickListener {
            val intent = Intent(applicationContext, viewTabs::class.java)
            startActivity(intent)
        }
        val favsList = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
        for (f in favsList){
            if (f.isNotBlank()){
                val name = f.split("|/%:(&):%/|")[0]
                val url = f.split("|/%:(&):%/|")[1]
                var button = Button(applicationContext)
                button.setBackgroundResource(R.drawable.button_rounded)
                button.setText(name)
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 20, 0, 0)
                button.setLayoutParams(params)
                button.setOnClickListener {
                    val intent = Intent(applicationContext,normalTab::class.java)
                    intent.putExtra("url",url)
                    startActivity(intent)
                }
               binding.favLinearLayout.addView(button)
            }
        }
        binding.favScrollLayout.requestLayout()

    }
}