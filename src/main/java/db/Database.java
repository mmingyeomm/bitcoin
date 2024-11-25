package db;

import org.json.JSONArray;
import org.json.JSONObject;
import utxo.UTXO;
import utxo.UTXOSet;
import utxo.ScriptPubKey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DB_FILE = "src/main/java/db/initialUTXOSet.json";
    private UTXOSet utxoSet;

    public Database() {
        loadUTXOs();
    }

    private void loadUTXOs() {
        try {

            String content = new String(Files.readAllBytes(Paths.get(DB_FILE)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray utxosArray = jsonObject.getJSONArray("utxos");

            List<UTXO> utxoList = new ArrayList<>();

            // Parse each UTXO in the array
            for (int i = 0; i < utxosArray.length(); i++) {
                JSONObject utxoJson = utxosArray.getJSONObject(i);
                UTXO utxo = new UTXO();

                // Set UTXO fields
                utxo.setTxid(utxoJson.getString("txid"));
                utxo.setVout(utxoJson.getInt("vout"));
                utxo.setAmount(utxoJson.getDouble("amount"));

                // Parse and set ScriptPubKey
                JSONObject scriptPubKeyJson = utxoJson.getJSONObject("script_pubkey");
                ScriptPubKey scriptPubKey = new ScriptPubKey();
                scriptPubKey.setAsm(scriptPubKeyJson.getString("asm"));
                scriptPubKey.setHex(scriptPubKeyJson.getString("hex"));
                scriptPubKey.setType(scriptPubKeyJson.getString("type"));

                utxo.setScriptPubkey(scriptPubKey);
                utxoList.add(utxo);
            }

            utxoSet = new UTXOSet(utxoList);
            System.out.println("database loaded with UTXOs");

        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            utxoSet = new UTXOSet(); // Initialize empty UTXOSet in case of error
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            utxoSet = new UTXOSet(); // Initialize empty UTXOSet in case of error
        }
    }

    public UTXOSet getUTXOSet() {
        return utxoSet;
    }

    public void saveUTXOs() {

    }
}
