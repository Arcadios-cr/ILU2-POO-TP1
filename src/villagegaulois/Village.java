package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
    private Marche marche;


	public Village(String nom, int nbVillageoisMaximum,int nbEtals){
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
        marche = new Marche(nbEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
        int indiceEtal = marche.trouverEtalLibre();
        if (indiceEtal != -1) {
            marche.utiliserEtal(indiceEtal, vendeur, produit, nbProduit);
            return vendeur.getNom() + " s'installe à l'étal n°" + (indiceEtal + 1) + " pour vendre " + nbProduit + " " + produit + ".";
        } else {
            return "Il n'y a pas d'étal disponible pour vendre des " + produit + ".";
        }
    }

    public String rechercherVendeursProduit(String produit) {
        Etal[] etals = marche.trouverEtals(produit);
        if (etals.length > 0) {
            StringBuilder chaine = new StringBuilder("Les vendeurs qui proposent des " + produit + " sont :\n");
            for (Etal etal : etals) {
                chaine.append("- ").append(etal.getVendeur().getNom()).append("\n");
            }
            return chaine.toString();
        } else {
            return "Il n'y a pas de vendeur qui propose des " + produit + " au marché.";
        }
    }

    public Etal rechercherEtal(Gaulois vendeur) {
        return marche.trouverVendeur(vendeur);
    }

    public String partirVendeur(Gaulois vendeur) {
        Etal etal = marche.trouverVendeur(vendeur);
        if (etal != null) {
            String result = etal.libererEtal();
            return result + "Le vendeur " + vendeur.getNom() + " quitte le marché.";
        } else {
            return "Le vendeur " + vendeur.getNom() + " n'est pas installé à un étal.";
        }
    }

    public String afficherMarche() {
        return marche.afficherMarche();
    }
	
	
	private static class Marche {
        private Etal[] etals;

        public Marche(int nombreEtals) {
            etals = new Etal[nombreEtals];
            for (int i = 0; i < nombreEtals; i++) {
                etals[i] = new Etal();
            }
        }

        public void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
            if (indiceEtal >= 0 && indiceEtal < etals.length) {
                etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
            } else {
                System.out.println("Indice d'étal non valide.");
            }
        }

        public int trouverEtalLibre() {
            for (int i = 0; i < etals.length; i++) {
                if (!etals[i].isEtalOccupe()) {
                    return i;
                }
            }
            return -1;
        }

        public Etal[] trouverEtals(String produit) {
            int count = 0;
            for (Etal etal : etals) {
                if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
                    count++;
                }
            }

            Etal[] result = new Etal[count];
            count = 0;

            for (Etal etal : etals) {
                if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
                    result[count++] = etal;
                }
            }

            return result;
        }

        public Etal trouverVendeur(Gaulois gaulois) {
            for (Etal etal : etals) {
                if (etal.isEtalOccupe() && etal.getVendeur().equals(gaulois)) {
                    return etal;
                }
            }
            return null;
        }

        public String afficherMarche() {
            StringBuilder chaine = new StringBuilder();

            for (Etal etal : etals) {
                if (etal.isEtalOccupe()) {
                    chaine.append(etal.afficherEtal());
                }
            }

            int nbEtalVide = 0;
            for (Etal etal : etals) {
                if (!etal.isEtalOccupe()) {
                    nbEtalVide++;
                }
            }

            if (nbEtalVide > 0) {
                chaine.append("Il reste " + nbEtalVide + " étals non utilisés dans le marché.\n");
            }

            return chaine.toString();
        }
    }
}
