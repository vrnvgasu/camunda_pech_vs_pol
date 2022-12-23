package ru.edu;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class PrepareToBattle implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    // получить данные из процесса
    int warriors = (int) delegateExecution.getVariable("warriors");

    int enemyWarriors = (int) (Math.random() * 100);
    String battleStatus = "undefined";
    boolean isWin = false;

    if (warriors < 1 || warriors > 100) {
      // код ошибки из события
       throw new BpmnError("warriorError");
    }

    if (warriors - enemyWarriors > 0) {
      isWin = true;
      battleStatus = "Victory";
    } else {
      battleStatus = "Fail";
    }

    //присваиваем данные переменным процесса
    delegateExecution.setVariable("enemyWarriors", enemyWarriors);
    delegateExecution.setVariable("battleStatus", battleStatus);
    delegateExecution.setVariable("isWin", isWin);
  }

}
