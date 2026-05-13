import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Zak> vsichniZaci = new ArrayList<>();

        try {
            // Standardní načtení (přesně podle image_1d9b14.png)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("skola.xml"));
            doc.getDocumentElement().normalize();

            NodeList tridy = doc.getElementsByTagName("trida");
            for (int i = 0; i < tridy.getLength(); i++) {
                Element tridaEl = (Element) tridy.item(i);
                String nazevTridy = tridaEl.getAttribute("nazev");

                NodeList zaci = tridaEl.getElementsByTagName("zak");
                for (int j = 0; j < zaci.getLength(); j++) {
                    Element zakEl = (Element) zaci.item(j);
                    String jmeno = zakEl.getElementsByTagName("jmeno").item(0).getTextContent();

                    List<Predmet> predmetyZaka = new ArrayList<>();
                    NodeList predmetyNodes = zakEl.getElementsByTagName("predmet");
                    for (int k = 0; k < predmetyNodes.getLength(); k++) {
                        Element pEl = (Element) predmetyNodes.item(k);
                        String nazevP = pEl.getElementsByTagName("nazev").item(0).getTextContent();
                        int znamka = Integer.parseInt(pEl.getElementsByTagName("znamka").item(0).getTextContent());
                        predmetyZaka.add(new Predmet(nazevP, znamka));
                    }
                    vsichniZaci.add(new Zak(jmeno, nazevTridy, predmetyZaka));
                }
            }

            // Úkol 3: Výpis
            System.out.println("=== Průměry žáků ===");
            vsichniZaci.forEach(System.out::println);

            // Úkol 4: Hledání nejlepšího žáka (Klasický algoritmus na minimum)
            if (!vsichniZaci.isEmpty()) {
                Zak nejlepsi = vsichniZaci.get(0);
                for (int i = 1; i < vsichniZaci.size(); i++) {
                    if (vsichniZaci.get(i).getPrumer() < nejlepsi.getPrumer()) {
                        nejlepsi = vsichniZaci.get(i);
                    }
                }
                System.out.println("\nNejlepší žák: " + nejlepsi.getJmeno() + " (Průměr: " + nejlepsi.getPrumer() + ")");
            }

            // Úkol 5: Žáci s pětkou (Pomocí boolean flagu)
            System.out.println("\n=== Žáci s alespoň jednou pětkou ===");
            boolean naselSeNekdo = false;
            for (int i = 0; i < vsichniZaci.size(); i++) {
                Zak z = vsichniZaci.get(i);
                List<Predmet> pZaka = z.getPredmety();

                for (int j = 0; j < pZaka.size(); j++) {
                    if (pZaka.get(j).getZnamka() == 5) {
                        System.out.println(z.getJmeno() + " z třídy " + z.getNazevTridy());
                        naselSeNekdo = true;
                        break; // Našel pětku, dál u tohoto žáka hledat nemusím
                    }
                }
            }
            if (!naselSeNekdo) {
                System.out.println("Nikdo nema petku.");
            }

            // Úkol 6: Statistika předmětu (Vstup z klávesnice)
            Scanner sc = new Scanner(System.in);
            System.out.print("\nZadejte název předmětu: ");
            String hledany = sc.nextLine();

            double suma = 0;
            int pocet = 0;

            for (int i = 0; i < vsichniZaci.size(); i++) {
                List<Predmet> pZaka = vsichniZaci.get(i).getPredmety();
                for (int j = 0; j < pZaka.size(); j++) {
                    if (pZaka.get(j).getNazev().equalsIgnoreCase(hledany)) {
                        suma += pZaka.get(j).getZnamka();
                        pocet++;
                    }
                }
            }

            if (pocet == 0) {
                throw new PredmetNotFoundException("Předmět nenalezen!");
            }
            System.out.println("Průměr z " + hledany + " je: " + (suma / pocet));

        } catch (Exception e) {
            System.out.println("Chyba: " + e.getMessage());
        }
    }
}