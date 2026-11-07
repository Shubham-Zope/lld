package com.shubham.snakeladder.rules;

import com.shubham.snakeladder.model.Player;

import java.util.List;

public class RuleEngine {

    private final List<GameRule> rules;

    private final MovementRule movementRule;

    public RuleEngine(List<GameRule> rules) {

        this.rules = List.copyOf(rules);

        List<MovementRule> movementRules = rules.stream()
                .filter(rule -> rule instanceof MovementRule)
                .map(rule -> (MovementRule) rule)
                .toList();

        if (movementRules.size() != 1) {
            throw new IllegalArgumentException(
                    "Exactly one movement rule is required"
            );
        }

        this.movementRule = movementRules.getFirst();
    }

    public boolean isTurnCancelled(Player player, int roll) {

        return rules.stream()
                .anyMatch(rule ->
                        rule.isTurnCancelled(player, roll)
                );
    }

    public boolean grantsExtraTurn(Player player, int roll) {

        return rules.stream()
                .anyMatch(rule ->
                        rule.grantsExtraTurn(player, roll)
                );
    }

    public int calculatePosition(
            int currentPosition,
            int roll,
            int boardSize
    ) {

        return movementRule.calculatePosition(
                currentPosition,
                roll,
                boardSize
        );
    }

    public void afterTurn(Player player, int roll) {

        rules.forEach(rule -> rule.afterTurn(player, roll));
    }

    public void afterCancelledTurn(Player player) {

        rules.forEach(rule -> rule.afterCancelledTurn(player));
    }
}