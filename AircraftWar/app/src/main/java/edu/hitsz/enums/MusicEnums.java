package edu.hitsz.enums;

public enum MusicEnums {
    BGM("bgm", "/videos/bgm.wav"),
    BGM_BOSS("bossBgm", "/videos/jevil_world_revolving.wav"),
    BOMB_EXPLOSION("bombExplosion", "/videos/bomb_explosion.wav"),
    BULLET_HIT("bulletHit", "/videos/bullet_hit.wav"),
    GAME_OVER("gameOver", "/videos/game_over.wav"),
    GET_SUPPLY("getSupply", "/videos/get_supply.wav");


    private final String name;
    private final String path;
    MusicEnums(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getPath() {
        if (this == BGM_BOSS && MusicEnums.class.getResource(path) == null) {
            return "/videos/bgm_boss.wav";
        }
        return this.path;
    }

    public String getName() {
        return this.name;
    }
}
