package network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;

public class VerifyTransaction {
    public boolean isValidTransactionFormat(String txJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(txJson);

            // Validate basic transaction structure
            return validateBasicTxStructure(rootNode);

        } catch (JsonProcessingException e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
            return false;
        }
    }

    private boolean validateBasicTxStructure(JsonNode tx) {
        // Check for required fields
        if (!tx.has("version") || !tx.has("inputs") || !tx.has("outputs") || !tx.has("locktime")) {
            System.out.println("Error: Missing basic transaction fields");
            return false;
        }

        if (!tx.get("inputs").isArray() || !tx.get("outputs").isArray()) {
            System.out.println("Error: Inputs or outputs is not an array");
            return false;
        }

        // Validate all inputs
        for (JsonNode input : tx.get("inputs")) {
            if (!validateInput(input)) return false;
        }

        // Validate all outputs
        for (JsonNode output : tx.get("outputs")) {
            if (!validateOutput(output)) return false;
        }

        return true;
    }

    private boolean validateInput(JsonNode input) {
        // Check basic input structure
        if (!input.has("previous_tx_hash") ||
                !input.has("previous_output_index") ||
                !input.has("unlocking_script") ||
                !input.has("sequence")) {
            System.out.println("Error: Missing required input fields");
            return false;
        }

        // Validate unlocking script
        JsonNode unlockingScript = input.get("unlocking_script");
        if (!unlockingScript.has("scriptSig") || !unlockingScript.get("scriptSig").isArray()) {
            System.out.println("Error: Invalid unlocking script structure");
            return false;
        }

        return true;
    }

    private boolean validateOutput(JsonNode output) {
        // Check basic output structure
        if (!output.has("amount") || !output.has("locking_script")) {
            System.out.println("Error: Missing required output fields");
            return false;
        }

        // Validate locking script
        JsonNode lockingScript = output.get("locking_script");
        if (!lockingScript.has("scriptPubKey") || !lockingScript.get("scriptPubKey").isArray()) {
            System.out.println("Error: Invalid locking script structure");
            return false;
        }

        return true;
    }
}
