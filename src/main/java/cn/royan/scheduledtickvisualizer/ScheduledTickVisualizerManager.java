package cn.royan.scheduledtickvisualizer;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScheduledTickVisualizerManager {
	private static HashMap<BlockPos, ScheduledTickEntities> entitys = new HashMap<>();
	public static List<UUID> uuids = new ArrayList<>();

	public void tick(){
		if(ScheduledTickVisualizer.scheduledTickVisualizerConfig.delay == 0) return;

		List<BlockPos> willDel = new ArrayList<>();

		entitys.forEach((pos, scheduledTickEntities) -> {
			if(scheduledTickEntities.tick()){
				scheduledTickEntities.discord();
				willDel.add(pos);
			}
		});

		willDel.forEach(pos -> {
			entitys.remove(pos);
		});
	}

	public void addScheduledTickEntities(BlockPos pos, ScheduledTickEntities scheduledTickEntities) {
		if(entitys.get(pos) != null || !getPlayersNearBy(pos, ScheduledTickVisualizer.scheduledTickVisualizerConfig.distance)) return;
		scheduledTickEntities.summonEntity();
		entitys.put(pos, scheduledTickEntities);
	}

	public void removeScheduledTickEntities(BlockPos pos) {
		if(entitys.get(pos) == null) return;
		entitys.get(pos).discord();
		entitys.remove(pos);
	}

	public static boolean getPlayersNearBy(BlockPos blockPos, float distance){
		for(ServerPlayerEntity player : ScheduledTickVisualizer.minecraftServer.getPlayerManager().getAll()){
			double playerX = player.x;
			double playerY = player.y;
			double playerZ = player.z;

			double blockX = blockPos.getX() + 0.5;
			double blockY = blockPos.getY() + 0.5;
			double blockZ = blockPos.getZ() + 0.5;

			double distanceSquared = Math.pow(blockX - playerX, 2)
				+ Math.pow(blockY - playerY, 2)
				+ Math.pow(blockZ - playerZ, 2);

			if(distanceSquared < distance*distance){
				return true;
			}
		}
		return false;
	}
}
