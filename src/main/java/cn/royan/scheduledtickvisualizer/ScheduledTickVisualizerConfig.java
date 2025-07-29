package cn.royan.scheduledtickvisualizer;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScheduledTickVisualizerConfig {
	public Double offset = 0.8;
	public boolean detail = true;
	public long delay = 20;
	public float distance = 10;

	public static ScheduledTickVisualizerConfig load(Path configPath) {
		ScheduledTickVisualizerConfig config;
		if(Files.exists(configPath)) {
			config = new Toml().read(configPath.toFile()).to(ScheduledTickVisualizerConfig.class);
		} else {
			config = new ScheduledTickVisualizerConfig();
		}

		File configFile = configPath.toFile();
		try {
			new TomlWriter().write(config, configFile);
		} catch (IOException e) {
			ScheduledTickVisualizer.LOGGER.error("Init config failed.", e);
		}

		return config;
	}
}
