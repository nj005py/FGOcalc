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

    public static class OptionEntity {
        //选项文字
        public String text;
        //选项操作
        public CharacterInterface characterInterface;
    }

    public interface CharacterInterface {
        void onClick();
    }
}
