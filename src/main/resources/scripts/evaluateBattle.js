var warriors = execution.getVariable("warriors");
var enemyWarriors = execution.getVariable("enemyWarriors");

if (warriors > enemyWarriors) {
    execution.setVariable("isWin", true);
    execution.setVariable("battleStatus", "WIN!");
} else {
    execution.setVariable("isWin", false);
    execution.setVariable("battleStatus", "FAIL!");
}
