package org.pseudonymcode.naturalis.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.pseudonymcode.naturalis.Game;
import org.pseudonymcode.naturalis.items.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    public enum SlotType {
        // Individual objects handle their own special logic with an update() function (called from the inventory's update() function)
        Blank, // takes up a spot in the inventory, but doesn't show as anything or do anything
        Selectable, // if ItemStack is double-clicked on, inventory closes and the ItemStack is returned as a result
        Display, // item texture is shown in slot, but cannot be interacted with
        MovableTo, // item can be put into slot but not taken out
        MovableFrom, // item can be taken out of slot but not put into
        MovableBoth, // item can be both taken out and put into the slot
        Help, // help button that, when hovered over, will give a tooltip of info (probably on how to use the machine inventory)

        // Special
        PlayerStorage, // is a slot that connects to the player's storage. This is used primarily to fill these slots easily
        Machine // is a slot that is intended to have a machine interact in some way (just a way to target the slot easier)
    }
    public interface InventoryOwner {
        // Functions for maintaining & creating the inventory's UI objects
        public Inventory generateInventory();
        public void updateInventory(Inventory inventory); // update the currently open inventory (probably to show any changes made to your internals)
        public boolean shouldCallInventoryUpdate(); // if this returns true, your updateInventory() function will be called next frame
        public boolean onInventorySlotClick(SlotType slotType, int number); // when a button is clicked, this is called before doing the normal behavior of moving and placing ItemStacks (if false is returned, normal behavior doesn't happen)

        // Functions for interfacing with the storage (ItemStacks in an inventory) (also may be more than one storage list internally, so these should handle that)
        public boolean insertIntoStorage(ItemStack itemStack); // called when something wants to put an ItemStack into the object's inventory (
        public boolean outputFromStorage(ItemStack itemStack); // called when something wants to take an ItemStack from the object's storage (again, the object may have multiple storage lists or extra, logic, this should handle it)
    }
    public class Slot {
        public SlotType slotType;
        public TextButton button;
        public Slot(SlotType newSlotType, TextButton newButton) {
            slotType = newSlotType;
            button = newButton;
        }
    }

    public static final int SLOT_SIZE = 75;

    private Table table;
    private List<Slot> slots;
    private InventoryOwner source;
    private boolean deleteFlag = false; // when true, the UIHandler (checking every frame) will stop rendering this Inventory and delete it from its memory
    private ItemStack hoverItemStack;

    private TextButton generateSlot() {
        TextButton button = new TextButton("", Game.getUiHandler().getSkin());
        button.setSize(SLOT_SIZE, SLOT_SIZE);
        // add event handler for when the button is clicked
        return button;
    }

    public Inventory(List<SlotType> newSlots, int width, int height, String background, InventoryOwner newSource) {
        source = newSource;
        slots = new ArrayList<>();
        hoverItemStack = null;
        table = new Table();
        table.setWidth((float)((SLOT_SIZE * width) + SLOT_SIZE));
        table.setHeight((float)((SLOT_SIZE * height) + SLOT_SIZE));
        table.setBackground(new TextureRegionDrawable(Game.getAssetHandler().getAssetManager().get("ui/backgrounds/" + background + ".png", Texture.class)));
        table.setPosition(Gdx.graphics.getWidth()/2f - table.getWidth()/2f, Gdx.graphics.getHeight()/2f - table.getHeight()/2f);

        int count = 0;
        for (SlotType slotType : newSlots) {
            TextButton button;
            switch (slotType) {
                case PlayerStorage:
                    button = generateSlot();
                    slots.add(new Slot(slotType, button));
                    break;
                default:
                    button = generateSlot();
                    slots.add(new Slot(slotType, button));
                    break;
            }
            table.add(button).size(SLOT_SIZE, SLOT_SIZE).expandX().expandY();
            if (count == width-1) {
                table.row();
                count = 0;
            }
            else count += 1;
        }
    }

    // This is called to update any slots that are live (need to be current, like player storage)
    public void update() {
        // Do any updates mandated by the SlotTypes

        if (source.shouldCallInventoryUpdate()) source.updateInventory(this);
    }

    // Helper method to set a Slot at position in the slots list to show the given item
    public void setItem(ItemStack itemStack, int position) {
        TextButton button = slots.get(position).button;
        button.setBackground(new TextureRegionDrawable(Game.getAssetHandler().getAssetManager().get("items/" + itemStack.item.name + ".png", Texture.class)));
        button.setText(Integer.toString(itemStack.count));
    }

    // Helper method to load the player's storage into inventory slots of type PlayerStorage
    public void setPlayerStorageSlots() {
        List<ItemStack> stacks = Game.getPlayer().getStorage();
        for (int i = 0; i < stacks.size(); i++) {
            Slot slot = slots.get(i);
            ItemStack itemStack = stacks.get(i);
            if (slot.slotType == SlotType.PlayerStorage && itemStack != null) {
                setItem(itemStack, i);
            }
        }
    }

    public Table getTable() {
        return table;
    }
}
