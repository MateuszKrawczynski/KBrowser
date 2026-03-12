package mateusz.krawczynski.kbrowser

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import mateusz.krawczynski.kbrowser.databinding.ActivityNormalTabBinding
import java.io.File
import androidx.activity.addCallback


class normalTab : AppCompatActivity() {
    private var beforeUrl = ""
    private var afterUrl = ""
    private var currentUrl = ""
    private var isFav = false
    private var loadupTriggered = false
    private var currentTitle = ""
    lateinit var binding: ActivityNormalTabBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNormalTabBinding.inflate(layoutInflater)
        beforeUrl = intent.getStringExtra("url").toString()
        afterUrl = ""
        currentUrl = beforeUrl
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(binding.root)
        binding.root.post {
            binding.urlBar.layoutParams.width = (binding.root.width*0.5).toInt()
            binding.urlBar.requestLayout()
            binding.web.layoutParams.height = (binding.root.height*0.73).toInt()
            binding.web.requestLayout()
            binding.tabs.layoutParams.width = (binding.root.width/2)
            binding.mainpage.layoutParams.width = binding.root.width/2
            binding.tabs.requestLayout()
            binding.mainpage.requestLayout()
        }

        fun Context.showInputDialog(title: String = "Enter Text", onResult: (String) -> Unit) {
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT

            AlertDialog.Builder(this)
                .setTitle(title)
                .setView(input)
                .setPositiveButton("OK") { _, _ ->
                    val userInput = input.text.toString()
                    onResult(userInput)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }


        val myWebView: WebView = binding.web
        myWebView.settings.javaScriptEnabled = true
        myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (!loadupTriggered){
                    beforeUrl = currentUrl
                }
                else{ loadupTriggered = false }
                currentUrl = url.toString()

                binding.urlBar.setText(currentUrl)
                var favslist = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
                var defbool = false
                for (fav in favslist){
                    if (fav.isNotEmpty()){
                    if ( currentUrl == fav.split("|/%:(&):%/|")[1] ){defbool = true; break}}
                }
                if (defbool){ isFav = true; binding.favTrigger.setImageResource(R.mipmap.fav)  }
                else{ isFav = false; binding.favTrigger.setImageResource(R.mipmap.nonfav) }

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                currentTitle = myWebView.title.toString()
            }
        }
        fun favCheck(){
            currentUrl = myWebView.url.toString()
            var favslist = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
            var defbool = false
            for (fav in favslist){
                if (fav.isNotEmpty()){
                if ( currentUrl == fav.split("|/%:(&):%/|")[1] ){defbool = true; break}}
            }
            if (defbool){ isFav = true; binding.favTrigger.setImageResource(R.mipmap.fav)  }
            else{ isFav = false; binding.favTrigger.setImageResource(R.mipmap.nonfav) }
        }

        myWebView.loadUrl(beforeUrl)
        binding.urlBar.setText(beforeUrl)
        binding.goback.setOnClickListener {
            if (beforeUrl.isNotEmpty()){
                loadupTriggered = true
                myWebView.loadUrl(beforeUrl)
                afterUrl = currentUrl
                currentUrl = beforeUrl
                beforeUrl = ""
                favCheck()

            }
        }
        binding.goforward.setOnClickListener {
            if (afterUrl.isNotEmpty()){
                loadupTriggered = true
                myWebView.loadUrl(afterUrl)
                beforeUrl = currentUrl
                currentUrl = afterUrl
                afterUrl = ""
                favCheck()
            }
        }

        binding.search.setOnClickListener {
            loadupTriggered = true
            var rawInput = binding.urlBar.text.toString()
            if (!rawInput.startsWith("https://") && !rawInput.startsWith("http://")){
                val buf = rawInput
                rawInput = "https://"+buf
            }
            beforeUrl = currentUrl
            myWebView.loadUrl(rawInput)
            currentUrl = myWebView.url.toString()
            binding.urlBar.setText(currentUrl)

            favCheck()

        }
        binding.favTrigger.setOnClickListener {

            favCheck()
            if (isFav){
                isFav = false
                var newS = ""
                var tmpFavsList = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
                for (fav in tmpFavsList){
                    if (fav.isNotEmpty()){
                        if (fav.split("|/%:(&):%/|")[1] != currentUrl){ newS += fav + "\n"  }
                    }
                }
                File(applicationContext.filesDir,"favs.txt").writeText(newS)
                favCheck()
            }
            else{
                isFav = true
                showInputDialog("How to name this favourite?",{r ->
                    if (r.isNotEmpty()){ File(applicationContext.filesDir,"favs.txt").appendText(r+"|/%:(&):%/|"+currentUrl+"\n") }
                    favCheck()
                })
            }
        }
        binding.mainpage.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        binding.tabs.setOnClickListener {
            // launch see tabs activity
            val intent = Intent(applicationContext, viewTabs::class.java)
            startActivity(intent)
        }
        onBackPressedDispatcher.addCallback(this) {
            if (myWebView.canGoBack()) {
                if (beforeUrl.isNotEmpty()){
                    loadupTriggered = true
                    myWebView.loadUrl(beforeUrl)
                    afterUrl = currentUrl
                    currentUrl = beforeUrl
                    beforeUrl = ""
                    favCheck()

                }
            } else {
                finish()
            }
        }


    }

    override fun onStop() {
        super.onStop()
        if (currentUrl != "https://www.google.com/") {
            val Sbuilder = currentUrl + "|/%:(&):%/|" + currentTitle +"\n" + File(applicationContext.filesDir, "openTabsNormal.txt").readText()
            File(applicationContext.filesDir, "openTabsNormal.txt").writeText(Sbuilder)
        }
    }
}