package riskyken.armourersWorkshop.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.common.lib.LibModInfo;

public abstract class AbstractModBlock extends Block {
    
    public AbstractModBlock(String name) {
        super(Material.iron);
        setCreativeTab(ArmourersWorkshop.tabArmorersWorkshop);
        setHardness(3.0F);
        setStepSound(soundTypeMetal);
        setUnlocalizedName(name);
    }
    
    public AbstractModBlock(String name, Material material, SoundType soundType, boolean addCreativeTab) {
        super(material);
        if (addCreativeTab) {
            setCreativeTab(ArmourersWorkshop.tabArmorersWorkshop);
        }
        setHardness(3.0F);
        setStepSound(soundTypeMetal);
        setUnlocalizedName(name);
    }
    
    @Override
    public Block setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(LibModInfo.ID, "tile." + name));
        GameRegistry.registerBlock(this);
//        GameRegistry.registerItem(new ModItemBlock(this), "tile." + name,LibModInfo.ID);
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return getModdedUnlocalizedName(super.getUnlocalizedName());
    }

    protected String getModdedUnlocalizedName(String unlocalizedName) {
        String name = unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
        return "tile." + LibModInfo.ID.toLowerCase() + ":" + name;
    }
}