/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.credits;

import java.math.BigDecimal;

public class CreditsData {

    private final String player;
    private final BigDecimal credits;

    public CreditsData(String player, BigDecimal credits) {
        this.player = player;
        this.credits = credits;
    }

    public String getPlayer() {
        return player;
    }

    public BigDecimal getCredits() {
        return credits;
    }
}
