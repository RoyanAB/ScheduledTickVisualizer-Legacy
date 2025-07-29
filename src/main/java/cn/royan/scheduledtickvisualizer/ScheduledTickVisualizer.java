package cn.royan.scheduledtickvisualizer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.MinecraftServer;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class ScheduledTickVisualizer implements ModInitializer
{
	public static final Logger LOGGER = LogManager.getLogger("scheduledtickvisualizer");

	public static final String MOD_ID = "scheduledtickvisualizer";
	public static String MOD_VERSION = "unknown";
	public static String MOD_NAME = "unknown";

	public static ScheduledTickVisualizerManager scheduledTickVisualizermanager;
	public static ScheduledTickVisualizerConfig scheduledTickVisualizerConfig;
	public static MinecraftServer minecraftServer;


	@Override
	public void init()
	{
		ModMetadata metadata = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata();
		MOD_NAME = metadata.getName();
		MOD_VERSION = metadata.getVersion().getFriendlyString();

		Path configFile = FabricLoader.getInstance().getConfigDir().resolve("ScheduledTickVisualizer.toml");
		scheduledTickVisualizerConfig = ScheduledTickVisualizerConfig.load(configFile);

		MinecraftServerEvents.READY.register((server) -> {
			scheduledTickVisualizermanager = new ScheduledTickVisualizerManager();
			minecraftServer = server;
		});

		MinecraftServerEvents.TICK_START.register((server) -> {
			scheduledTickVisualizermanager.tick();
		});
	}
}
