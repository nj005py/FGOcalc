package org.phantancy.fgocalc.entity;

import java.io.Serializable;

public class AtkHpItem implements Serializable {
    private int atk;
    private int hp;

    public AtkHpItem(int atk, int hp) {
        this.atk = atk;
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
