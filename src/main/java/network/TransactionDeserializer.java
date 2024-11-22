package network;

import com.google.gson.*;
import transaction.*;
import java.util.*;

public class TransactionDeserializer {

    public static Transaction deserialize(String jsonStr) {
        JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
        return parseTransaction(jsonObject);
    }

    private static Transaction parseTransaction(JsonObject txJson) {
        int version = txJson.get("version").getAsInt();
        long locktime = txJson.get("locktime").getAsLong();

        List<Input> inputs = parseInputs(txJson.getAsJsonArray("inputs"));
        List<Output> outputs = parseOutputs(txJson.getAsJsonArray("outputs"));

        return new Transaction(version, inputs, outputs, locktime);
    }

    private static List<Input> parseInputs(JsonArray inputsJson) {
        List<Input> inputs = new ArrayList<>();

        for (JsonElement inputElement : inputsJson) {
            JsonObject inputJson = inputElement.getAsJsonObject();

            String prevTxHash = inputJson.get("previous_tx_hash").getAsString();
            long prevOutputIndex = inputJson.get("previous_output_index").getAsLong();

            JsonObject unlockingScriptJson = inputJson.getAsJsonObject("unlocking_script");
            JsonArray scriptSigArray = unlockingScriptJson.getAsJsonArray("scriptSig");

            List<String> scriptSig = new ArrayList<>();
            scriptSigArray.forEach(element -> scriptSig.add(element.getAsString()));

            UnlockingScript unlockingScript = new UnlockingScript(scriptSig);
            inputs.add(new Input(prevTxHash, prevOutputIndex, unlockingScript));
        }

        return inputs;
    }

    private static List<Output> parseOutputs(JsonArray outputsJson) {
        List<Output> outputs = new ArrayList<>();

        for (JsonElement outputElement : outputsJson) {
            JsonObject outputJson = outputElement.getAsJsonObject();

            double amount = outputJson.get("amount").getAsDouble();
            JsonObject lockingScriptJson = outputJson.getAsJsonObject("locking_script");
            JsonArray scriptPubKeyArray = lockingScriptJson.getAsJsonArray("scriptPubKey");

            List<String> scriptPubKey = new ArrayList<>();
            scriptPubKeyArray.forEach(element -> scriptPubKey.add(element.getAsString()));

            LockingScript lockingScript = new LockingScript(scriptPubKey);
            outputs.add(new Output(amount, lockingScript));
        }

        return outputs;
    }
}