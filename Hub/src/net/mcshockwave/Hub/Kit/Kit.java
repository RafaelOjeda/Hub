package net.mcshockwave.Hub.Kit;

import net.mcshockwave.Hub.HubPlugin;
import net.mcshockwave.Hub.Commands.PVPCommand;
import net.mcshockwave.MCS.Menu.ItemMenu;
import net.mcshockwave.MCS.Menu.ItemMenu.Button;
import net.mcshockwave.MCS.Menu.ItemMenu.ButtonRunnable;
import net.mcshockwave.MCS.Utils.ItemMetaUtils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum Kit {

	Warrior(
		i(Material.LEATHER_HELMET),
		i(Material.CHAINMAIL_CHESTPLATE),
		i(Material.CHAINMAIL_LEGGINGS),
		i(Material.LEATHER_BOOTS),
		i(Material.IRON_SWORD)),
	Archer(
		i(Material.IRON_HELMET),
		i(Material.LEATHER_CHESTPLATE),
		i(Material.LEATHER_LEGGINGS),
		i(Material.IRON_BOOTS),
		i(Material.WOOD_SWORD),
		i(Material.BOW),
		i(Material.ARROW, 1)),
	Tank(
		i(Material.IRON_HELMET),
		i(Material.IRON_CHESTPLATE),
		i(Material.IRON_LEGGINGS),
		i(Material.IRON_BOOTS),
		i(Material.IRON_AXE)),
	Mage(
		i(Material.GOLD_HELMET),
		i(Material.GOLD_CHESTPLATE),
		i(Material.AIR),
		i(Material.AIR),
		i(Material.BLAZE_ROD),
		i(Material.NETHER_STAR)),
	Assassin(
		i(Material.AIR),
		i(Material.AIR),
		i(Material.AIR),
		i(Material.LEATHER_BOOTS),
		i(Material.SHEARS),
		i(Material.GLOWSTONE_DUST, 5)),
	Medic(
		i(Material.LEATHER_HELMET),
		i(Material.LEATHER_CHESTPLATE),
		i(Material.GOLD_LEGGINGS),
		i(Material.GOLD_BOOTS),
		i(Material.STONE_SWORD),
		i(Material.BEACON)),
	Demoman(
		i(Material.LEATHER_HELMET),
		i(Material.LEATHER_CHESTPLATE),
		i(Material.LEATHER_LEGGINGS),
		i(Material.LEATHER_BOOTS),
		i(Material.STONE_AXE),
		i(Material.IRON_BARDING)),
	Engineer(
		i(Material.LEATHER_HELMET),
		i(Material.LEATHER_CHESTPLATE),
		i(Material.IRON_LEGGINGS),
		i(Material.LEATHER_BOOTS),
		i(Material.STONE_SWORD),
		i(Material.DISPENSER)),
	Pyro(
		i(Material.GOLD_HELMET),
		i(Material.CHAINMAIL_CHESTPLATE),
		i(Material.GOLD_LEGGINGS),
		i(Material.GOLD_BOOTS),
		i(Material.GOLD_AXE),
		i(Material.FLINT_AND_STEEL));

	public ItemStack[]	acontents;
	public ItemStack[]	contents;

	private Kit(ItemStack h, ItemStack c, ItemStack l, ItemStack b, ItemStack... items) {
		acontents = new ItemStack[] { b, l, c, h };
		contents = items;
	}
	
	public ItemStack getIcon() {
		if (contents.length > 1 && contents[1].getAmount() == 1) {
			return contents[1];
		}
		return contents[0];
	}

	public static ItemMenu getSelectorMenu(Player p) {
		int le = values().length;
		ItemMenu m = new ItemMenu("Kits", ((le + 8) / 9) * 9);

		for (int i = 0; i < le; i++) {
			final Kit k = values()[i];

			Button b = new Button(true, k.getIcon().getType(), 1, k.getIcon().getDurability(), "Kit - �a"
					+ k.name(), "Click to use");
			m.addButton(b, i);
			b.setOnClick(new ButtonRunnable() {
				public void run(Player p, InventoryClickEvent event) {
					p.teleport(PVPCommand.arena(HubPlugin.dW()));
					p.sendMessage("�aEntering arena with kit " + k.name());

					k.use(p);
				}
			});
		}

		return m;
	}

	@SuppressWarnings("deprecation")
	public void use(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(acontents);
		for (ItemStack it : contents) {
			if (it.getType() == Material.BOW) {
				it = ItemMetaUtils.addEnchantment(it, Enchantment.ARROW_DAMAGE, 1);
				it = ItemMetaUtils.addEnchantment(it, Enchantment.ARROW_KNOCKBACK, 1);
				it = ItemMetaUtils.addEnchantment(it, Enchantment.ARROW_INFINITE, 1);
			}
			if (it.getType() == Material.ARROW) {
				p.getInventory().setItem(17, it);
			} else
				p.getInventory().addItem(it);
		}
		p.updateInventory();
		clearPE(p);

		if (this == Tank) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
		}
		if (this == Assassin) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
		}
		if (this == Archer) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
		}

		HubPlugin.petApi.removePet(p, false, true);
	}

	public static void clearPE(Player p) {
		for (PotionEffect pe : p.getActivePotionEffects()) {
			p.removePotionEffect(pe.getType());
		}
	}

	public static ItemStack i(Material m) {
		return new ItemStack(m);
	}

	public static ItemStack i(Material m, int a) {
		return new ItemStack(m, a);
	}

	public static ItemStack i(Material m, int a, int d) {
		return new ItemStack(m, a, (short) d);
	}

}