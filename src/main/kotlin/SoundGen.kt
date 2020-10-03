import kotlinx.coroutines.Dispatchers
import javax.sound.sampled.*
import kotlin.math.sin
import kotlinx.coroutines.*

class SoundGen {
    companion object : CoroutineScope {
        val job = SupervisorJob()
        override val coroutineContext = Dispatchers.IO + job
        private const val rate = 44100
        private val aformat = AudioFormat(rate.toFloat(), 8, 1, false, true)
        private val dataLine = AudioSystem.getSourceDataLine(aformat)

        fun start() = with(dataLine) {
                open(aformat)
                start()
                play(sigGen(220))
        }

        fun stop () {
            job.cancelChildren()
            with(dataLine) { flush(); stop(); close() }
        }

        fun SourceDataLine.play (buffer: ByteArray) {
            launch {
                val size = buffer.size
                while(this.isActive) {
                    this@play.write(buffer, 0, size)
                }
            }
        }

        fun sigGen (freq: Int) : ByteArray {
            val buffer = ByteArray((rate / freq) * 4)
            for (i in buffer.indices) {
                val ang = (2f * Math.PI * i) / (rate / freq)
                buffer[i] = ((sin(ang) + 1) * 50).toInt().toByte()
            }
            return buffer
        }
    }
}