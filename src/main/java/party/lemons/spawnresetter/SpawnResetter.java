package party.lemons.spawnresetter;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.minecraft.commands.Commands.*;

public class SpawnResetter implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("spawnresetter");

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				dispatcher.register(literal("spawnreset").requires(s->s.hasPermission(2)).executes(ctx->{
					if(ctx.getSource().isPlayer())
					{
						try{
							spawnMobs(ctx.getSource().getPlayer().blockPosition(), ctx.getSource().getLevel());

						}catch (Exception e)
						{
							e.printStackTrace();
						}
					}

					return 1;
				}))
				);
	}

	public void spawnMobs(BlockPos pos, ServerLevel level)
	{
		ChunkPos chunkPos = level.getChunk(pos).getPos();
		WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));

		for(int xx = -12; xx < 12; xx++)
		{
			for(int zz = -12; zz < 12; zz++)
			{
				ChunkPos genPos = new ChunkPos(chunkPos.x + xx, chunkPos.z + zz);
				NaturalSpawner.spawnMobsForChunkGeneration(level, level.getBiome(pos), genPos, worldgenRandom);

			}
		}

	//	level.getChunkSource().getGenerator().spawnOriginalMobs(new WorldGenRegion(level, List.of(level.getChunk(pos)), ChunkStatus.SPAWN, -1));
	}
}