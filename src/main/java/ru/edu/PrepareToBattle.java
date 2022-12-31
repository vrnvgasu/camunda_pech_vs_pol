package ru.edu;

import static org.camunda.spin.Spin.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpRequest;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.spin.json.SpinJsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.edu.domain.Warrior;

@Component
public class PrepareToBattle implements JavaDelegate {

  @Value("${maxWarriors}")
  private int maxWarriors;

  @Value("${url}")
  private String url;

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
    List<Warrior> army = createWarriorList(warriors);

    System.out.println("Prepare to battle! Enemy army = " + enemyWarriors + " vs our army: " + warriors);

    // вместо serializationDataFormat в конфиге можно задать параметр default-serialization-format: application/json
    ObjectValue armyJson = Variables.objectValue(army).serializationDataFormat("application/json").create();

    delegateExecution.setVariable("army", army);
    delegateExecution.setVariable("armyJson", armyJson);
    delegateExecution.setVariable("enemyWarriors", enemyWarriors);
  }

  private List<Warrior> createWarriorList(int warriors) {
    List<Warrior> army = new ArrayList<>();
    HttpConnector httpConnector = Connectors.getConnector(HttpConnector.ID);
    HttpRequest request = httpConnector.createRequest()
        .get()
        .url(url + "&_quantity=" + warriors);

    Map<String, String> headers = new HashMap<>();
    request.setRequestParameter("headers", headers );

    headers.put("Content-type", "application/json");
    HttpResponse response = request.execute();

    if (response.getStatusCode() == 200) {
      SpinJsonNode node = JSON(response.getResponse());
      node = node.prop("data");

      for (int i = 0; i < warriors; i++) {
        SpinJsonNode childNode = node.elements().get(i);
        Warrior warrior = new Warrior();
        // Разбираем запрос вручну
        warrior.setName(childNode.prop("name").stringValue());
        warrior.setTitle(childNode.prop("title").stringValue());
        warrior.setHp(childNode.prop("number").numberValue().intValue());

        // mapping через camunda spin
//        warrior = childNode.mapTo(Warrior.class);

        warrior.setAlive(true);
        army.add(warrior);
      }
    }
    response.close();

    return army;
  }

}
