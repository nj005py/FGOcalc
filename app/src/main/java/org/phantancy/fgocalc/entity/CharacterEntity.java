package org.phantancy.fgocalc.entity;

import java.io.Serializable;
import java.util.List;

public class CharacterEntity<T> implements Serializable {
    //文字内容
    public String content = null;
    //立绘
    public T img = null;
    //选项
    public List<OptionEntity> options = null;

    public CharacterEntity() {
    }

    public CharacterEntity(String content, T img) {
        this.content = content;
        this.img = img;
    }

    public static class OptionEntity {
        //选项文字
        public String text;
        //选项操作
        public CharacterInterface characterInterface;

        public OptionEntity(String text, CharacterInterface characterInterface) {
            this.text = text;
            this.characterInterface = characterInterface;
        }
    }

    public interface CharacterInterface {
        void onClick();
    }
}
