package ga.justreddy.wiki.reggwars.nms.v1_8_R3;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.hologram.IArmorStand;
import ga.justreddy.wiki.reggwars.api.model.hologram.IHologramLine;
import ga.justreddy.wiki.reggwars.model.game.team.GameTeam;
import ga.justreddy.wiki.reggwars.nms.Nms;
import ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity.VillagerShop;
import ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity.hologram.EntityHologram;
import ga.justreddy.wiki.reggwars.packets.FakeTeam;
import ga.justreddy.wiki.reggwars.packets.FakeTeamManager;
import ga.justreddy.wiki.reggwars.utils.NumberUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.*;

public final class v1_8_R3 implements Nms {


    private static int id = 0;

    @Override
    public boolean isLegacyVersion() {
        return true;
    }

    @Override
    public void sendJsonMessage(Player player, String json) {
        final IChatBaseComponent component = IChatBaseComponent
                .ChatSerializer.a(json);
        ((CraftPlayer) player).getHandle()
                .playerConnection
                .sendPacket(new PacketPlayOutChat(component));


    }

    @Override
    public void sendTitle(Player player, String title, String subtitle) {
        PlayerConnection connection = ((CraftPlayer) player)
                .getHandle().playerConnection;
        PacketPlayOutTitle titleInfo = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.TIMES,
                null,
                20,
                60,
                20
        );
        connection.sendPacket(titleInfo);
        if (title != null) {
            IChatBaseComponent component = IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" +
                            title +
                            "\"}");
            connection.sendPacket(new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.TITLE,
                    component
            ));
        }

        if (subtitle != null) {
            IChatBaseComponent component = IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" +
                            subtitle +
                            "\"}");
            connection.sendPacket(new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                    component
            ));
        }

    }

    @Override
    public void sendActionbar(Player player, String actionBar) {
        if (actionBar == null) return;
        IChatBaseComponent component = IChatBaseComponent.ChatSerializer
                .a("{\"text\": \"" +
                        actionBar +
                        "\"}");
        PacketPlayOutChat chat = new PacketPlayOutChat(
                component, (byte) 2
        );
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
    }

    @Override
    public void spawnParticle(Location location, String type, int amount, float offsetX, float offsetY, float offsetZ, float data) {
        if (type == null) return;
        EnumParticle particle = EnumParticle.valueOf(type);
        float x = (float) location.getBlockX();
        float y = (float) location.getBlockY();
        float z = (float) location.getBlockZ();
        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(particle, true, x, y, z, offsetX, offsetY, offsetZ, data, amount, 1);
        for (final Player player : location.getWorld().getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
        }
    }

    @Override
    public boolean isParticleCorrect() {
        return true;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new ChunkGenerator() {
            @Override
            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
                return createChunkData(world);
            }
        };
    }

    @Override
    public void setWorldRule(World world, String rule, boolean value) {
        String stringValue = "false";
        if (value) stringValue = "true";
        world.setGameRuleValue(rule, stringValue);
    }

    @Override
    public void removeEntityAI(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity
                = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setBoolean("NoAI", true);
        tag.setBoolean("Silent", true);
        nmsEntity.f(tag);
    }

    @Override
    public void setGravity(Entity entity, boolean gravity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity
                = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setBoolean("Gravity", gravity);
        nmsEntity.f(tag);
    }


    @Override
    public void setPlayerListName(Player player, Player otherPlayer, String name) {

    }

    @Override
    public void setTeamName(IGameTeam team) {
        Map<UUID, List<FakeTeam>> TEAMS = FakeTeamManager.getPlayerTeams();
        int priority = NumberUtil.getPriority(team);
        FakeTeam fakeTeam = new FakeTeam(team.getTeam().getTag(), "", priority);
        for (IGamePlayer player : team.getAlivePlayers()) {
            List<FakeTeam> teams = TEAMS.getOrDefault(player.getUniqueId(), new ArrayList<>());
            if (!teams.isEmpty()) teams.clear();
            teams.add(fakeTeam);
            TEAMS.put(player.getUniqueId(), teams);
            TEAMS.get(player.getUniqueId()).get(0).addMember(player.getName());
        }


        for (IGamePlayer player : team.getGame().getPlayers()) {
            for (IGamePlayer players : team.getPlayers()) {
                List<FakeTeam> teams = TEAMS.getOrDefault(players.getUniqueId(), new ArrayList<>());
                FakeTeamManager.sendTeam(players.getPlayer(), teams.get(0));
                if (players == player) continue;
                FakeTeamManager.sendTeam(player.getPlayer(), teams.get(0));
            }
        }





    }

    @Override
    public void setTeamName(IGamePlayer player) {
        IGameTeam team = player.getTeam();
        Map<UUID, List<FakeTeam>> TEAMS = FakeTeamManager.getPlayerTeams();
        int priority = NumberUtil.getPriority(team);
        List<FakeTeam> teams = TEAMS.getOrDefault(player.getUniqueId(), new ArrayList<>());
        if (!teams.isEmpty()) teams.clear();
        FakeTeam fakeTeam = new FakeTeam(team.getTeam().getTag(), "", priority);
        teams.add(fakeTeam);
        TEAMS.put(player.getUniqueId(), teams);
        TEAMS.get(player.getUniqueId()).get(0).addMember(player.getName());
        // TODO this
        for (IGamePlayer p : team.getGame().getPlayers()) {
            for (IGamePlayer players : team.getPlayers()) {
                List<FakeTeam> t = TEAMS.getOrDefault(players.getUniqueId(), new ArrayList<>());
                FakeTeamManager.sendTeam(players.getPlayer(), t.get(0));
                if (players == p) continue;
                FakeTeamManager.sendTeam(p.getPlayer(), t.get(0));
            }
        }
    }

    @Override
    public void setWaitingLobbyName(IGamePlayer player) {

    }

    @Override
    public void removeWaitingLobbyName(IGame game) {

    }

    @Override
    public void removeWaitingLobbyName(IGame game, IGamePlayer player) {

    }

    @Override
    public Block getRelative(Location location) {
        if (location.getBlock().getType() == XMaterial.OAK_WALL_SIGN.parseMaterial())
            switch (location.getBlock().getData()) {
                case 2:
                    return location.getBlock().getRelative(BlockFace.SOUTH);
                case 3:
                    return location.getBlock().getRelative(BlockFace.NORTH);
                case 4:
                    return location.getBlock().getRelative(BlockFace.EAST);
                case 5:
                    return location.getBlock().getRelative(BlockFace.WEST);
            }
        return null;
    }

    @Override
    public void spawnVillager(Location location) {
        // TODO add hologram
        VillagerShop shop = new VillagerShop(location);
        removeEntityAI(shop.getBukkitEntity());
        ((CraftLivingEntity) shop.getBukkitEntity()).setRemoveWhenFarAway(false);
    }

    @Override
    public IArmorStand spawnArmorStand(Location location, IGamePlayer player, String line, IHologramLine hologramLine) {
        return EntityHologram.spawn(location, player, line, hologramLine);
    }
}
