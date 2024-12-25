package ru.neoflex.education.camunda;

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

import java.util.*;

import static org.camunda.spin.Spin.JSON;

@Component
public class PrepareToBattle implements JavaDelegate {

    @Value("${maxWarriors}")
    private int maxWarriors;

    @Value("${url}")
    private String url;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        int warriors = (int) delegateExecution.getVariable("warriors");
        int enemyWarriors = (int) (Math.random() * 100);
        maxWarriors = maxWarriors == 0 ? 100 : maxWarriors;

        if (warriors < 1 || warriors > maxWarriors) {
            throw new BpmnError("warriorsError");
        }

        List army = new ArrayList<>();

        for (int i = 0; i <= warriors; i++) {
            army.add(create());
        }

        System.out.println("Prepare to battle! Enemy army = " + enemyWarriors + " vs. our army: " + warriors);


        ObjectValue jsonArmy = Variables.objectValue(army).serializationDataFormat("application/json").create();

        delegateExecution.setVariable("army", army);
        delegateExecution.setVariable("jsonArmy", jsonArmy);
        delegateExecution.setVariable("enemyWarriors", enemyWarriors);
    }

    private Warrior create() {
        try {
            HttpConnector httpConnector = Connectors.getConnector(HttpConnector.ID);
            HttpRequest request = httpConnector.createRequest()
                    .url(url)
                    .get();

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            request.setRequestParameter("headers", headers);

            HttpResponse response = request.execute();

            if (response.getStatusCode() == 200 && response.getResponse() != null && !response.getResponse().isEmpty()) {
                return JSON(response.getResponse()).mapTo(Warrior.class);
            } else {
                throw new RuntimeException("Invalid response from server: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error during warrior creation: " + e.getMessage());
            e.printStackTrace();
            return generateMockWarrior();
        }
    }


    private Warrior generateMockWarrior() {
        Warrior mockWarrior = new Warrior();
        mockWarrior.setName("Mock Warrior " + UUID.randomUUID());
        mockWarrior.setTitle("Mock Title");
        mockWarrior.setHp((int) (Math.random() * 100 + 50));
        mockWarrior.setIsAlive(true);
        System.out.println("Generated mock warrior: " + mockWarrior.getName());
        return mockWarrior;
    }

}
