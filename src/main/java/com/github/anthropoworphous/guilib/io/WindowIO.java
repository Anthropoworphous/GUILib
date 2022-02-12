package com.github.anthropoworphous.guilib.io;

import com.github.anthropoworphous.guilib.GUILib;
import com.github.anthropoworphous.guilib.window.Window;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WindowIO {
    private static final String path = GUILib.getPlugin().getDataFolder() + File.separator + "windows";

    public static void exportToPluginFolderInJson(Window window) {
        try {
            Path path = Path.of(WindowIO.path);
            //noinspection ResultOfMethodCallIgnored
            path.toFile().mkdirs();
            Writer writer = Files.newBufferedWriter(Paths.get(path.toString(), window.getName() + ".json"));
            new GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(new WindowJsonConverter(window), writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Window importFromPluginFolderInJson(String nameOfWindow) {
        try {
            Reader reader = Files.newBufferedReader(Path.of(WindowIO.path + File.separator + nameOfWindow + ".json"));
            Window win = new Gson().fromJson(reader, WindowJsonConverter.class).fromJson();
            //Connected.reconnect(win.getMainPane());
            reader.close();
            return win;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
