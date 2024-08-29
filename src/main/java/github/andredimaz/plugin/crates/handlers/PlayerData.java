package github.andredimaz.plugin.crates.handlers;

public class PlayerData {

    private int limite;

    public PlayerData(int limite) {
        this.limite = limite;
    }

    public int getLimite() {
        return limite;
    }

    public int setLimite(int limite) {
        this.limite = limite;
        return limite;
    }
}
