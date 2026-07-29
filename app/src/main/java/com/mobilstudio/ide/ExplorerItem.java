package com.mobilstudio.ide;

import java.io.File;

public class ExplorerItem {

    private final File file;

    public ExplorerItem(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return file.getName();
    }

    public boolean isFolder() {
        return file.isDirectory();
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public long getSize() {
        return file.length();
    }

}
