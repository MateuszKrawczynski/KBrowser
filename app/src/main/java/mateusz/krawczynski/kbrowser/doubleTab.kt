package mateusz.krawczynski.kbrowser

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.InputType
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import mateusz.krawczynski.kbrowser.databinding.ActivityDoubleTabBinding
import mateusz.krawczynski.kbrowser.databinding.ActivityMainBinding
import java.io.File
import androidx.activity.addCallback

class doubleTab : AppCompatActivity() {
    private var beforeUrl1 = ""
    private var afterUrl1 = ""
    private var currentUrl1 = ""
    private var isFav1 = false
    private var loadupTriggered1 = false
    private var beforeUrl2 = ""
    private var afterUrl2 = ""
    private var currentUrl2 = ""
    private var isFav2 = false
    private var loadupTriggered2 = false
    private var currentTitle1 = ""
    private var currentTitle2 = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var binding: ActivityDoubleTabBinding
        super.onCreate(savedInstanceState)
        binding = ActivityDoubleTabBinding.inflate(layoutInflater)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(binding.root)
        beforeUrl1 = intent.getStringExtra("url1").toString()
        afterUrl1 = ""
        currentUrl1 = beforeUrl1
        beforeUrl2 = intent.getStringExtra("url2").toString()
        afterUrl2 = ""
        currentUrl2 = beforeUrl2
        binding.root.post {
            binding.urlBar1.layoutParams.width = (binding.root.width*0.5).toInt()
            binding.urlBar1.requestLayout()
            binding.web1.layoutParams.height = (binding.root.height*0.36).toInt()
            binding.web1.requestLayout()
            binding.tabs.layoutParams.width = (binding.root.width/2)
            binding.mainpage.layoutParams.width = binding.root.width/2
            binding.tabs.requestLayout()
            binding.mainpage.requestLayout()
            binding.urlBar2.layoutParams.width = (binding.root.width*0.5).toInt()
            binding.urlBar2.requestLayout()
            binding.web2.layoutParams.height = (binding.root.height*0.36).toInt()
            binding.web2.requestLayout()

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


        val myWebView: WebView = binding.web1
        myWebView.settings.javaScriptEnabled = true
        myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (!loadupTriggered1){
                    beforeUrl1 = currentUrl1
                }
                else{ loadupTriggered1 = false }
                currentUrl1 = url.toString()
                binding.urlBar1.setText(currentUrl1)
                var favslist = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
                var defbool = false
                for (fav in favslist){
                    if (fav.isNotEmpty()){
                        if ( currentUrl1 == fav.split("|/%:(&):%/|")[1] ){defbool = true; break}}
                }
                if (defbool){ isFav1 = true; binding.favTrigger1.setImageResource(R.mipmap.fav)  }
                else{ isFav1 = false; binding.favTrigger1.setImageResource(R.mipmap.nonfav) }

            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                currentTitle1 = myWebView.title.toString()

            }
        }
        val myWebView2: WebView = binding.web2
        myWebView2.settings.javaScriptEnabled = true
        myWebView2.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (!loadupTriggered2){
                    beforeUrl2 = currentUrl2
                }
                else{ loadupTriggered2 = false }
                currentUrl2 = url.toString()
                binding.urlBar2.setText(currentUrl2)
                var favslist = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
                var defbool = false
                for (fav in favslist){
                    if (fav.isNotEmpty()){
                        if ( currentUrl2 == fav.split("|/%:(&):%/|")[1] ){defbool = true; break}}
                }
                if (defbool){ isFav2 = true; binding.favTrigger2.setImageResource(R.mipmap.fav)  }
                else{ isFav2 = false; binding.favTrigger2.setImageResource(R.mipmap.nonfav) }

            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                currentTitle2 = myWebView2.title.toString()
            }
        }
        fun favCheck(){
            currentUrl1 = myWebView.url.toString()
            var favslist = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
            var defbool = false
            for (fav in favslist){
                if (fav.isNotEmpty()){
                    if ( currentUrl1 == fav.split("|/%:(&):%/|")[1] ){defbool = true; break}}
            }
            if (defbool){ isFav1 = true; binding.favTrigger1.setImageResource(R.mipmap.fav)  }
            else{ isFav1 = false; binding.favTrigger1.setImageResource(R.mipmap.nonfav) }
        }
        fun favCheck2(){
            currentUrl2 = myWebView2.url.toString()
            var favslist = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
            var defbool = false
            for (fav in favslist){
                if (fav.isNotEmpty()){
                    if ( currentUrl2 == fav.split("|/%:(&):%/|")[1] ){defbool = true; break}}
            }
            if (defbool){ isFav2 = true; binding.favTrigger2.setImageResource(R.mipmap.fav)  }
            else{ isFav2 = false; binding.favTrigger2.setImageResource(R.mipmap.nonfav) }
        }

        myWebView.loadUrl(beforeUrl1)
        myWebView2.loadUrl(beforeUrl2)
        binding.urlBar1.setText(beforeUrl1)
        binding.urlBar2.setText(beforeUrl2)
        binding.goback1.setOnClickListener {
            if (beforeUrl1.isNotEmpty()){
                loadupTriggered1 = true
                myWebView.loadUrl(beforeUrl1)
                afterUrl1 = currentUrl1
                currentUrl1 = beforeUrl1
                beforeUrl1 = ""
                favCheck()

            }
        }
        binding.goforward1.setOnClickListener {
            if (afterUrl1.isNotEmpty()){
                loadupTriggered1 = true
                myWebView.loadUrl(afterUrl1)
                beforeUrl1 = currentUrl1
                currentUrl1 = afterUrl1
                afterUrl1 = ""
                favCheck()
            }
        }

        binding.search1.setOnClickListener {
            loadupTriggered1 = true
            var rawInput = binding.urlBar1.text.toString()
            if (!rawInput.startsWith("https://") && !rawInput.startsWith("http://")){
                val buf = rawInput
                rawInput = "https://"+buf
            }
            beforeUrl1 = currentUrl1
            myWebView.loadUrl(rawInput)
            currentUrl1 = myWebView.url.toString()
            binding.urlBar1.setText(currentUrl1)

            favCheck()

        }
        binding.favTrigger1.setOnClickListener {

            favCheck()

            if (isFav1){
                isFav1 = false
                var newS = ""
                var tmpFavsList = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
                for (fav in tmpFavsList){
                    if (fav.isNotEmpty()){
                        if (fav.split("|/%:(&):%/|")[1] != currentUrl1){ newS += fav + "\n"  }
                    }
                }

                File(applicationContext.filesDir,"favs.txt").writeText(newS)
                favCheck()
            }
            else{
                isFav1 = true
                showInputDialog("How to name this favourite?",{r ->
                    if (r.isNotEmpty()){
                        val file = File(applicationContext.filesDir,"favs.txt")
                        file.appendText(r+"|/%:(&):%/|"+currentUrl1+"\n")
                    }
                    favCheck()
                })
            }
        }

        binding.goback2.setOnClickListener {
            if (beforeUrl2.isNotEmpty()){
                loadupTriggered2 = true
                myWebView2.loadUrl(beforeUrl2)
                afterUrl2 = currentUrl2
                currentUrl2 = beforeUrl2
                beforeUrl2 = ""
                favCheck2()

            }
        }
        binding.goforward2.setOnClickListener {
            if (afterUrl2.isNotEmpty()){
                loadupTriggered2 = true
                myWebView2.loadUrl(afterUrl2)
                beforeUrl2 = currentUrl2
                currentUrl2 = afterUrl2
                afterUrl2 = ""
                favCheck2()
            }
        }

        binding.search2.setOnClickListener {
            loadupTriggered2 = true
            var rawInput = binding.urlBar2.text.toString()
            if (!rawInput.startsWith("https://") && !rawInput.startsWith("http://")){
                val buf = rawInput
                rawInput = "https://"+buf
            }
            beforeUrl2 = currentUrl2
            myWebView2.loadUrl(rawInput)
            currentUrl2 = myWebView.url.toString()
            binding.urlBar2.setText(currentUrl2)

            favCheck2()

        }
        binding.favTrigger2.setOnClickListener {

            favCheck2()
            if (isFav2){
                isFav2 = false
                var newS = ""
                var tmpFavsList = File(applicationContext.filesDir,"favs.txt").readText().split("\n")
                for (fav in tmpFavsList){
                    if (fav.isNotEmpty()){
                        if (fav.split("|/%:(&):%/|")[1] != currentUrl2){ newS += fav + "\n"  }
                    }
                }
                File(applicationContext.filesDir,"favs.txt").writeText(newS)
                favCheck2()
            }
            else{
                isFav2 = true
                showInputDialog("How to name this favourite?",{r ->
                    if (r.isNotEmpty()){
                        val file = File(applicationContext.filesDir,"favs.txt")
                        file.appendText(r+"|/%:(&):%/|"+currentUrl2+"\n")
                    }
                    favCheck2()
                })
            }
        }
        //
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
            if (!beforeUrl1.isNotEmpty() && !beforeUrl2.isNotEmpty()){finish()}

                if (beforeUrl1.isNotEmpty()){
                    loadupTriggered1 = true
                    myWebView.loadUrl(beforeUrl1)
                    afterUrl1 = currentUrl1
                    currentUrl1 = beforeUrl1
                    beforeUrl1 = ""
                    favCheck()

                }
            }

                if (beforeUrl2.isNotEmpty()) {
                    loadupTriggered2 = true
                    myWebView.loadUrl(beforeUrl2)
                    afterUrl2 = currentUrl2
                    currentUrl2 = beforeUrl2
                    beforeUrl2 = ""
                    favCheck()

                }





    }
    override fun onStop() {
        super.onStop()
        if (currentUrl1 != "https://www.google.com/" || currentUrl2 != "https://www.google.com/") {
            val Sbuilder = currentUrl1 + "|/%:(&):%/|" + currentTitle1 + "|/%:(&):%/|"  + currentUrl2 + "|/%:(&):%/|" + currentTitle2 + "\n" + File(applicationContext.filesDir, "openTabsDouble.txt").readText()
            File(applicationContext.filesDir, "openTabsDouble.txt").writeText(Sbuilder)

        }
    }


}