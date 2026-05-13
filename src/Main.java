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

//Příklad 5
//Škola – hluboké vnořování, atributy, statistiky
//
//Struktura XML
//Soubor skola.xml obsahuje třídy s žáky, každý žák má seznam předmětů se známkami. Třídy a
//škola jsou identifikovány XML atributy (nazev).
//&lt;skola nazev=&quot;Gymnazium Praha&quot;&gt;
//&lt;trida nazev=&quot;3A&quot;&gt;
//&lt;zak&gt;
//&lt;jmeno&gt;Petr Novak&lt;/jmeno&gt;
//&lt;predmety&gt;
//&lt;predmet&gt;
//&lt;nazev&gt;Matematika&lt;/nazev&gt;
//&lt;znamka&gt;1&lt;/znamka&gt;
//&lt;/predmet&gt;
//&lt;!-- další předměty... --&gt;
//&lt;/predmety&gt;
//&lt;/zak&gt;
//&lt;!-- další žáci... --&gt;
//&lt;/trida&gt;
//&lt;!-- další třídy... --&gt;
//&lt;/skola&gt;
//
//Úkoly
//1. Třídy Predmet a Zak: Vytvořte třídy Predmet (nazev, znamka) a Zak (jmeno, nazevTridy,
//List&lt;Predmet&gt;). Implementujte konstruktory, gettery a toString().
//2. Načtení dat: Načtěte soubor skola.xml. Pro každou třídu (element &lt;trida&gt;) přečtěte XML
//atribut nazev pomocí getAttribute(&quot;nazev&quot;). Pro každého žáka načtěte jeho předměty a
//uložte vše do List&lt;Zak&gt;.
//3. Průměry žáků: Pro každého žáka vypočítejte průměr jeho známek a vypište:
//[3A] Petr Novak – prumer: 1.33
//4. Nejlepší žák: Najděte nejlepšího žáka celé školy (nejnižší průměr). Vypište jeho jméno,
//třídu a průměr.
//5. Žáci s pětkou: Vypište všechny žáky, kteří mají alespoň jednu pětku. Pokud žádný takový
//žák neexistuje, vypište &quot;Nikdo nema petku.&quot;.
//6. Statistika předmětu: Z klávesnice načtěte název předmětu (např. Matematika).
//Vypočítejte a vypište průměrnou známku z tohoto předmětu pro všechny žáky školy. Pokud
//předmět v datech není, vyhoďte vlastní výjimku PredmetNotFoundException.
//
//Nápověda – čtení XML atributů
/// / Čtení atributu elementu:
//Element trida = (Element) tridyList.item(i);
//String nazevTridy = trida.getAttribute(&quot;nazev&quot;);
//
//Procvičení: Práce se soubory XML v Javě • Příklad 5
//// Pozor: getElementsByTagName prohledává celý podstrom,
//// takže autor.getElementsByTagName(&quot;nazev&quot;) vrátí jen
//// elementy &lt;nazev&gt; uvnitř daného autora/třídy.