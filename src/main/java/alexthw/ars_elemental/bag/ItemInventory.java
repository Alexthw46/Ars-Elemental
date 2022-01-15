package alexthw.ars_elemental.bag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class ItemInventory extends Inventory
{
    private final ItemStack stack;

    public ItemInventory(ItemStack stack, int expectedSize)
    {
        super(expectedSize);
        this.stack = stack;

        ListNBT list = stack.getOrCreateTag().getList("items", 10);
        int i = 0;
        for (; i < expectedSize && i < list.size(); i++)
        {
            setItem(i, ItemStack.of(list.getCompound(i)));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return !stack.isEmpty();
    }

    @Override
    public void setChanged()
    {
        super.setChanged();
        ListNBT list = new ListNBT();
        for (int i = 0; i < getContainerSize(); i++)
        {
            list.add(getItem(i).save(new CompoundNBT()));
        }
        stack.getOrCreateTag().put("items", list);
    }
}