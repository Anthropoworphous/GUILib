package com.github.anthropoworphous.guilib.util;

import org.jetbrains.annotations.NotNull;

public class ID implements Comparable<ID> {
    public ID(int id) {
        this.id = id;
    }

    private int id;

    //non static
    public int getID() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public ID move(int offset) {
        id += offset;
        return this;
    }
    public ID move(ID offset) {
        id += offset.id;
        return this;
    }

    public ID offset(int offset) {
        return new ID(id + offset);
    }
    public ID offset(ID offset) {
        return new ID(id + offset.getID());
    }

    @Override
    public String toString() {
        return "id: " + id;
    }

    @Override
    public int compareTo(@NotNull ID o) {
        return this.id - o.id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ID && ((ID) obj).id == id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    //static
    public static int offset(int origin, int offset) {
        return origin + offset;
    }
}
