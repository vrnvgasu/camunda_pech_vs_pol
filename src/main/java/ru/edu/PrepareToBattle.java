package ru.edu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PrepareToBattle implements JavaDelegate {

  @Value("${maxWarriors}")
  private int maxWarriors;

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    // получить данные из процесса
    int warriors = (int) delegateExecution.getVariable("warriors");
    int enemyWarriors = (int) (Math.random() * 100);

    maxWarriors = maxWarriors == 0 ? 100 : maxWarriors;

    if (warriors < 1 || warriors > maxWarriors) {
      // код ошибки из события
       throw new BpmnError("warriorError");
    }

    // Создадим коллекцию длины maxWarriors и все значения true
    List<Boolean> army = new ArrayList<>(Collections.nCopies(warriors, true));

    System.out.println("Prepare to battle! Enemy army = " + enemyWarriors + " vs our army: " + warriors);
    delegateExecution.setVariable("army", army);
    delegateExecution.setVariable("enemyWarriors", enemyWarriors);
  }

}
