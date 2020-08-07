package motherlode.core.item;

import motherlode.core.Motherlode;
import motherlode.core.assets.ItemModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import static motherlode.core.registry.MotherlodeItems.register;

public class MaterialToolsAndArmor {

    public final Item AXE;
    public final Item HOE;
    public final Item PICKAXE;
    public final Item SHOVEL;
    public final Item SWORD;

    public final Item HELMET;
    public final Item CHESTPLATE;
    public final Item LEGGINGS;
    public final Item BOOTS;

    public MaterialToolsAndArmor(DefaultToolMaterial toolMaterial, ArmorMaterial armorMaterial) {
        this.AXE = register(toolMaterial.getName() + "_axe", ItemModel.HANDHELD, new MaterialAxe(toolMaterial));
        this.HOE = register(toolMaterial.getName() + "_hoe", ItemModel.HANDHELD, new MaterialHoe(toolMaterial));
        this.PICKAXE = register(toolMaterial.getName() + "_pickaxe", ItemModel.HANDHELD, new MaterialPickaxe(toolMaterial));
        this.SHOVEL = register(toolMaterial.getName() + "_shovel", ItemModel.HANDHELD, new ShovelItem(toolMaterial, 1.5F, 3.0F, new Item.Settings().maxCount(1).group(Motherlode.ARMOUR_AND_TOOLS)));
        this.SWORD = register(toolMaterial.getName() + "_sword", ItemModel.HANDHELD, new SwordItem(toolMaterial, 3, -2.4F, new Item.Settings().maxCount(1).group(Motherlode.ARMOUR_AND_TOOLS)));

        this.HELMET = register(toolMaterial.getName() + "_helmet", ItemModel.DEFAULT, new ArmorItem(armorMaterial, EquipmentSlot.HEAD, new Item.Settings().group(Motherlode.ARMOUR_AND_TOOLS)));
        this.CHESTPLATE = register(toolMaterial.getName() + "_chestplate", ItemModel.DEFAULT, new ArmorItem(armorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(Motherlode.ARMOUR_AND_TOOLS)));
        this.LEGGINGS = register(toolMaterial.getName() + "_leggings", ItemModel.DEFAULT, new ArmorItem(armorMaterial, EquipmentSlot.LEGS, new Item.Settings().group(Motherlode.ARMOUR_AND_TOOLS)));
        this.BOOTS = register(toolMaterial.getName() + "_boots", ItemModel.DEFAULT, new ArmorItem(armorMaterial, EquipmentSlot.FEET, new Item.Settings().group(Motherlode.ARMOUR_AND_TOOLS)));
    }

}

class MaterialPickaxe extends PickaxeItem {
    public MaterialPickaxe(DefaultToolMaterial material) {
        super(material, 1, -2.8F, new Item.Settings().maxCount(1).group(Motherlode.ARMOUR_AND_TOOLS));
    }
}

class MaterialAxe extends AxeItem {
    public MaterialAxe(DefaultToolMaterial material) {
        super(material, 6, -3.1F, new Item.Settings().maxCount(1).group(Motherlode.ARMOUR_AND_TOOLS));
    }
}

class MaterialHoe extends HoeItem {
    public MaterialHoe(DefaultToolMaterial material) {
        super(material, -2, -1F, new Item.Settings().maxCount(1).group(Motherlode.ARMOUR_AND_TOOLS));
    }
}
