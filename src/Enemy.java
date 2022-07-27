public abstract class Enemy extends Unit {
    //fields
    private DeathCallback dcb;
    private final int experience_value;

    //constructors
    public Enemy(String name, Integer health_pool, Integer attack_points, Integer defence_points, Integer experience_value, Character tile) {
        super(tile, name, health_pool, attack_points, defence_points);
        this.experience_value = experience_value;
    }

    public void init(DeathCallback edc, MessageCallback mcb) {
        this.dcb = edc;
        super.messageCallback = mcb;

    }


    public void accept(Unit unit) {
        unit.visit(this);
    }

    public void onDeath() {
        dcb.onDeath(); // remove from board
        setHealthAmount(0);
        this.send(new Message(this.getName() + " died"));
    }

    public void specialAbility(Player player) {
        player.castAbility(this);
    }

    public abstract Position move(Player player);

    public Integer getExperience_value() {
        return experience_value;
    }

    public abstract void gameTick(Player player);

    public void setExperience(Unit unit) {
        //Enemy cannot get EXP do nothing
    }

    public void levelUp() {
        //DO nothing
    }

    public void visit(Enemy enemy) {
        //do nothing
    }

    public void acceptEXP(Player player) {
        player.setExperience(player.getExperience() + this.getExperience_value()); // give the player the exp value
        player.messageCallback.send(new Message(String.format(player.getName() + " gained %d experience", this.getExperience_value() )));

    }

}
