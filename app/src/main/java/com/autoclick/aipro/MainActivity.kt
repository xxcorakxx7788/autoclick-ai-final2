
package com.autoclick.aipro
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lay = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(40,60,40,40) }
        val t = TextView(this).apply { text = "AutoClick AI Pro - Android 11+"; textSize = 22f }
        val input = EditText(this).apply { hint = "Ej: COMPRAR, X cerrar anuncio"; setText(AIBrain.objetivo) }
        val bPerm = Button(this).apply { text = "1. Activar Accesibilidad" }
        val bIni = Button(this).apply { text = "INICIAR IA" }
        val bStop = Button(this).apply { text = "DETENER" }
        val st = TextView(this).apply { text = "Estado: Detenido" }
        bPerm.setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        bIni.setOnClickListener {
            AIBrain.objetivo = input.text.toString()
            AutoClickService.instance?.iniciar()
            st.text = "IA Activa: ${AIBrain.objetivo}"
            Toast.makeText(this, "Minimiza y abre la otra app", Toast.LENGTH_LONG).show()
        }
        bStop.setOnClickListener { AutoClickService.instance?.detener(); AIBrain.isRunning=false; st.text="Detenido" }
        lay.addView(t); lay.addView(input); lay.addView(bPerm); lay.addView(bIni); lay.addView(bStop); lay.addView(st)
        setContentView(lay)
    }
}
