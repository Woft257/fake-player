package com.fakeplayer.core;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NameGenerator {
    private static final String[] FIRST_NAMES = {
            "Alex","Steve","John","Mike","David","Chris","James","Robert","Michael","William",
            "Richard","Joseph","Thomas","Charles","Daniel","Matthew","Anthony","Mark","Donald","Steven",
            "Paul","Andrew","Joshua","Kenneth","Kevin","Brian","George","Edward","Ronald","Timothy",
            "Jason","Jeffrey","Ryan","Jacob","Gary","Nicholas","Eric","Jonathan","Stephen","Larry",
            "Justin","Scott","Brandon","Benjamin","Samuel","Raymond","Gregory","Alexander","Patrick","Jack",
            "Dennis","Jerry","Tyler","Aaron","Jose","Adam","Henry","Douglas","Peter","Zachary",
            // Thêm tên phổ biến hơn
            "Ethan","Noah","Liam","Mason","Logan","Oliver","Elijah","Lucas","Aiden","Jackson",
            "Sophia","Emma","Olivia","Ava","Mia","Isabella","Amelia","Harper","Evelyn","Abigail",
            "Minh","Anh","Khoa","Nam","Phuc","Huy","Khanh","Duy","Long","Bao",
            "Linh","Trang","Thu","Ngoc","My","Thao","Lan","Quynh","Chi","Nhi"
    };

    private static final String[] LAST_NAMES = {
            "Smith","Johnson","Williams","Brown","Jones","Garcia","Miller","Davis","Rodriguez","Martinez",
            "Hernandez","Lopez","Gonzalez","Wilson","Anderson","Thomas","Taylor","Moore","Jackson","Martin",
            "Lee","Perez","Thompson","White","Harris","Sanchez","Clark","Ramirez","Lewis","Robinson",
            "Walker","Young","Allen","King","Wright","Scott","Torres","Peterson","Phillips","Campbell",
            "Parker","Evans","Edwards","Collins","Reyes","Stewart","Morris","Morales","Murphy","Cook",
            "Rogers","Gutierrez","Ortiz","Morgan","Reed","Bell","Gomez","Nguyen","Tran","Le",
            "Pham","Hoang","Huynh","Phan","Vu","Vo","Dang","Bui","Do","Truong"
    };

    private static final String[] PREFIXES = {
            "Pro","Super","Ultra","Mega","Epic","Cyber","Shadow","Dark","Light","Swift",
            "Rapid","Silent","Mystic","Cosmic","Quantum","Nexus","Apex","Titan","Phoenix","Dragon",
            "Nova","Prime","Alpha","Omega","Hyper","Iron","Silver","Golden","Crimson","Aqua"
    };

    private static final String[] SUFFIXES = {
            "Player","Gamer","Master","Knight","Warrior","Ninja","Sage","Lord","King","Queen",
            "Hunter","Slayer","Ranger","Mage","Rogue","Paladin","Monk","Bard","Cleric","Druid",
            "Samurai","Assassin","Pirate","Sniper","Wizard","Archer","Viking","Sentinel","Guardian","Scout"
    };

    private final Random random = new Random();

    public String generateRandomName() {
        int type = random.nextInt(3);
        String raw;
        switch (type) {
            case 0:
                raw = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)] + LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                break;
            case 1:
                raw = PREFIXES[random.nextInt(PREFIXES.length)] + SUFFIXES[random.nextInt(SUFFIXES.length)];
                break;
            case 2:
                raw = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)] + random.nextInt(10000);
                break;
            default:
                raw = "Player" + random.nextInt(100000);
        }
        return normalizeToMcName(raw);
    }

    // Ensure 3..16 chars and only [A-Za-z0-9_]
    private String normalizeToMcName(String input) {
        if (input == null) input = "Player" + random.nextInt(100000);
        String sanitized = input.replaceAll("[^A-Za-z0-9_]", "");
        if (sanitized.length() > 16) {
            sanitized = sanitized.substring(0, 16);
        }
        if (sanitized.length() < 3) {
            sanitized = (sanitized + "Player").substring(0, Math.min(16, (sanitized + "Player").length()));
        }
        // Avoid starting with a number (rare client quirks with some plugins)
        if (sanitized.length() > 0 && Character.isDigit(sanitized.charAt(0))) {
            sanitized = "P" + sanitized.substring(0, Math.min(15, sanitized.length()));
        }
        return sanitized;
    }

    // Unique name (case-insensitive) against reserved set
    public String generateUniqueName(Set<String> reservedLower) {
        if (reservedLower == null) reservedLower = new HashSet<>();
        for (int i = 0; i < 200; i++) { // bounded attempts
            String candidate = generateRandomName();
            if (!reservedLower.contains(candidate.toLowerCase())) return candidate;
        }
        // fallback: base + numeric suffix while keeping <= 16
        String base = normalizeToMcName("Player" + random.nextInt(100000));
        int suffix = 1;
        String candidate = base;
        while (reservedLower.contains(candidate.toLowerCase())) {
            String suffixStr = String.valueOf(++suffix);
            int keep = Math.max(3, 16 - suffixStr.length());
            candidate = base.substring(0, Math.min(keep, base.length())) + suffixStr;
        }
        return candidate;
    }
}

