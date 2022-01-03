package org.phantancy.fgocalc.character_factory

import android.content.Context
import com.jeremyliao.liveeventbus.LiveEventBus
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.dialog.CharacterDialog
import org.phantancy.fgocalc.entity.CharacterEntity
import org.phantancy.fgocalc.event.DatabaseEvent

class DatabaseCharacter(val ctx: Context) {
    fun onError(){
        val cd = CharacterDialog(ctx)
        val charEntity = CharacterEntity<Int>("数据库加载出错", R.drawable.altria_alter_b)
        charEntity.options = listOf(
                CharacterEntity.OptionEntity("重新加载数据库") {
                    LiveEventBus.get(DatabaseEvent::class.java)
                            .post(DatabaseEvent(DatabaseEvent.RELOAD))
                    cd.dismiss()}
        )
        cd.setEntity(charEntity)
        cd.show()
    }

    fun onSuccess(){
        val cd = CharacterDialog(ctx)
        val charEntity = CharacterEntity<Int>("数据库加载成功", R.drawable.altria_a)
        cd.setEntity(charEntity)
        cd.show()
    }

    fun onUpdated(){
        val cd = CharacterDialog(ctx);
        val charEntity = CharacterEntity<Int>("数据库已更新",R.drawable.altria_a)
        cd.setEntity(charEntity)
        cd.show()
    }
}