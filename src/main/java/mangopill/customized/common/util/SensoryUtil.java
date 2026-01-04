package mangopill.customized.common.util;

import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public final class SensoryUtil {
    private SensoryUtil() {}

    /**
     * Converts a Vec3 (floating-point coordinates) to a Vec3i (integer coordinates).
     * <p>
     * This method uses {@link Mth#floor(double)} to convert each coordinate component
     * from double to integer, effectively truncating the decimal part.
     * @param vec3 The Vec3 to convert
     * @return A new Vec3i with floored coordinates
     */
    public static Vec3i vec3toVec3i(Vec3 vec3) {
        return new Vec3i(Mth.floor(vec3.x()), Mth.floor(vec3.y()), Mth.floor(vec3.z()));
    }

    /**
     * Adds a single particle to the level at the specified position with velocity.
     * <p>
     * This method is typically used for client-side particle effects.
     * @param level The level to add the particle to
     * @param type The type of particle to create
     * @param x The x-coordinate for the particle
     * @param y The y-coordinate for the particle
     * @param z The z-coordinate for the particle
     * @param xSpeed The velocity in the x-direction
     * @param ySpeed The velocity in the y-direction
     * @param zSpeed The velocity in the z-direction
     */
    public static void addParticle(Level level, ParticleOptions type, double x, double y, double z,
                                   double xSpeed, double ySpeed, double zSpeed) {
        level.addParticle(type, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    /**
     * Sends particles to all players in a server level with specified parameters.
     * <p>
     * This method is used for server-side particle synchronization.
     * @param <T> The particle type
     * @param serverLevel The server level to send particles in
     * @param type The type of particle to create
     * @param x The x-coordinate for the particle origin
     * @param y The y-coordinate for the particle origin
     * @param z The z-coordinate for the particle origin
     * @param count The number of particles to create
     * @param xOffset The maximum random offset in the x-direction
     * @param yOffset The maximum random offset in the y-direction
     * @param zOffset The maximum random offset in the z-direction
     * @param speed The base speed of the particles
     */
    public static <T extends ParticleOptions> void sendParticle(ServerLevel serverLevel, T type,
                                                                double x, double y, double z, int count,
                                                                double xOffset, double yOffset, double zOffset,
                                                                double speed) {
        serverLevel.sendParticles(type, x, y, z, count, xOffset, yOffset, zOffset, speed);
    }

    /**
     * @see #addRandomParticle(Level, Vec3i, ParticleOptions, RandomSource, float, int, double, double, double, double, double, double, double, double) 
     */
    public static void addRandomParticle(Level level, Vec3i pos, ParticleOptions type, RandomSource random,
                                         float probability, int count,
                                         double xMin, double xMax, double yOffset, double zMin, double zMax) {
        addRandomParticle(level, pos, type, random, probability, count, xMin, xMax, yOffset, zMin, zMax,
                0.0D, 0.0D, 0.0D);
    }
    
    /**
     * Adds random particles within a specified area with velocity and probability.
     * <p>
     * This is an extended version of {@link #addRandomParticle(Level, Vec3i, ParticleOptions, RandomSource, float, int, double, double, double, double, double)}
     * that allows specifying velocity for the created particles.
     * @param level The level to add particles to
     * @param pos The base position (Vec3i) for particle generation
     * @param type The type of particle to create
     * @param random The random source for determining positions and probabilities
     * @param probability The probability (0.0 to 1.0) of creating a particle per attempt
     * @param count The number of attempts to make
     * @param xMin The minimum x-offset from the base position
     * @param xMax The maximum x-offset from the base position
     * @param yOffset The y-offset from the base position
     * @param zMin The minimum z-offset from the base position
     * @param zMax The maximum z-offset from the base position
     * @param xSpeed The velocity in the x-direction for created particles
     * @param ySpeed The velocity in the y-direction for created particles
     * @param zSpeed The velocity in the z-direction for created particles
     */
    public static void addRandomParticle(Level level, Vec3i pos, ParticleOptions type, RandomSource random,
                                         float probability, int count,
                                         double xMin, double xMax, double yOffset, double zMin, double zMax,
                                         double xSpeed, double ySpeed, double zSpeed) {
        int i = 0;
        while (i < count) {
            if (random.nextFloat() < probability) {
                double x = pos.getX() + Math.clamp(random.nextDouble(), xMin, xMax);
                double y = pos.getY() + yOffset;
                double z = pos.getZ() + Math.clamp(random.nextDouble(), zMin, zMax);
                addParticle(level, type, x, y, z, xSpeed, ySpeed, zSpeed);
            }
            i++;
        }
    }

    /**
     * Sends random particles to all players in a server level with specified parameters.
     * <p>
     * This method creates a single batch of particles at a random position within the
     * specified area and sends them to all players in the server level. The entire
     * batch is either created or skipped based on the probability parameter.
     * @param serverLevel The server level to send particles in
     * @param pos The base position (Vec3i) for particle generation
     * @param type The type of particle to create
     * @param random The random source for determining positions and probabilities
     * @param probability The probability (0.0 to 1.0) of creating the particle batch
     * @param count The number of particles to create in the batch
     * @param speed The base speed of the particles
     * @param xMin The minimum x-offset from the base position
     * @param xMax The maximum x-offset from the base position
     * @param yOffset The y-offset from the base position
     * @param zMin The minimum z-offset from the base position
     * @param zMax The maximum z-offset from the base position
     */
    public static void sendRandomParticle(ServerLevel serverLevel, Vec3i pos, ParticleOptions type, RandomSource random,
                                          float probability, int count, double speed,
                                          double xMin, double xMax, double yOffset, double zMin, double zMax) {
        int i = 0;
        while (i < count) {
            if (random.nextFloat() < probability) {
                double x = pos.getX() + Math.clamp(random.nextDouble(), xMin, xMax);
                double y = pos.getY() + yOffset;
                double z = pos.getZ() + Math.clamp(random.nextDouble(), zMin, zMax);
                sendParticle(serverLevel, type, x, y, z, 1, 0.0D, 0.0D, 0.0D, speed);
            }
            i++;
        }
    }

    /**
     * Plays a sound at the specified location in the level.
     * <p>
     * This method delegates to the level's sound system to play a sound effect.
     * It can optionally associate the sound with a specific entity for positional tracking.
     * @param level The level to play the sound in
     * @param entity The entity to associate with the sound (can be null).
     * @param pos The block position where the sound originates.
     * @param sound The sound event to play
     * @param category The sound category (e.g., ambient, player, hostile, etc.)
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    public static void playSound(Level level, @Nullable Entity entity, BlockPos pos, SoundEvent sound,
                                 SoundSource category, float volume, float pitch) {
        if (level.isClientSide) {
            double x = entity == null ? pos.getX() : entity.getX();
            double y = entity == null ? pos.getY() : entity.getY();
            double z = entity == null ? pos.getZ() : entity.getZ();
            level.playLocalSound(x, y, z, sound, category, volume, pitch, false);
            return;
        }
        level.playSound(entity, pos, sound, category, volume, pitch);
    }

    /**
     * Plays a sound effect randomly based on a specified probability.
     * @see #playSound(Level, Entity, BlockPos, SoundEvent, SoundSource, float, float) 
     */
    public static void playRandomSound(Level level, @Nullable Entity entity, BlockPos pos, SoundEvent sound,
                                       SoundSource category, RandomSource random, float probability,
                                       float volume, float pitch) {
        if (random.nextFloat() < probability) {
            playSound(level, entity, pos, sound, category, volume, pitch);
        }
    }
}
