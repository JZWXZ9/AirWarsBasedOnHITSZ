package edu.hitsz.props;

import java.util.Random;

public class PropsFactory {
    public static Random random = new Random();

    public static AbstractProps createProps(String name, int locationX, int locationY) {
        return switch (name) {
            case "BloodProps" -> {
                int increasementOfHp = 50 + random.nextInt(-10, 10);
                yield new BloodProps(locationX, locationY, increasementOfHp);
            }
            case "BulletProps" -> new BulletProps(locationX, locationY);
            case "BulletPlusProps" -> new BulletPlusProps(locationX, locationY);
            case "BombProps" -> new BombProps(locationX, locationY);
            case "FreezeProps" -> new FreezeProps(locationX, locationY);
            default -> {
                System.out.println(name + " Doesn't exist!");
                yield null;
            }
        };
    }
}

