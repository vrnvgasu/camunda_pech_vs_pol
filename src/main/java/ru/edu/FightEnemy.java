package ru.edu;

import java.util.ArrayList;
import java.util.Random;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class FightEnemy implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    // передали army в сервисном процессе PrepareToBattle
    ArrayList<Boolean> army = (ArrayList<Boolean>) delegateExecution.getVariable("army");
    int enemyWarriors = (int) delegateExecution.getVariable("enemyWarriors");

    // задержка на 2 сек, чтобы продемонстрировать обработку коллекции
    // в сервисной параллельной задаче более наглядно
    Thread.sleep(2000);

    if (new Random().nextBoolean()) {
      enemyWarriors--;
      System.out.println("Enemy warrior killed!");
    } else {
      System.out.println("Our warrior killed!");
      army.remove(army.size() - 1);
    }

    delegateExecution.setVariable("army", army);
    delegateExecution.setVariable("warriors", army.size());
    delegateExecution.setVariable("enemyWarriors", enemyWarriors);
  }

}
