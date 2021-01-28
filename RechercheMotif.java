import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class RechercheMotif {

    // Recherche du motif {motif} dans la chaîne {texte}
    static void recherche(String texte, String motif) {
        // indice de parcours du texte
        int i;
        // indice de parcours du motif
        int j;

        i = 0;
        j = 0;
        while ((i <= texte.length() - motif.length())) {

            // recherche du motif à l'indice i
            j = 0;
            while ((j < motif.length()) && (texte.charAt(i + j) == motif.charAt(j))) {
                // invariant : texte[i..i+j-1] = motif[0..j-1]
                j = j + 1;
            }

            if (j == motif.length()) {
                System.out.println(i);
            }

            i = i + 1;
        }
    }

    static int getUnitCode(String s, int i) {
        // System.out.println("Hash de " + s.charAt(i) + " = " + ((int)s.charAt(i)));
        return (int)s.charAt(i);
    }

    // Renvoie le hashcode de la sous-chaîne s[debut..fin]
    static int hash(String s, int debut, int fin) {
        // debut est 2 et fin est 4 : b a (a a b) a donne int(a)+int(a)+int(b) = 4
        int h = 0;
        for (int i = debut; i < fin; i++) {
            h += getUnitCode(s, i);
        }
        return h;
    }

    // Renvoie le hashcode de la sous-chaîne s[debut..fin], à l'aide
    // de la valeur hash = hashcode(s,debut-1,fin-1)
    static int updateHash(String s, int hash, int debut, int fin) {
        if(debut > 0)
            hash -= getUnitCode(s, debut-1);
        hash += getUnitCode(s, fin);
        return hash;
        //return hash + (int)(s.charAt(fin) - s.charAt(debut-1));
    }

    static void rechercheKR(String texte, String motif) {
        // indice de parcours du texte
        int i;
        // indice de parcours du motif
        int j;
        // valeurs de hachage du motif et des sous-chaînes
        int hmotif, hcourant;

        hmotif = hash(motif, 0, motif.length());
        i = 0;
        hcourant = 0;
        
        while (i < texte.length() - motif.length() +1) {
            // hachage de la sous-chaîne texte[i..i+m-1]
            hcourant = (i==0)? 
                hash(texte, i, i + motif.length()) :
                updateHash(texte, hcourant, i, i + motif.length()-1);
                
            if (hcourant == hmotif) {
                // recherche du motif à l'indice i
                j = 0;
                while ((j < motif.length()) && (texte.charAt(i + j) == motif.charAt(j))) {
                    // invariant : texte[i..i+j-1] = motif[0..j-1]
                    j++;
                }

                if (j == motif.length()) {
					System.out.println(i);
                }
            }
            i++;
        }
    }

    public static void main(String args[]) {
        String texte;
        String motif;

        FileInputStream input;
        BufferedReader reader;

        for (int i = 0; i < args.length; i++) {
            try {
                // Ouverture du fichier passé en argument
                input = new FileInputStream(args[i]);
                reader = new BufferedReader(new InputStreamReader(input));

                // Lecture de la chaîne
                texte = reader.readLine();
                // Lecture du motif
                motif = reader.readLine();

                // date de début
                long startTime = System.nanoTime();

                //recherche(texte, motif);
                rechercheKR(texte, motif);

                // date de fin pour le calcul du temps écoulé
                long endTime = System.nanoTime();

                // Impression de la longueur du texte et du temps d'exécution
                System.out.println(args[i].replace("tests/wc", "") + "\t" + ((endTime - startTime) / 1.0E9));

            } catch (FileNotFoundException e) {
                System.err.println("Erreur lors de l'ouverture du fichier " + args[i]);
            } catch (IOException e) {
                System.err.println("Erreur de lecture dans le fichier");
            }
        }
    }
}