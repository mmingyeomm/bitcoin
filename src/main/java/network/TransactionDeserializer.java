package network;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import transaction.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDeserializer extends JsonDeserializer<Transaction> {

    @Override
    public Transaction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        System.out.println("Deserializer");
        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        JsonNode node = mapper.readTree(p);

        // Parse version
        int version = node.get("version").asInt();

        // Parse inputs
        List<Input> inputs = new ArrayList<>();
        JsonNode inputsNode = node.get("inputs");
        if (inputsNode.isArray()) {
            for (JsonNode inputNode : inputsNode) {
                String prevTxHash = inputNode.get("previous_tx_hash").asText();
                long prevOutputIndex = inputNode.get("previous_output_index").asLong();

                // Parse unlocking script
                JsonNode unlockingScriptNode = inputNode.get("unlockingScript");
                List<String> scriptSig = new ArrayList<>();
                if (unlockingScriptNode.get("scriptSig").isArray()) {
                    for (JsonNode scriptNode : unlockingScriptNode.get("scriptSig")) {
                        scriptSig.add(scriptNode.asText());
                    }
                }
                UnlockingScript unlockingScript = new UnlockingScript(scriptSig);

                inputs.add(new Input(prevTxHash, prevOutputIndex, unlockingScript));
            }
        }

        // Parse outputs
        List<Output> outputs = new ArrayList<>();
        JsonNode outputsNode = node.get("outputs");
        if (outputsNode.isArray()) {
            for (JsonNode outputNode : outputsNode) {
                double amount = outputNode.get("amount").asDouble();

                // Parse locking script
                JsonNode lockingScriptNode = outputNode.get("lockingScript");
                List<String> scriptPubKey = new ArrayList<>();
                if (lockingScriptNode.get("scriptPubKey").isArray()) {
                    for (JsonNode scriptNode : lockingScriptNode.get("scriptPubKey")) {
                        scriptPubKey.add(scriptNode.asText());
                    }
                }
                LockingScript lockingScript = new LockingScript(scriptPubKey);

                outputs.add(new Output(amount, lockingScript));
            }
        }

        // Parse locktime and type
        long locktime = node.get("locktime").asLong();
        String type = node.get("type").asText();

        return new Transaction(version, inputs, outputs, locktime, type);
    }
}
