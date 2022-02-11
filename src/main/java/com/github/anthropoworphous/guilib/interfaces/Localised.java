package com.github.anthropoworphous.guilib.interfaces;

import com.github.anthropoworphous.guilib.util.ID;

public interface Localised {
    ID location();
    ID move(int offset);
}
