package com.omnipico.picoitemlib;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.*;

import java.util.Arrays;
import java.util.List;

public class PicoItemListener implements Listener {

    static final List<Material> PROHIBITED_INTERACTIONS = Arrays.asList(Material.COMPOSTER, Material.FARMLAND, Material.JUKEBOX, Material.LECTERN, Material.LODESTONE, Material.CAULDRON, Material.TNT, Material.GRINDSTONE, Material.FLOWER_POT);
    static final List<InventoryType> PROHIBITED_INSERTIONS = Arrays.asList(InventoryType.STONECUTTER, InventoryType.BREWING, InventoryType.SMOKER, InventoryType.GRINDSTONE, InventoryType.LECTERN, InventoryType.BEACON, InventoryType.BLAST_FURNACE, InventoryType.CARTOGRAPHY, InventoryType.LOOM, InventoryType.FURNACE);

    /*
    TODO: Tag to allow item renaming
    TODO: Tag to allow enchanting
    TODO: More sophisticated block denying
    TODO: Make an option for them to be placeable (hard)
     */

    public boolean isPicoItem(ItemStack item) {
        if (item == null) return false;

        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (!nmsItem.hasTag()) {
            return false;
        }
        NBTTagCompound tag = nmsItem.getTag();
        assert tag != null;
        if (tag.hasKey("pico_id")) {
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onCraft(CraftItemEvent event) {
        CraftingInventory craftingTable = event.getInventory();
        for (ItemStack item : craftingTable.getMatrix()) {
            if (isPicoItem(item)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory craftingTable = event.getInventory();
        for (ItemStack item : craftingTable.getMatrix()) {
            if (isPicoItem(item)) {
                craftingTable.setResult(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onPlace(BlockPlaceEvent event) {
        if (isPicoItem(event.getItemInHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onConsume(PlayerItemConsumeEvent event) {
        if (isPicoItem(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        /*
        Bukkit.getLogger().info("Information for " + event.getItem().toString());
        Bukkit.getLogger().info(event.getAction().toString());
        Bukkit.getLogger().info(event.isBlockInHand() ? "Block in hand" : "Block not in hand");
        Bukkit.getLogger().info(event.hasBlock() ? "Has Block" : "Does not have Block");
        Bukkit.getLogger().info(event.hasItem() ? "Has Item" : "Does not have Item");
        Bukkit.getLogger().info(event.useItemInHand().toString());
        Bukkit.getLogger().info(event.useInteractedBlock().toString());
        */
        if (isPicoItem(event.getItem()) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setUseItemInHand(Event.Result.DENY);
            if (block != null && PROHIBITED_INTERACTIONS.contains(block.getType())) {
                event.setCancelled(true);
            }

            if (event.isBlockInHand()) {
                //TODO: More sophisticated detection of block placement
                event.setCancelled(true);
            }

        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onDispense(BlockDispenseEvent event) {
        if (event.getVelocity().length()==0 && isPicoItem(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onPrepareEnchant(PrepareItemEnchantEvent event) {
        if (isPicoItem(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onEnchant(EnchantItemEvent event) {
        if (isPicoItem(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        if (isPicoItem(inventory.getItem(0)) || isPicoItem(inventory.getItem(1))) {
            event.setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClick() == ClickType.NUMBER_KEY && PROHIBITED_INSERTIONS.contains(event.getInventory().getType())) {
            if (isPicoItem(event.getWhoClicked().getInventory().getItem(event.getHotbarButton()))) {
                event.setCancelled(true);
            }
        }
        if (event.getClick().isShiftClick()) {
            if (event.getClickedInventory() == event.getWhoClicked().getInventory()) {
                if (isPicoItem(event.getCurrentItem()) && PROHIBITED_INSERTIONS.contains(event.getWhoClicked().getOpenInventory().getType())) {
                    event.setCancelled(true);
                }
            }
        }
        if (event.getClickedInventory() != null && PROHIBITED_INSERTIONS.contains(event.getClickedInventory().getType())) {
            if (isPicoItem(event.getCursor())) {
                event.setCancelled(true);
            }
        }
        if (event.getClickedInventory() instanceof AnvilInventory) {
            AnvilInventory anvilInventory = (AnvilInventory) event.getClickedInventory();
            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                if (isPicoItem(anvilInventory.getItem(0)) || isPicoItem(anvilInventory.getItem(1))) {
                    event.setCancelled(true);
                }
            }
        } else if (event.getClickedInventory() instanceof SmithingInventory) {
            SmithingInventory smithingInventory = (SmithingInventory) event.getClickedInventory();
            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                if (isPicoItem(smithingInventory.getItem(0)) || isPicoItem(smithingInventory.getItem(1))) {
                    event.setCancelled(true);
                }
            }
        } else if (event.getClickedInventory() instanceof StonecutterInventory) {
            StonecutterInventory stonecutterInventory = (StonecutterInventory) event.getClickedInventory();
            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                if (isPicoItem(stonecutterInventory.getItem(0))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onInventoryDrag(InventoryDragEvent event) {
        if (isPicoItem(event.getOldCursor())) {
            if (PROHIBITED_INSERTIONS.contains(event.getInventory().getType())) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if (isPicoItem(event.getItem()) && PROHIBITED_INSERTIONS.contains(event.getDestination().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // Listening for the event.
    public void onPrepareSmith(PrepareSmithingEvent event) {
        SmithingInventory inventory = event.getInventory();
        if (isPicoItem(inventory.getItem(0)) || isPicoItem(inventory.getItem(1))) {
            event.setResult(null);
            //inventory.setItem(3, null);
        }
    }
}
