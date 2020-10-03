import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import tornadofx.*

class MainApp() : App(MainView::class)

fun main() {
    launch<MainApp>()
}

class MainView () : View() {
    val state = SimpleBooleanProperty(false)
    val button = button("OFF") {
        action {
            state.value = !state.value
        }
    }
    init {
        state.onChange {
            if (it) {
                SoundGen.start()
                button.text = "ON"
            }
            else {
                SoundGen.stop()
                button.text = "OFF"
            }
        }
    }
    override val root = vbox {
        prefHeight = 500.0
        prefWidth = 300.0
        alignment = Pos.CENTER
        label("test") {
            style {
                fontSize = 30.px
            }
        }
        add(button)
    }
}