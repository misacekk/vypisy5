public class Predmet {
    private String nazev;
    private int znamka;

    public Predmet(String nazev, int znamka) {
        this.nazev = nazev;
        this.znamka = znamka;
    }
    public String getNazev() { return nazev; }
    public int getZnamka() { return znamka; }

    @Override
    public String toString() {
        return nazev + " " + znamka;
    }
}