package ru.endlessrise;

import net.minecraftforge.fml.common.Mod;

@Mod(EndlessRiseMain.MODID)
public class ModStarter {
    public ModStarter() {
        new EndlessRiseMain(); // Загружаем класс с Kotlin
    }
}
