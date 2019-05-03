package dk.sdu.cookie.castle.enemy;

import dk.sdu.cookie.castle.common.assets.Asset;
import dk.sdu.cookie.castle.common.assets.AssetType;
import dk.sdu.cookie.castle.common.assets.FileType;
import dk.sdu.cookie.castle.common.data.Entity;
import dk.sdu.cookie.castle.common.data.EntityType;
import dk.sdu.cookie.castle.common.data.Entityparts.*;
import dk.sdu.cookie.castle.common.data.GameData;
import dk.sdu.cookie.castle.common.data.World;
import dk.sdu.cookie.castle.common.enemy.Enemy;
import dk.sdu.cookie.castle.common.services.IGamePluginService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnemyPlugin implements IGamePluginService {
    private Entity enemy;
    private Map<String, Asset> assets = new ConcurrentHashMap<>();

    @Override
    public void start(GameData gameData, World world) {
        initializeAssets();
        gameData.addAssets(assets);
        enemy = createEnemy(gameData);
        world.addEntity(enemy);

    }

    /**
     * Creates an enemy ship, with the different parts and parameters needed.
     *
     * @param gameData The gamedata
     * @return The enemy created
     */
    private Entity createEnemy(GameData gameData) {
        float[] shapeX = new float[3];
        float[] shapeY = new float[3];

        float maxSpeed = 100;
        float x = gameData.getDisplayWidth() / 4;
        float y = gameData.getDisplayHeight() / 4;
        float radians = 3.1415f / 2;

        Entity enemyShip = new Enemy();
        enemyShip.initializeAssets(assets);
        enemyShip.setRadius(8);
        enemyShip.add(new AIMovingPart(maxSpeed));
        enemyShip.add(new PositionPart(x, y, radians));
        enemyShip.add(new LifePart(1, 1, 1, 1));
        enemyShip.add(new CollisionPart());
        enemyShip.setEntityType(EntityType.ENEMY);

        WeaponPart weaponPart = new WeaponPart(500f, 2f, 3f);
        enemyShip.add(weaponPart);

        enemyShip.setShapeY(shapeY);
        enemyShip.setShapeX(shapeX);
        enemyShip.add(new ShootingPart(weaponPart.getAttackSpeed()));

        enemyShip.setCurrentTexture("cookie");


        return enemyShip;
    }

    private void initializeAssets() {
        Asset enemyImage = new Asset("cookie", AssetType.TEXTURE, FileType.PNG);
        assets.put(enemyImage.getId(), enemyImage);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Enemy.class)) {
            world.removeEntity(entity);
        }
    }
}
