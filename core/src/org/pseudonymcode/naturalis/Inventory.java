package org.pseudonymcode.naturalis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
        public void onInventorySlotClick(Inventory source, Slot slot, int number, int mouseButtonUsed); // when a button is clicked, this is called before doing the normal behavior of moving and placing ItemStacks (should call the Inventory function doDefaultSlotClick() for default behavior)

        // Functions for interfacing with the storage (ItemStacks in an inventory) (also may be more than one storage list internally, so these should handle that)
        public boolean insertIntoStorage(ItemStack itemStack, int inputNumber);// called when something wants to put an ItemStack into the object's inventory (and let the object handle how that happens) at specified input (if the object has multiple, this is nice, especially if using some kind of machine that lets you choose. Just ignore the second param if not needed)
        public boolean outputFromStorage(ItemStack itemStack, int outputNumber); // called when something wants to take an ItemStack from the object's storage (again, the object may have multiple storage lists or extra, logic, this should handle it)
    }
    public class Slot {
        public SlotType slotType;
        public TextButton button;
        public Image image;
        public boolean canMoveTo;
        public boolean canMoveFrom;
        public Slot(SlotType newSlotType, TextButton newButton, boolean newCanMoveTo, boolean newCanMoveFrom) {
            slotType = newSlotType;
            button = newButton;
            image = new Image();
            image.setDrawable(NULL_ITEM_DRAWABLE);
            canMoveTo = newCanMoveTo;
            canMoveFrom = newCanMoveFrom;
        }
    }

    public static final int SLOT_SIZE = 75;
    public static final Drawable NULL_ITEM_DRAWABLE = Game.getAssetHandler().getDrawableFromTexturePath("items/null.png");

    private Table buttonTable;
    private Table imageTable;
    private List<Slot> slots;
    private InventoryOwner source;
    private ItemStack hoverItemStack;

    // Helper function that is called when a button from a slot in the inventory is clicked on by a player
    private void onPlayerSlotInteraction(int slotPos, int buttonUsed) {
        Slot slot = slots.get(slotPos);
        source.onInventorySlotClick(this, slot, slotPos, buttonUsed);
        if (hoverItemStack != null) Game.getUiHandler().setItemHover(Game.getAssetHandler().getDrawableFromTexturePath("items/" + hoverItemStack.item.name + ".png"), hoverItemStack.count);
        else Game.getUiHandler().setItemHoverEmpty();
    }

    // Can be called by an InventoryOwner from their onInventorySlotClick() function to have normal left and right click behavior on the clicked on Slot
    // Caller should set the clicked on stack in their storage to the return ItemStack of this function (it handles the hover stack)
    public ItemStack doDefaultSlotClick(ItemStack stack, int mouseButtonUsed) {
        if (mouseButtonUsed == Input.Buttons.LEFT) { // left click
            System.out.println("Left clicked a slot!");
            if (stack != null) {
                if (hoverItemStack == null) {
                    hoverItemStack = stack;
                    return null; // caller should set their inventory slot to null, the item has been moved
                }
                else {
                    if (ItemStack.areStacksCombinable(stack, hoverItemStack)) { // combine stacks, removing the hover stack
                        ItemStack newItemStack = ItemStack.combineStacks(stack, hoverItemStack);
                        hoverItemStack = null;
                        return newItemStack;
                    }
                    else { // switch hover stack with clicked on stack
                        ItemStack tempStack = hoverItemStack;
                        hoverItemStack = stack;
                        return tempStack;
                    }
                }
            }
            else if (hoverItemStack != null) { // clicked on stack is null, so see if hover item exists
                ItemStack newItemStack = hoverItemStack;
                hoverItemStack = null;
                return newItemStack;
            }
        }
        else if (mouseButtonUsed == Input.Buttons.RIGHT) { // right click
            System.out.println("Right clicked a slot!");
            if (hoverItemStack != null) {
                if (ItemStack.areStacksCombinable(stack, hoverItemStack) || stack == null) { // transfer one item from hover stack into stack
                    ItemStack tempHover = ItemStack.createItemStack(hoverItemStack.item, hoverItemStack.count);
                    assert tempHover != null; // don't know why this is wanted, but it's here so there you go
                    hoverItemStack = ItemStack.createItemStack(tempHover.item, tempHover.count - 1);
                    return ItemStack.createItemStack(tempHover.item, stack == null ? 1 : stack.count+1);
                }
            }
            else if (stack != null) {
                hoverItemStack = ItemStack.createItemStack(stack.item, (int)Math.ceil((double)stack.count/2.0));
                return ItemStack.createItemStack(stack.item, (int)Math.floor((double)stack.count/2.0));
            }
        }
        return stack; // caller does nothing on this return
    }

    // Functions to generate an inventory
    private Slot generateSlot(final int slotNum, SlotType type, boolean canMoveTo, boolean canMoveFrom) {
        TextButton button = new TextButton("", Game.getUiHandler().getSkin());
        button.setSize(SLOT_SIZE, SLOT_SIZE);
        // event handler for when the button is clicked
        button.addListener(new ClickListener(-1) { // listens on every button with -1
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onPlayerSlotInteraction(slotNum, event.getButton());
            }
        });

        return new Slot(type, button, canMoveTo, canMoveFrom);
    }

    public Inventory(List<SlotType> newSlots, int width, int height, String background, InventoryOwner newSource) {
        source = newSource;
        slots = new ArrayList<>();
        hoverItemStack = null;
        buttonTable = new Table();
        buttonTable.setWidth((float)((SLOT_SIZE * width) + SLOT_SIZE));
        buttonTable.setHeight((float)((SLOT_SIZE * height) + SLOT_SIZE));
        buttonTable.setPosition(Gdx.graphics.getWidth()/2f - buttonTable.getWidth()/2f, Gdx.graphics.getHeight()/2f - buttonTable.getHeight()/2f);
        imageTable = new Table();
        imageTable.setWidth((float)((SLOT_SIZE * width) + SLOT_SIZE));
        imageTable.setHeight((float)((SLOT_SIZE * height) + SLOT_SIZE));
        imageTable.setBackground(Game.getAssetHandler().getDrawableFromTexturePath("ui/backgrounds/" + background + ".png"));
        imageTable.setPosition(Gdx.graphics.getWidth()/2f - buttonTable.getWidth()/2f, Gdx.graphics.getHeight()/2f - buttonTable.getHeight()/2f);

        int count = 0;
        for (int i = 0; i < newSlots.size(); i++) {
            SlotType slotType = newSlots.get(i);
            Slot slot;
            switch (slotType) {
                case PlayerStorage:
                    slot = generateSlot(i, SlotType.PlayerStorage, true, true);
                    slots.add(slot);
                    break;
                default:
                    slot = generateSlot(i, slotType, true, true); // assumes player can move items to and from the slot if unrecognized SlotType :)
                    slots.add(slot);
                    break;
            }
            buttonTable.add(slot.button).size(SLOT_SIZE, SLOT_SIZE).expandX().expandY();
            imageTable.add(slot.image).size(SLOT_SIZE, SLOT_SIZE).expandX().expandY();
            if (count == width-1) {
                buttonTable.row();
                imageTable.row();
                count = 0;
            }
            else count += 1;
        }
    }

    // This is called to update any slots that are live (need to be current, like player storage)
    public void update() {
        // Do any updates mandated by the SlotTypes

        source.updateInventory(this);
    }

    // Helper method to set a Slot at position in the slots list to show the given item
    public void setItem(ItemStack itemStack, int position) {
        Slot slot = slots.get(position);
        if (itemStack == null) {
            slot.button.setText("");
            slot.image.setDrawable(NULL_ITEM_DRAWABLE);
        }
        else {
            slot.button.setText(Integer.toString(itemStack.count));
            slot.image.setDrawable(Game.assetHandler.getDrawableFromTexturePath("items/" + itemStack.item.name + ".png"));
        }
    }

    // Helper method to load the player's storage into inventory slots of type PlayerStorage
    public void setPlayerStorageSlots() {
        List<ItemStack> stacks = Game.getPlayer().getStorage();
        for (int i = 0; i < stacks.size(); i++) {
            Slot slot = slots.get(i);
            ItemStack itemStack = stacks.get(i);
            if (slot.slotType == SlotType.PlayerStorage) {
                setItem(itemStack, i);
            }
        }
    }

    public Table getButtonTable() { return buttonTable; }
    public Table getImageTable() { return imageTable; }
    public ItemStack getHoverItemStack() { return hoverItemStack; }
}
