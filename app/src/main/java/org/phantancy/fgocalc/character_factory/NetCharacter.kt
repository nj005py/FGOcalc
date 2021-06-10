package org.phantancy.fgocalc.character_factory

import android.content.Context
import com.jeremyliao.liveeventbus.LiveEventBus
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.dialog.CharacterDialog
import org.phantancy.fgocalc.entity.CharacterEntity
import org.phantancy.fgocalc.event.DatabaseEvent

class NetCharacter(val ctx: Context) {
    fun onError() {
        val cd = CharacterDialog(ctx)
        val charEntity = CharacterEntity<Int>("未检查到网络", R.drawable.altria_alter_b)
        charEntity.options = listOf(
                CharacterEntity.OptionEntity("好的") {
                    cd.dismiss()
                }
        )
        cd.setEntity(charEntity)
        cd.show()
    }
}