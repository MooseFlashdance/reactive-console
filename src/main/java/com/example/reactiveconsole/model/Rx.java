package com.example.reactiveconsole.model;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
public class Rx implements Cloneable {
    private final String id;
    private Integer rxNbr;
    private String product;
    private Integer barcode;
    private final String patient;
    private Boolean isIMZ;
    private Boolean isERx;
    private String version;

    public Rx(Boolean isIMZ, Boolean isERx, String product) {
        this.isIMZ = isIMZ;
        this.isERx = isERx;
        this.product = product;
        this.version = UUID.randomUUID().toString();
        this.id = UUID.randomUUID().toString();
        this.patient = firstNames.get(new Random().nextInt(firstNames.size())) +
                " " + lastNames.get(new Random().nextInt(lastNames.size()));
    }

    public Rx clone() throws CloneNotSupportedException {
        return (Rx) super.clone();
    }

    private static final List<String> firstNames =
            Arrays.asList("Slab", "Bridge", "Punt", "Butch", "Hold", "Splint",
                    "Flint", "Bolt", "Thick", "Blast", "Buff", "Crunch", "Fist",
                    "Stump", "Smash", "Punch", "Buck", "Stump", "Dirk", "Rip", "Slate",
                    "Crud", "Brick", "Rip", "Punch", "Gristle", "Slate", "Buff", "Bob",
                    "Blast", "Crunch", "Slab", "Lump", "Touch", "Beef", "Big", "Smoke",
                    "Beat", "Hack", "Roll");

    private static final List<String> lastNames =
            Arrays.asList("Bulkhead", "Largemeat", "Speedchunk", "Deadlift", "Bigflank",
                    "Chesthair", "Ironstag", "Vanderhuge", "McRunfast", "Hardcheese",
                    "Drinklots", "Slamchest", "Rockbone", "Beefknob", "Lampjaw", "Rockgroin",
                    "Plankchest", "Junkman", "Hardpec", "Steakface", "Slabrock", "Bonemeal",
                    "Hardmeat", "Slagcheek", "Sideiron", "McThornbody", "Fistcrunch", "Hardback",
                    "Johnson", "Thickneck", "Buttsteak", "Squatthrust", "Beefbroth", "Rustrod",
                    "Blastbody", "McLargehuge", "Manmuscle", "Punchmeat", "Blowfist", "Fizzlebeef");

}
