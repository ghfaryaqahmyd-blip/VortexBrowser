package com.vortex.browser

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VortexBrowserScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VortexBrowserScreen() {
    var urlText by remember { mutableStateOf("https://www.google.com") }
    var currentUrl by remember { mutableStateOf("https://www.google.com") }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = urlText,
                onValueChange = { urlText = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { Text("آدرس سایت یا جستجو...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val formattedUrl = if (urlText.startsWith("http://") || urlText.startsWith("https://")) {
                    urlText
                } else if (urlText.contains(".")) {
                    "https://$urlText"
                } else {
                    "https://www.google.com/search?q=$urlText"
                }
                currentUrl = formattedUrl
                webViewInstance?.loadUrl(formattedUrl)
            }) {
                Text("برو")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = { webViewInstance?.goBack() }) {
                Text("◄")
            }
            OutlinedButton(onClick = { webViewInstance?.goForward() }) {
                Text("►")
            }
            OutlinedButton(onClick = { webViewInstance?.reload() }) {
                Text("↻")
            }
            OutlinedButton(onClick = {
                currentUrl = "https://www.google.com"
                urlText = "https://www.google.com"
                webViewInstance?.loadUrl("https://www.google.com")
            }) {
                Text("خانه")
            }
        }

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            url?.let {
                                currentUrl = it
                                urlText = it
                            }
                        }
                    }
                    loadUrl(currentUrl)
                    webViewInstance = this
                }
            },
            update = { webView ->
                webViewInstance = webView
            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
    }
}
