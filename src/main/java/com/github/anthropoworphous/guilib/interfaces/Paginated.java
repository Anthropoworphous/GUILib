package com.github.anthropoworphous.guilib.interfaces;

public interface Paginated {
    void next();
    void previous();
    void jumpTo(int pageNumber);
    int currentPage();
}
