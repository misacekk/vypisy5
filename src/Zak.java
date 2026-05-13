import java.util.List;

public class Zak {
    private String jmeno;
    private String nazevTridy;
    private List<Predmet> predmety;

    public Zak(String jmeno, String nazevTridy, List<Predmet> predmety) {
        this.jmeno = jmeno;
        this.nazevTridy = nazevTridy;
        this.predmety = predmety;
    }

    public String getJmeno() { return jmeno; }
    public String getNazevTridy() { return nazevTridy; }
    public List<Predmet> getPredmety() { return predmety; }

    public double getPrumer() {
        if (predmety.isEmpty()) {
            return 0.0;
        }

        double soucet = 0;
        for (int i = 0; i < predmety.size(); i++) {
            Predmet p = predmety.get(i);
            soucet = soucet + p.getZnamka();
        }

        return soucet / predmety.size();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - prumer: %.2f", nazevTridy, jmeno, getPrumer());
    }
}