
package com.autoclick.aipro
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import kotlin.random.Random

object AIBrain {
    var objetivo: String = "COMPRAR"
    var isRunning = false
    fun debeClickear(texto: String?): Boolean {
        if (texto.isNullOrBlank()) return false
        val t = texto.uppercase()
        return objetivo.uppercase().split(" ").any { it.length>2 && t.contains(it) }
    }
}
class AutoClickService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!AIBrain.isRunning) return
        buscar()
    }
    override fun onInterrupt() {}
    fun iniciar() {
        AIBrain.isRunning = true
        runnable = object: Runnable {
            override fun run() {
                if (!AIBrain.isRunning) return
                buscar()
                handler.postDelayed(this, Random.nextLong(400,1200))
            }
        }
        handler.post(runnable!!)
    }
    fun detener() { AIBrain.isRunning = false; runnable?.let { handler.removeCallbacks(it) } }
    private fun buscar() {
        val root = rootInActiveWindow ?: return
        val lista = mutableListOf<AccessibilityNodeInfo>()
        recolectar(root, lista)
        val nodo = lista.firstOrNull { AIBrain.debeClickear(it.text?.toString() ?: it.contentDescription?.toString()) }
        nodo?.let { clickNodo(it) }
    }
    private fun recolectar(n: AccessibilityNodeInfo, l: MutableList<AccessibilityNodeInfo>) {
        l.add(n); for(i in 0 until n.childCount) n.getChild(i)?.let { recolectar(it,l) }
    }
    private fun clickNodo(node: AccessibilityNodeInfo) {
        val r = android.graphics.Rect(); node.getBoundsInScreen(r)
        clickEn(r.centerX(), r.centerY())
    }
    private fun clickEn(x: Int, y: Int) {
        val fx = x + Random.nextInt(-12,12); val fy = y + Random.nextInt(-12,12)
        val p = Path(); p.moveTo(fx.toFloat(), fy.toFloat())
        val g = GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(p,0,80)).build()
        dispatchGesture(g,null,null)
    }
    companion object { var instance: AutoClickService? = null }
    override fun onServiceConnected() { super.onServiceConnected(); instance = this }
}
