package cl.roboto.validator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("robotovalidator.json").toFile();
    public static JsonConfig jsonConfig = JsonConfig.initializeDefault();

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CONFIG_FILE), StandardCharsets.UTF_8))) {
                JsonConfig loadedConfig = GSON.fromJson(reader, JsonConfig.class);

                if (loadedConfig != null) {
                    jsonConfig = loadedConfig;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveConfig();
    }

    public static void saveConfig() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(CONFIG_FILE), StandardCharsets.UTF_8)) {
            JsonConfig currentConfig = jsonConfig;
            GSON.toJson(currentConfig, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
