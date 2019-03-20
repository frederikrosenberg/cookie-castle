package dk.sdu.cookie.castle.common.data.Entityparts;


import dk.sdu.cookie.castle.common.data.Buff;
import dk.sdu.cookie.castle.common.data.Entity;
import dk.sdu.cookie.castle.common.data.GameData;

import static java.lang.Math.abs;
import static java.lang.StrictMath.sqrt;

public class MovingPart implements EntityPart {
    private float speed;
    private boolean left, right, up, down;

    public MovingPart(float maxSpeed) {
        this.speed = maxSpeed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        PositionPart positionPart = entity.getPart(PositionPart.class);
        InventoryPart inventoryPart = entity.getPart(InventoryPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float deltaX = 0;
        float deltaY = 0;
        float buffedSpeed = speed;
        if(inventoryPart != null) {
            for (ItemPart itemPart : inventoryPart.getItemParts()) {
                if(!itemPart.isWeapon()) {
                    BuffPart buffPart = itemPart.getBuff();
                    buffedSpeed += buffPart.getSpecificBuff(Buff.MOVEMENT_SPEED);
                }
            }
        }

        if (left) {
            deltaX -= buffedSpeed;
        }

        if (right) {
            deltaX += buffedSpeed;
        }

        if (up) {
            deltaY += buffedSpeed;
        }

        if (down) {
            deltaY -= buffedSpeed;
        }

        if((abs(deltaX) + abs(deltaY)) > buffedSpeed) {
            deltaX = (deltaX/ (float) sqrt((deltaX*deltaX) + (deltaY*deltaY))) * buffedSpeed;
            deltaY = (deltaY/ (float) sqrt((deltaX*deltaX) + (deltaY*deltaY))) * buffedSpeed;
            x += deltaX;
            y += deltaY;

        }   else {
            x += deltaX;
            y += deltaY;
        }

        positionPart.setPosition(x, y);

    }

}
