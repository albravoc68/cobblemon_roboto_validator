package cl.roboto.validator.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonConfig {

    private List<String> BANNED_POKEMON;
    private List<String> BANNED_SPECIAL_FORMS;
    private List<String> BANNED_MOVES;
    private List<String> BANNED_ITEMS;
    private List<String> BANNED_ABILITIES;
    private Boolean OH_KO_CLAUSE;
    private Boolean EVASION_CLAUSE;
    private Boolean ITEM_CLAUSE;
    private Boolean SPECIES_CLAUSE;

    public JsonConfig() {

    }

    public static JsonConfig initializeDefault() {
        JsonConfig config = new JsonConfig();
        config.setBANNED_POKEMON(getDefaultBannedPokemon());
        config.setBANNED_SPECIAL_FORMS(getDefaultBannedSpecialForms());
        config.setBANNED_MOVES(getDefaultBannedMoves());
        config.setBANNED_ITEMS(getDefaultBannedItems());
        config.setBANNED_ABILITIES(getDefaultBannedAbilities());
        config.setOH_KO_CLAUSE(getDefaultOHKOClause());
        config.setEVASION_CLAUSE(getDefaultEvasionClause());
        config.setITEM_CLAUSE(getDefaultItemClause());
        config.setSPECIES_CLAUSE(getDefaultSpeciesClause());
        return config;
    }

    private static List<String> getDefaultBannedPokemon() {
        return Arrays.asList(
                "Mewtwo",
                "Ditto",
                "Lugia",
                "Ho-Oh",
                "Groudon",
                "Kyogre",
                "Rayquaza",
                "Deoxys",
                "Dialga",
                "Palkia",
                "Giratina",
                "Arceus",
                "Darkrai",
                "Reshiram",
                "Zekrom",
                "Landorus",
                "Genesect",
                "Xerneas",
                "Yveltal",
                "Zygarde",
                "Solgaleo",
                "Lunala",
                "Magearna",
                "Marshadow",
                "Pheromosa",
                "Naganadel",
                "Eternatus",
                "Zacian",
                "Urshifu",
                "Spectrier",
                "Dracovish",
                "Sneasler",
                "Miraidon",
                "Koraidon",
                "Annihilape",
                "Baxcalibur",
                "Espathra",
                "Chi-Yu",
                "Chien-Pao",
                "Flutter Mane",
                "Iron Bundle",
                "Roaring Moon",
                "Walking Wake",
                "Gouging Fire"
        );
    }

    private static List<String> getDefaultBannedSpecialForms() {
        return Arrays.asList(
                "Shaymin:-:Sky",
                "Kyurem:-:Black",
                "Kyurem:-:White",
                "Darmanitan:-:Galar",
                "Necrozma:-:Dawn-Wings",
                "Necrozma:-:Dusk-Mane",
                "Necrozma:-:Ultra",
                "Zacian:-:Crowned",
                "Zamazenta:-:Crowned",
                "Ursaluna:-:Bloodmoon",
                "Calyrex:-:Ice",
                "Calyrex:-:Shadow",
                "Ogerpon:-:Hearthflame"
        );
    }

    private static List<String> getDefaultBannedMoves() {
        return Arrays.asList(
                "batonpass",
                "Assist",
                "LastRespects",
                "ShedTail"
        );
    }

    private static List<String> getDefaultBannedItems() {
        return Arrays.asList(
                "Alakazite",
                "Blastoisinite",
                "Blazikenite",
                "Gengarite",
                "Kangaskhanite",
                "Lucarionite",
                "Metagrossite",
                "Salamencite",
                "Kings Rock",
                "Quick Claw",
                "Razor Fang"
        );
    }

    private static List<String> getDefaultBannedAbilities() {
        return Arrays.asList(
                "ArenaTrap",
                "Moody",
                "PowerConstruct",
                "ShadowTag"
        );
    }

    private static boolean getDefaultOHKOClause() {
        return true;
    }

    private static boolean getDefaultEvasionClause() {
        return true;
    }

    private static boolean getDefaultItemClause() {
        return false;
    }

    private static boolean getDefaultSpeciesClause() {
        return true;
    }

    public static List<String> getEvasionMoves() {
        return Arrays.asList(
                "DoubleTeam",
                "Minimize"
        );
    }

    public static List<String> getOHKOMoves() {
        return Arrays.asList(
                "Fissure",
                "HornDrill",
                "Guillotine",
                "SheerCold"
        );
    }

    public List<String> getBANNED_POKEMON() {
        return BANNED_POKEMON != null ? BANNED_POKEMON : new ArrayList<>();
    }

    public void setBANNED_POKEMON(List<String> BANNED_POKEMON) {
        this.BANNED_POKEMON = BANNED_POKEMON;
    }

    public List<String> getBANNED_SPECIAL_FORMS() {
        return BANNED_SPECIAL_FORMS != null ? BANNED_SPECIAL_FORMS : new ArrayList<>();
    }

    public void setBANNED_SPECIAL_FORMS(List<String> BANNED_SPECIAL_FORMS) {
        this.BANNED_SPECIAL_FORMS = BANNED_SPECIAL_FORMS;
    }

    public List<String> getBANNED_MOVES() {
        return BANNED_MOVES != null ? BANNED_MOVES : new ArrayList<>();
    }

    public void setBANNED_MOVES(List<String> BANNED_MOVES) {
        this.BANNED_MOVES = BANNED_MOVES;
    }

    public List<String> getBANNED_ITEMS() {
        return BANNED_ITEMS != null ? BANNED_ITEMS : new ArrayList<>();
    }

    public void setBANNED_ITEMS(List<String> BANNED_ITEMS) {
        this.BANNED_ITEMS = BANNED_ITEMS;
    }

    public List<String> getBANNED_ABILITIES() {
        return BANNED_ABILITIES != null ? BANNED_ABILITIES : new ArrayList<>();
    }

    public void setBANNED_ABILITIES(List<String> BANNED_ABILITIES) {
        this.BANNED_ABILITIES = BANNED_ABILITIES;
    }

    public Boolean getOH_KO_CLAUSE() {
        return OH_KO_CLAUSE != null && OH_KO_CLAUSE;
    }

    public void setOH_KO_CLAUSE(Boolean OH_KO_CLAUSE) {
        this.OH_KO_CLAUSE = OH_KO_CLAUSE;
    }

    public Boolean getEVASION_CLAUSE() {
        return EVASION_CLAUSE != null && EVASION_CLAUSE;
    }

    public void setEVASION_CLAUSE(Boolean EVASION_CLAUSE) {
        this.EVASION_CLAUSE = EVASION_CLAUSE;
    }

    public Boolean getITEM_CLAUSE() {
        return ITEM_CLAUSE != null && ITEM_CLAUSE;
    }

    public void setITEM_CLAUSE(Boolean ITEM_CLAUSE) {
        this.ITEM_CLAUSE = ITEM_CLAUSE;
    }

    public Boolean getSPECIES_CLAUSE() {
        return SPECIES_CLAUSE != null && SPECIES_CLAUSE;
    }

    public void setSPECIES_CLAUSE(Boolean SPECIES_CLAUSE) {
        this.SPECIES_CLAUSE = SPECIES_CLAUSE;
    }

}
