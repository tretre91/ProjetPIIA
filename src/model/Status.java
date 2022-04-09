package model;

/**
 * Enumeration des différents statuts que peuvent avoir les utilisateurs
 */
public enum Status {
    ADMIN(0), PARENT(1), TEEN(2), CHILD(3);

    private final int value;

    private Status(int value) {
        this.value = value;
    }

    /**
     * <p>
     * Récupère le niveau de privilège (entier entre 0 et 3) associé à un statut
     * </p>
     * <ul>
     * <li>0 pour l'admin</li>
     * <li>1 pour les parents</li>
     * <li>2 pour les adolescents</li>
     * <li>3 pour les enfants</li>
     * </ul>
     * 
     * @return Le niveau de privilège associé à ce statut
     */
    public int getValue() {
        return value;
    }

    /**
     * Créée un statut à partir du niveau de privilèges (entier)
     * 
     * @param value
     *            Le niveau de privilège (entier entre 0 et 3)
     * @return Le statut correspondant
     */
    static public Status fromInt(int value) {
        switch (value) {
            case 0:
                return ADMIN;
            case 1:
                return PARENT;
            case 2:
                return TEEN;
            case 3:
                return CHILD;
            default:
                return CHILD; // TODO : gestion d'erreur ?
        }
    }

    /**
     * Inqique si le statut à une priorité moindre qu'un autre
     * 
     * @param other
     *            Le statut à comparer
     * @return true si ce statut a moins de droit que other
     */
    public boolean lessThan(Status other) {
        return this.value > other.getValue();
    }
}
