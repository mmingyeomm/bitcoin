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

            for (int i = 0; i < utxosArray.length(); i++) {
                JSONObject utxoJson = utxosArray.getJSONObject(i);
                UTXO utxo = new UTXO();

                utxo.setTxid(utxoJson.getString("txid"));
                utxo.setVout(utxoJson.getInt("vout"));
                utxo.setAmount(utxoJson.getDouble("amount"));

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
            utxoSet = new UTXOSet();
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            utxoSet = new UTXOSet();
        }
    }

    public UTXOSet getUTXOSet() {
        return utxoSet;
    }

    public void removeUTXO(String txid) {
        if (txid == null || txid.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
        System.out.println("Removing UTXO: " + txid);

        List<UTXO> currentUTXOs = utxoSet.getUtxos();

        // UTXO 존재 여부 확인
        boolean utxoExists = currentUTXOs.stream()
                .anyMatch(utxo -> utxo.getTxid().equals(txid));

        if (!utxoExists) {
            throw new IllegalStateException("UTXO with txid " + txid + " does not exist in the UTXO set");
        }

        try {
            int sizeBefore = currentUTXOs.size();
            currentUTXOs.removeIf(utxo -> utxo.getTxid().equals(txid));
            int sizeAfter = currentUTXOs.size();

            // 실제로 제거된 UTXO 수 확인
            int removedCount = sizeBefore - sizeAfter;

            utxoSet = new UTXOSet(currentUTXOs);
            System.out.println("Successfully removed " + removedCount + " UTXO(s) with txid: " + txid);

            // UTXO set의 현재 상태 로깅
            System.out.println("Current UTXO set size: " + sizeAfter);

        } catch (Exception e) {
            String errorMsg = "Error while removing UTXO with txid " + txid;
            System.err.println(errorMsg + ": " + e.getMessage());
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void addUTXO(UTXO utxo) {
        if (utxo == null) {
            System.err.println("Cannot add null UTXO");
            return;
        }
        List<UTXO> currentUTXOs = utxoSet.getUtxos();
        // 동일한 UTXO가 이미 존재하는지 확인
        boolean exists = currentUTXOs.stream()
                .anyMatch(existing ->
                        existing.getTxid().equals(utxo.getTxid()) &&
                                existing.getVout() == utxo.getVout()
                );

        if (!exists) {
            currentUTXOs.add(utxo);
            utxoSet = new UTXOSet(currentUTXOs);
            System.out.println("New UTXO added: " + utxo.getTxid() + " (vout: " + utxo.getVout() + ")");
        } else {
            System.out.println("UTXO already exists: " + utxo.getTxid() + " (vout: " + utxo.getVout() + ")");
        }
    }

    public void updateUTXOSet(List<UTXO> newUTXOs) {
        utxoSet = new UTXOSet(newUTXOs);
        System.out.println("UTXOSet updated in memory");
    }






}
