package com.fakeplayer.core;

import java.util.Random;

public class NameGenerator {
    private static final String[] FIRST_NAMES = {
            "Alex", "Steve", "John", "Mike", "David", "Chris", "James", "Robert", "Michael", "William",
            "Richard", "Joseph", "Thomas", "Charles", "Daniel", "Matthew", "Anthony", "Mark", "Donald", "Steven",
            "Paul", "Andrew", "Joshua", "Kenneth", "Kevin", "Brian", "George", "Edward", "Ronald", "Timothy",
            "Jason", "Jeffrey", "Ryan", "Jacob", "Gary", "Nicholas", "Eric", "Jonathan", "Stephen", "Larry",
            "Justin", "Scott", "Brandon", "Benjamin", "Samuel", "Raymond", "Gregory", "Alexander", "Patrick", "Jack",
            "Dennis", "Jerry", "Tyler", "Aaron", "Jose", "Adam", "Henry", "Douglas", "Peter", "Zachary"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
            "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
            "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
            "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Peterson", "Phillips", "Campbell",
            "Parker", "Evans", "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales", "Murphy", "Cook",
            "Rogers", "Gutierrez", "Ortiz", "Morgan", "Peterson", "Cooper", "Peterson", "Reed", "Bell", "Gomez"
    };

    private static final String[] PREFIXES = {
            "Pro", "Super", "Ultra", "Mega", "Epic", "Cyber", "Shadow", "Dark", "Light", "Swift",
            "Rapid", "Silent", "Mystic", "Cosmic", "Quantum", "Nexus", "Apex", "Titan", "Phoenix", "Dragon"
    };

    private static final String[] SUFFIXES = {
            "Player", "Gamer", "Master", "Knight", "Warrior", "Ninja", "Sage", "Lord", "King", "Queen",
            "Hunter", "Slayer", "Ranger", "Mage", "Rogue", "Paladin", "Monk", "Bard", "Cleric", "Druid"
    };

    private final Random random = new Random();

    public String generateRandomName() {
        int type = random.nextInt(3);
        
        switch (type) {
            case 0: // FirstName + LastName
                return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)] + 
                       LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            case 1: // Prefix + Suffix
                return PREFIXES[random.nextInt(PREFIXES.length)] + 
                       SUFFIXES[random.nextInt(SUFFIXES.length)];
            case 2: // FirstName + Number
                return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)] + 
                       random.nextInt(10000);
            default:
                return "Player" + random.nextInt(100000);
        }
    }
}

