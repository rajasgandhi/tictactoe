package com.weebly.nsgamezz.tictactoe;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by iankc on 11/8/16.
 */

class BluetoothHelper {

    static final UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "BLUETOOTHHELPER";
    public static final int NO_OPPONENT = 1;
    public static final int MULTIPLE_OPPONENTS = 2;
    public static final int BLUETOOTH_NOT_ENABLED = 3;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice opponent;
    private Handler mhandler;

    BluetoothHelper(Handler _mhandler) throws BluetoothAccessException {
        Log.v(TAG, "Bluetooth Helper Constructor called");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mhandler = _mhandler;
        checkBluetoothCapability();
        getOpponent();
    }

    private void getOpponent() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() <= 0) {
            mhandler.obtainMessage(Player2Activity2Devices.BLUETOOTH_ERROR, NO_OPPONENT, 0).sendToTarget();
        } else if (pairedDevices.size() >= 2) {
            mhandler.obtainMessage(Player2Activity2Devices.BLUETOOTH_ERROR, MULTIPLE_OPPONENTS, 0).sendToTarget();
        } else {
            Log.v(TAG, "Bluetooth opponent found");
            List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>(pairedDevices);
            opponent = devices.get(0);
            Log.v(TAG, opponent.getName() + "\t" + opponent.getAddress());
            mhandler.obtainMessage(Player2Activity2Devices.BLUETOOTH_VALID, opponent.getName());
        }
    }

    private void checkBluetoothCapability() throws BluetoothAccessException {
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            throw new BluetoothAccessException();
        }
        Log.v(TAG, "Supports Bluetooth");
        if (!mBluetoothAdapter.isEnabled()) {
            Log.v(TAG, "Bluetooth is not enabled");
            mhandler.obtainMessage(Player2Activity2Devices.BLUETOOTH_ERROR, BLUETOOTH_NOT_ENABLED, 0).sendToTarget();
        }
    }

    void startServer(Handler mhandler) {
        Server server = new Server(mBluetoothAdapter, mhandler);
        server.start();
    }

    void makeClientRequest(Handler mhandler, Delta change) {
        Client client = new Client(opponent, mhandler, change);
        client.start();
    }
}
