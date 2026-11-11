package edu.hitsz.enums;

public enum MusicEnums {
    BGM("bgm", "/videos/deltarune-local/field-of-hopes.ogg", "/videos/bgm.wav"),
    BGM_BOSS("bossBgm", "/videos/jevil_world_revolving.wav", "/videos/bgm_boss.wav"),
    BOMB_EXPLOSION("bombExplosion", "/videos/bomb_explosion.wav"),
    BULLET_HIT("bulletHit", "/videos/deltarune-local/yellow-shot.wav", "/videos/bullet_hit.wav"),
    HERO_HURT("heroHurt", "/videos/deltarune-local/hurt.wav", "/videos/bullet_hit.wav"),
    GAME_OVER("gameOver", "/videos/game_over.wav"),
    GET_SUPPLY("getSupply", "/videos/get_supply.wav");


    private final String name;
    private final String path;
    private final String fallbackPath;

    MusicEnums(String name, String path) {
        this(name, path, path);
    }

    MusicEnums(String name, String path, String fallbackPath) {
        this.name = name;
        this.path = path;
        this.fallbackPath = fallbackPath;
    }

    public String getPath() {
        return MusicEnums.class.getResource(path) == null ? fallbackPath : path;
    }

    public String getName() {
        return this.name;
    }
}
