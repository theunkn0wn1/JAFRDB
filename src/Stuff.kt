import jcurses.system.PlatformUtil
import jcurses.system.Toolkit
import java.util.regex.Pattern
import kotlin.system.exitProcess

val inpThr : Thread = Thread(fun (){
    while (true) {
        //println("triggered")
        var line: String? = readLine()
        if (line.isNullOrBlank()) continue
        line = line!!

        if (line == "exit") {
            Toolkit.shutdown()
            exitProcess(0)
        }
        var nick : String = ""
        if (line.contains("nick: ")){
            nick = line.replace("nick: ", "").split(Pattern.compile(" "), 2).first()
            line.replace("nick: " + nick, "").trim()
        }
        listenfr.handleMessage(nick, line)
    }
})

fun String.strip() : String{
    val sb = StringBuilder()
    var f3 = false
    for (c in this.toCharArray()){
        if (c == 2.toChar()){continue} //STX
        if (c == 3.toChar()){f3 = true; continue} //ETX
        if (f3){
            if (c.toInt() in 48..57 || c == ','){
                continue
            }
            f3 = false
        }
        sb.append(c)
    }
    return sb.toString()
}

fun String.reduceOld() : String{
    val charr = this.toCharArray().toMutableList()
    val sb = StringBuilder()
    charr
            .filterIndexed { i, c -> charr.lastIndex > i && !(c == ' ' && charr[i + 1] == ' ') }
            .forEach { sb.append(it) }
    return sb.toString()
}

fun String.reduce() : String{
    val tmp1 = this.trim().split(" ").toMutableList()
    tmp1.removeAll { it.trim() == "" }
    val sb = StringBuilder()
    tmp1.forEach{ sb.append(it + " ") }
    return sb.toString().trim()
}

fun ArrayList<Rescue>.asStringArray(): ArrayList<String> {
    var retV = ArrayList<String>()
    this.mapTo(retV) { it.number.toString() + "|" + it.client + "|" + it.language + "|" + it.clientSystem.name + "|" + it.platform + "|" + it.active.toString() }
    return retV;
}

fun getLibraryName() : String{
    var library: String? = null
    when {
        PlatformUtil.isWindowsX86() -> library = "/META-INF/windows-x86/libjcurses.dll"
        PlatformUtil.isMacOsx() -> library = "/META-INF/osx/libjcurses.jnilib"
        PlatformUtil.isLinuxX86() -> library = "/META-INF/linux-x86/libjcurses.so"
    }
    return library!!
}