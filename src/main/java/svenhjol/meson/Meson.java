package svenhjol.meson;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.meson.handler.LogHandler;
import svenhjol.meson.helper.StringHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Meson {
    public static Meson INSTANCE = new Meson();
    public static LogHandler LOG = new LogHandler("Meson");
    public static Map<String, Map<String, MesonModule>> loadedModules = new ConcurrentHashMap<>();
    private static Map<String, MesonMod> mods = new ConcurrentHashMap<>();

    private Meson() {
    }

    public void register(MesonMod mod) {
        mods.put(mod.getId(), mod);
    }

    public static MesonMod getMod(String id) {
        if (!mods.containsKey(id))
            throw new RuntimeException("No such mod: " + id);

        return mods.get(id);
    }

    public static boolean enabled(String moduleName) {
        String[] split = moduleName.split(":");
        String mod = split[0];
        String module = split[1];

        if (!loadedModules.containsKey(mod))
            return false;

        if (module.contains("_"))
            module = StringHelper.snakeToUpperCamel(module);

        if (!loadedModules.get(mod).containsKey(module))
            return false;

        return loadedModules.get(mod).get(module).enabled;
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
